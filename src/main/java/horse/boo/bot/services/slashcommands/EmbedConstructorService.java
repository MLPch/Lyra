package horse.boo.bot.services.slashcommands;

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
import java.util.Objects;

import static horse.boo.bot.DiscordClient.TYPE;

@Component
public class EmbedConstructorService extends ListenerAdapter {
    private final Logger logger = LoggerFactory.getLogger(EmbedConstructorService.class);
    private final ConfigRepository configRepository;
    private final LocaleRepository localeRepository;

    private String embedType = "default";

    public EmbedConstructorService(ConfigRepository configRepository,
                                   LocaleRepository localeRepository) {
        this.configRepository = configRepository;
        this.localeRepository = localeRepository;
    }

    /**
     * @param event - Срабатывает после выбора строки в меню строк.
     *              При выборе строки открывает соответствующее контексту модальное окно.
     */
    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        SelectOption selectOption = event.getSelectedOptions().get(0);
        Guild guild = event.getGuild();
        assert guild != null;
        switch (selectOption.getValue()) {
            case "unrelated" -> {
                embedType = "unrelated";
                event.replyModal(getEmbedParamsModal("Unrelated embed edit", guild)).queue();
            }
            case "greetings" -> {
                embedType = "greetings";

                event.replyModal(getEmbedParamsModal("Greetings embed edit", guild)).queue();
            }
            case "farewell" -> {
                embedType = "farewell";
                event.replyModal(getEmbedParamsModal("Farewell embed edit", guild)).queue();
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
            assert guild != null;
            ConfigsTable config = configRepository.getConfigByGuildId(guild.getIdLong());
            var language = config.getBotLanguage();

            StringBuilder logMessage = new StringBuilder();
            //                    .append(ZonedDateTime.ofInstant(Instant.now(), ZoneOffset.UTC)
            //                            .format(DateTimeFormatter.ofPattern("dd-MM-yyyyVVHH:mm:ss"+ "\n")))
            logMessage.append("The value in the database has been changed!\n")
                    .append("           ###############################################################################\n")
                    .append("           Embed: ").append(embedType).append(" embed\n")
                    .append("           Date: ").append(OffsetDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))).append("\n")
                    .append("           Initiator: ").append(Objects.requireNonNull(event.getMember()).getUser()).append("\n")
                    .append("           ---------------------------------------------\n");

            event.getInteraction().getValues().forEach(value -> {
                if (value != null) {
                    var localeName = embedType + "_" + TYPE + "_" + value.getId();
                    LocalesTable locale = localeRepository.findByGuildIdAndLocaleName(guild.getIdLong(), localeName);
                    LocalesTable oldLocale = localeRepository.findByGuildIdAndLocaleName(guild.getIdLong(), localeName);
                    locale.setValueByLanguage(language, Objects.requireNonNull(event.getInteraction().getValue(value.getId())).getAsString());
                    localeRepository.save(locale);
                    logMessage.append("           ----------------------------\n")
                            .append("           Old value: ").append(oldLocale).append("\n")
                            .append("           New value: ").append(locale).append("\n")
                            .append("           ----------------------------\n");
                }
            });

            logMessage.append("           ---------------------------------------------\n")
                    .append("           ###############################################################################");
            logger.info(logMessage.toString());
            event.reply("Success!").queue();
        }
    }

    /**
     * @param event - Срабатывает при вызове команды "constructor".
     *              Выводит в чат предупреждение, шаблонный эмбед и меню для выбора изменяемого эмбеда.
     *              Сообщение с выводом видит только получатель.
     */
    public void constructor(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("constructor")) {
            event.reply("""
                    *The embed fields are applied to the currently selected language in the configuration.*
                    *To change the language of the edited embed, use the /select_language command.*
                                        
                    __CAREFULLY STUDY THE TEMPLATE BELOW AND SELECT THE DESIRED EMBED__
                                        
                    STRING ABOVE
                    """).addEmbeds(getExampleEmbed()).addActionRow(getSelectEmbedTypeMenu()).setEphemeral(true).queue();
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
                .addOptions(SelectOption.of("Unrelated", "unrelated") // another way to create a SelectOption
                        .withDescription("Edit unrelated embed") // this time with a description
                        .withEmoji(Emoji.fromUnicode("\uD83D\uDEAE")) // and an emoji
                        .withDefault(false)) // while also being the default option
                .addOptions(SelectOption.of("Greetings", "greetings") // another way to create a SelectOption
                        .withDescription("Edit welcome embed") // this time with a description
                        .withEmoji(Emoji.fromUnicode("❤")) // and an emoji
                        .withDefault(false)) // while also being the default option
                .addOptions(SelectOption.of("Farewell", "farewell") // another way to create a SelectOption
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
        String language = configRepository.getConfigByGuildId(guild.getIdLong()).getBotLanguage();
        String shortEmbedType = embedType.split(" ")[0].toLowerCase();
        TextInput stringAbove = TextInput.create("stringAbove", "String Above", TextInputStyle.SHORT)
                .setPlaceholder(localeRepository.findByGuildIdAndLocaleName(guild.getIdLong(),
                        shortEmbedType + "_" + TYPE + "_stringAbove").getValueByLanguage(language))
                .setRequiredRange(1, 2000)
                .setRequired(false)
                .build();

        TextInput title = TextInput.create("title", "Title", TextInputStyle.SHORT)
                .setPlaceholder(localeRepository.findByGuildIdAndLocaleName(guild.getIdLong(),
                        shortEmbedType + "_" + TYPE + "_title").getValueByLanguage(language))
                .setRequiredRange(1, 250)
                .setRequired(false)
                .build();

        TextInput fieldName = TextInput.create("fieldName", "Field Name", TextInputStyle.SHORT)
                .setPlaceholder(localeRepository.findByGuildIdAndLocaleName(guild.getIdLong(),
                        shortEmbedType + "_" + TYPE + "_fieldName").getValueByLanguage(language))
                .setRequiredRange(1, 256)
                .setRequired(false)
                .build();

        TextInput fieldValue = TextInput.create("fieldValue", "Field Value", TextInputStyle.SHORT)
                .setPlaceholder(localeRepository.findByGuildIdAndLocaleName(guild.getIdLong(),
                        shortEmbedType + "_" + TYPE + "_fieldValue").getValueByLanguage(language))
                .setRequiredRange(1, 1024)
                .setRequired(false)
                .build();

        TextInput footer = TextInput.create("footerText", "Footer", TextInputStyle.SHORT)
                .setPlaceholder(localeRepository.findByGuildIdAndLocaleName(guild.getIdLong(),
                        shortEmbedType + "_" + TYPE + "_footerText").getValueByLanguage(language))
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
}
