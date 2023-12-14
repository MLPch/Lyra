package horse.boo.bot.services.slashcommands;

import horse.boo.bot.database.enums.FieldType;
import horse.boo.bot.database.enums.Languages;
import horse.boo.bot.database.repository.ConfigRepository;
import horse.boo.bot.database.repository.LocaleRepository;
import horse.boo.bot.database.table.ConfigsTable;
import horse.boo.bot.database.table.LocalesTable;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static horse.boo.bot.DiscordClient.TYPE;
import static horse.boo.bot.database.enums.FieldType.*;
import static horse.boo.bot.database.enums.LocaleType.*;

@Component
public class EmbedConstructorService extends ListenerAdapter {
    private final Logger logger = LoggerFactory.getLogger(EmbedConstructorService.class);
    private final ConfigRepository configRepository;
    private final LocaleRepository localeRepository;

    private String embedType = "default";

    public EmbedConstructorService(ConfigRepository configRepository, LocaleRepository localeRepository) {
        this.configRepository = configRepository;
        this.localeRepository = localeRepository;
        ;
    }

    /**
     * @param event - Срабатывает после выбора строки в меню строк.
     *              При выборе строки открывает соответствующее контексту модальное окно.
     */
    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        SelectOption selectOption = event.getSelectedOptions().get(0);
        Guild guild = event.getGuild();
        if (guild == null) throw new AssertionError();
        switch (selectOption.getValue()) {
            case "unrelated" -> {
                embedType = UNRELATED.name();
                event.replyModal(getEmbedParamsModal(UNRELATED.name(), guild)).queue();
            }
            case "greetings" -> {
                embedType = GREETINGS.name();

                event.replyModal(getEmbedParamsModal(GREETINGS.name(), guild)).queue();
            }
            case "farewell" -> {
                embedType = FAREWELL.name();
                event.replyModal(getEmbedParamsModal(FAREWELL.name(), guild)).queue();
            }
            default -> throw new IllegalStateException("Unexpected value: " + selectOption.getValue());
        }
    }

    /**
     * @param event - Срабатывает в момент нажатия кнопки "Отправить" в модальном окне.
     *              Меняет значения в БД на те которые были введены в полях модального окна.
     *              Выводит в логи программы информацию о каждом изменении.
     */
    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (event.getModalId().equals("modmail")) {
            var guild = event.getGuild();
            if (guild == null) throw new AssertionError();
            ConfigsTable config = configRepository.getConfigByGuildId(guild.getIdLong());

            StringBuilder logMessage = new StringBuilder();
            logMessage.append("The value in the database has been changed!\n")
                    .append("           #################################################################\n")
                    .append("           Embed: ").append(embedType).append(" embed\n")
                    .append("           Date: ").append(OffsetDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))).append("\n")
                    .append("           Initiator: ").append(Objects.requireNonNull(event.getMember()).getUser()).append("\n")
                    .append("           ---------------------------------------------\n");

            event.getInteraction().getValues().forEach(value -> {
                if (value != null) {
                    LocalesTable selectedLocale =
                            localeRepository.getByGuildIdAndLanguageAndLocaleTypeAndModeTypeAndFieldType(
                                    guild.getIdLong(), config.getBotLanguage(), embedType, TYPE, value.getId());

                    String oldLocaleValue = selectedLocale.getValue();
                    if (!event.getInteraction().getValue(value.getId()).getAsString().isBlank()) {
                        selectedLocale.setValue(event.getInteraction().getValue(value.getId()).getAsString());

                        logMessage.append("           ----------------------------\n")
                                .append("           Old value: ").append(oldLocaleValue).append("\n")
                                .append("           New value: ").append(selectedLocale.getValue()).append("\n")
                                .append("           ----------------------------\n");

                        localeRepository.save(selectedLocale);
                    }
                }
            });

            logMessage.append("           ---------------------------------------------\n")
                    .append("           #################################################################");
            config.sendInLogChannel(guild, logMessage.toString());
            logger.info(logMessage.toString());

            event.reply("Success!").setEphemeral(true).queue();
        }
    }

    /**
     * @param event - Срабатывает при вызове команды "constructor".
     *              Выводит в чат предупреждение, шаблонный эмбед и меню для выбора изменяемого эмбеда.
     *              Сообщение с выводом видит только получатель.
     */
    public void constructor(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("constructor")) {
            event.reply("*The embed fields are applied to the currently selected language in the configuration.*\n" +
                        "*To change the language of the edited embed, use the /select_language command.*\n" +
                        "\n" +
                        "__CAREFULLY STUDY THE TEMPLATE BELOW AND SELECT THE DESIRED EMBED__\n" +
                        "\n" +
                        "STRING ABOVE\n").addEmbeds(getExampleEmbed()).addActionRow(getSelectEmbedTypeMenu()).setEphemeral(true).queue();
        }
    }

    /**
     * @return MessageEmbed - Возвращает шаблонный эмбед для наглядности.
     */
    @NotNull
    private MessageEmbed getExampleEmbed() {

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("TITLE");
        eb.addField("FIELD_NAME", "FIELD_VALUE", true);
        eb.setColor(Color.BLACK);
        eb.setTimestamp(OffsetDateTime.now());
        eb.setFooter("FOOTER_TEXT");
        return eb.build();
    }

    /**
     * @return SelectMenu - Возвращает меню с выбором типа эмбеда.
     */
    @NotNull
    private SelectMenu getSelectEmbedTypeMenu() {
        return StringSelectMenu.create("choose-embed")
                .addOptions(SelectOption.of(UNRELATED.name(),
                                UNRELATED.getLocaleType()) // another way to create a SelectOption
                        .withDescription("Edit unrelated embed") // this time with a description
                        .withEmoji(Emoji.fromUnicode("\uD83D\uDEAE")) // and an emoji
                        .withDefault(false)) // while also being the default option
                .addOptions(SelectOption.of(GREETINGS.name(),
                                GREETINGS.getLocaleType()) // another way to create a SelectOption
                        .withDescription("Edit welcome embed") // this time with a description
                        .withEmoji(Emoji.fromUnicode("❤")) // and an emoji
                        .withDefault(false)) // while also being the default option
                .addOptions(SelectOption.of(FAREWELL.name(),
                                FAREWELL.getLocaleType()) // another way to create a SelectOption
                        .withDescription("Edit farewell embed") // this time with a description
                        .withEmoji(Emoji.fromUnicode("\uD83D\uDC94")) // and an emoji
                        .withDefault(false)) // while also being the default option
                .build();
    }

    /**
     * @param embedType - Тип эмбеда.
     * @param guild     - Гильдия в которой совершается операция.
     * @return Modal - возвращает модальное окно которое в последствии необходимо вызвать.
     */
    @NotNull
    private Modal getEmbedParamsModal(@NotNull String embedType, @NotNull Guild guild) {
        System.out.println(getEmbedValueFromDB(guild, EMBED_FIELD_NAME));
        TextInput stringAbove = TextInput
                .create(EMBED_STRING_ABOVE.getFieldType(), "String Above", TextInputStyle.SHORT)
                .setPlaceholder(getEmbedValueFromDB(guild, EMBED_STRING_ABOVE))
                .setRequiredRange(1, 2000)
                .setRequired(false)
                .build();

        TextInput title = TextInput
                .create(EMBED_TITLE.getFieldType(), "Title", TextInputStyle.SHORT)
                .setPlaceholder(getEmbedValueFromDB(guild, EMBED_TITLE))
                .setRequiredRange(1, 250)
                .setRequired(false)
                .build();

        TextInput fieldName = TextInput
                .create(EMBED_FIELD_NAME.getFieldType(), "Field Name", TextInputStyle.SHORT)
                .setPlaceholder(getEmbedValueFromDB(guild, EMBED_FIELD_NAME))
                .setRequiredRange(1, 256)
                .setRequired(false)
                .build();

        TextInput fieldValue = TextInput
                .create(EMBED_FIELD_VALUE.getFieldType(), "Field Value", TextInputStyle.SHORT)
                .setPlaceholder(getEmbedValueFromDB(guild, EMBED_FIELD_VALUE))
                .setRequiredRange(1, 1024)
                .setRequired(false)
                .build();

        TextInput footer = TextInput
                .create(EMBED_FOOTER_TEXT.getFieldType(), "Footer", TextInputStyle.SHORT)
                .setPlaceholder(getEmbedValueFromDB(guild, EMBED_FOOTER_TEXT))
                .setRequiredRange(1, 2048)
                .setRequired(false)
                .build();

        return Modal.create("modmail", embedType).addComponents(
                ActionRow.of(stringAbove),
                ActionRow.of(title),
                ActionRow.of(fieldName),
                ActionRow.of(fieldValue),
                ActionRow.of(footer)
        ).build();
    }

    private String getEmbedValueFromDB(@NotNull Guild guild, @NotNull FieldType fieldType) {
        ConfigsTable config = configRepository.getConfigByGuildId(guild.getIdLong());
        String value = localeRepository.getByGuildIdAndLanguageAndLocaleTypeAndModeTypeAndFieldType(
                guild.getIdLong(),
                config.getBotLanguage(),
                embedType,
                TYPE,
                fieldType.getFieldType()).getValue();
        if (value.isBlank()) {
            value = "-----VOID-----";
        }
        return value;
    }

    public List<LocalesTable> constructorInitLocalesTable(Guild guild, String mode) {
        List<LocalesTable> localesTableList = new ArrayList<>();
        for (FieldType fieldT : FieldType.values()) {
            switch (fieldT) {
                case CONSTRUCTOR_REPLY -> {
                    for (Languages language : Languages.values()) {
                        LocalesTable initConstructorLocale = getLocalesNewTable(guild, mode, fieldT, language);
                        switch (language) {
                            case ENGLISH -> {
                                initConstructorLocale.setValue(
                                        "*The embed fields are applied to the currently selected language in the configuration.*\n" +
                                        "*To change the language of the edited embed, use the /select_language command.*\n\n" +
                                        "__CAREFULLY STUDY THE TEMPLATE BELOW AND SELECT THE DESIRED EMBED__\n\n");
                                localesTableList.add(initConstructorLocale);
                            }
                            case RUSSIAN -> {
                                initConstructorLocale.setValue(
                                        "*Поля для эмбеда применяются к текущему выбранному языку в конфигурации.*\n" +
                                        "*Чтобы изменить язык редактируемого встраиваемого файла, используйте команду" +
                                        " /select_language.*\n\n" +
                                        "__ВНИМАТЕЛЬНО ИЗУЧИТЕ ПРИВЕДЕННЫЙ НИЖЕ ШАБЛОН И ВЫБЕРИТЕ ЖЕЛАЕМЫЙ ЭМБЕД__\n\n");
                                localesTableList.add(initConstructorLocale);
                            }
                            case UKRAINE -> {
                                initConstructorLocale.setValue(
                                        "*Поля для вбудовування застосовуються до поточної вибраної мови в конфігурації.*\n" +
                                        "*Щоб змінити мову редагованого вбудованого файлу, використовуйте команду" +
                                        " /select_language.*\n\n" +
                                        "__Уважно вивчіть наведений нижче ШАБЛОН і виберіть потрібну ВСТАВКУ__ \n\n");
                                localesTableList.add(initConstructorLocale);
                            }
                            case CHINESE -> {
                                initConstructorLocale.setValue(
                                        "*嵌入字段应用于配置中当前选定的语言。*\n" +
                                        "*要更改编辑的嵌入的语言，请使用/select_language命令。*\n\n" +
                                        "__仔细研究下面的模板并选择所需的嵌入__\n\n");
                                localesTableList.add(initConstructorLocale);
                            }
                        }
                    }
                }
            }
        }
        return localesTableList;
    }

    @NotNull
    private static LocalesTable getLocalesNewTable(@NotNull Guild guild, String mode, @NotNull FieldType fieldT, @NotNull Languages language) {
        LocalesTable initConstructorLocale = new LocalesTable();
        initConstructorLocale.setGuildName(guild.getName());
        initConstructorLocale.setGuildId(guild.getIdLong());
        initConstructorLocale.setModeType(mode);
        initConstructorLocale.setLocaleType(CONSTRUCTOR.name());
        initConstructorLocale.setFieldType(fieldT.getFieldType());
        initConstructorLocale.setLanguage(language.getLanguage());
        return initConstructorLocale;
    }
}
