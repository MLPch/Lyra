package horse.boo.bot.services.slashcommands.handlers;

import horse.boo.bot.database.repository.ConfigRepository;
import horse.boo.bot.database.repository.LocaleRepository;
import horse.boo.bot.database.table.ConfigsTable;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.Objects;

@Component
public class EmbedConstructorService extends ListenerAdapter {
    //TODO: Пометить завершённые классы от которых нет наследования как final
    private final Logger logger = LoggerFactory.getLogger(EmbedConstructorService.class);
    private final ConfigRepository configRepository;
    private final LocaleRepository localeRepository;

    public String type = "default";
    private String embedType = "default";

    public EmbedConstructorService(ConfigRepository configRepository, LocaleRepository localeRepository) {
        this.configRepository = configRepository;
        this.localeRepository = localeRepository;
    }


    @NotNull
    public static SlashCommandData constructorData() {
        return new CommandDataImpl("constructor", "constructor").addSubcommands()
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
    }


    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        SelectOption selectOption = event.getSelectedOptions().get(0);
        Guild guild = event.getGuild();
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

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (event.getModalId().equals("modmail")) {
            var guild = event.getGuild();
            ConfigsTable config = configRepository.getConfigByGuildId(guild.getIdLong());
            var language = config.getBotLanguage();

            event.getInteraction().getValues().forEach(value -> {
                var localeName = embedType + "_" + type + "_" + value.getId();
                var locale = localeRepository.findByGuildIdAndLocaleName(guild.getIdLong(), localeName);
                var oldLocale = localeRepository.findByGuildIdAndLocaleName(guild.getIdLong(), localeName);
                locale.setValueByLanguage(language, event.getInteraction().getValue(value.getId()).getAsString());
                localeRepository.save(locale);
                logger.info("The value in the database has been changed!\n" +
                        "           ###############################################################################\n" +
                        "           Embed: " + embedType + " embed\n" +
                        "           Initiator: " + Objects.requireNonNull(event.getMember()).getUser() + "\n" +
                        "           Old value: " + oldLocale + "\n" +
                        "           New value: " + locale + "\n" +
                        "           ###############################################################################");
            });

            event.reply(Objects.requireNonNull(event.getInteraction().getValue("title")).getAsString()).queue();
        }
    }

    public void constructor(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("constructor")) {
            event.reply("""
                    CAREFULLY STUDY THE TEMPLATE BELOW AND SELECT THE DESIRED EMBED


                    STRING ABOVE
                    """).addEmbeds(getExampleEmbed().build()).addActionRow(getSelectEmbedTypeMenu()).setEphemeral(true).queue();
        }
    }


    @NotNull
    private EmbedBuilder getExampleEmbed() {

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("TITLE");
        eb.addField("FIELD_NAME", "FIELD_VALUE", true);
        eb.setColor(Color.BLACK);
        eb.setTimestamp(OffsetDateTime.now());
        eb.setFooter("FOOTER_TEXT");
        return eb;
    }


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


    @NotNull
    private Modal getEmbedParamsModal(String embedType, Guild guild) {
        String language = configRepository.getConfigByGuildId(guild.getIdLong()).getBotLanguage();
        String shortEmbedType = embedType.split(" ")[0].toLowerCase();
        TextInput stringAbove = TextInput.create("stringAbove", "String Above", TextInputStyle.SHORT)
                .setPlaceholder(localeRepository.findByGuildIdAndLocaleName(guild.getIdLong(),
                        shortEmbedType + "_" + type + "_stringAbove").getValueByLanguage(language))
                .setRequiredRange(1, 2000)
                .setRequired(false)
                .build();

        TextInput title = TextInput.create("title", "Title", TextInputStyle.SHORT)
                .setPlaceholder(localeRepository.findByGuildIdAndLocaleName(guild.getIdLong(),
                        shortEmbedType + "_" + type + "_title").getValueByLanguage(language))
                .setRequiredRange(1, 250)
                .setRequired(false)
                .build();

        TextInput fieldName = TextInput.create("fieldName", "Field Name", TextInputStyle.SHORT)
                .setPlaceholder(localeRepository.findByGuildIdAndLocaleName(guild.getIdLong(),
                        shortEmbedType + "_" + type + "_fieldName").getValueByLanguage(language))
                .setRequiredRange(1, 256)
                .setRequired(false)
                .build();

        TextInput fieldValue = TextInput.create("fieldValue", "Field Value", TextInputStyle.SHORT)
                .setPlaceholder(localeRepository.findByGuildIdAndLocaleName(guild.getIdLong(),
                        shortEmbedType + "_" + type + "_fieldValue").getValueByLanguage(language))
                .setRequiredRange(1, 1024)
                .setRequired(false)
                .build();

        TextInput footer = TextInput.create("footerText", "Footer", TextInputStyle.SHORT)
                .setPlaceholder(localeRepository.findByGuildIdAndLocaleName(guild.getIdLong(),
                        shortEmbedType + "_" + type + "_footerText").getValueByLanguage(language))
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
