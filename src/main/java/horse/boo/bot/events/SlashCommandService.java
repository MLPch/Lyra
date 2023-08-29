package horse.boo.bot.events;


import horse.boo.bot.database.enums.Languages;
import horse.boo.bot.database.repository.ConfigRepository;
import horse.boo.bot.database.repository.LocaleRepository;
import horse.boo.bot.database.table.ConfigsTable;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

@Component
public class SlashCommandService extends ListenerAdapter {
    //TODO: Вынести сюда все шаги по настройке.

    private final Logger logger = LoggerFactory.getLogger(BotReadyService.class);
    private final ConfigRepository configRepository;
    private final LocaleRepository localeRepository;
    public String type = "default";

    public SlashCommandService(ConfigRepository configRepository, LocaleRepository localeRepository) {
        this.configRepository = configRepository;
        this.localeRepository = localeRepository;
    }


    /**
     * @param event - реагирует на любое сообщение в чате
     *              <p>
     *              Первая версия функционала реагирования на команды в чате, пока актуально, но надо бы убрать и сделать слэшами
     */
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.isFromGuild()) {
            Guild guild = event.getGuild();
            ConfigsTable config = configRepository.getConfigByGuildId(guild.getIdLong());
            String language = config.getBotLanguage();
            var msg = event.getMessage();
            if (msg.getContentRaw().equals("g34tw7b8cg7qt43")) { //for tests

                //TODO: 3 строчки ниже пригодятся при заделке интерфейса смены текстов
                EntitySelectMenu menu = EntitySelectMenu.create("menu:class", EntitySelectMenu.SelectTarget.CHANNEL)
                        .setPlaceholder("Select a channel for logs").setRequiredRange(1, 1) // must select exactly one
                        .build();
                logger.debug("lyrastart");
//            localeConfig.setBotId(event.getGuild().getSelfMember().getIdLong());
                logger.debug("I set myself an ID. Again.");
//            localeConfig.setBotLanguage(getActualLanguage());
                System.out.println("Now there will be a language - " + language);
//            guild.getTextChannelsByName("test", true).get(0).sendMessage(language.getLanguage()).addActionRow().complete();
//            guild.getTextChannelsByName("test", true).get(0).sendMessage(getActualLanguage().toString()).addActionRow(menu).complete();
                guild.getTextChannelsByName("test", true).get(0)
                        .sendMessage(localeRepository.getValueByLanguageAndLocaleNameAndGuild(language, "greetings_" + type + "_fieldName", guild)).complete();
            }
        }
    }

    public void languageSelect(@NotNull SlashCommandInteractionEvent event) {


        MessageEmbed eb = new EmbedBuilder()
                .setAuthor("Welcome to the Setup wizard!")
                .addField("To continue - please select a language.", "There are currently 4 languages available.", true)
                .setColor(Color.magenta)
                .build();

        event.reply("").setEmbeds(eb).addActionRow(
                        Button.danger("language.english", "English"),
                        Button.danger("language.russian", "Russian"),
                        Button.danger("language.ukrainian", "Ukrainian"),
                        Button.danger("language.china", "China"))
                .queue();
    }


    public void setup(@NotNull SlashCommandInteractionEvent event, String content) {
        var guild = event.getGuild();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("BOT SETTINGS UPDATED");

        boolean zaglushka = false;

        ConfigsTable config = configRepository.findByGuildId(guild.getIdLong()).orElseGet(() -> new ConfigsTable(guild));

        config.setGuildId(guild.getIdLong());
        if (event.getOption("admin_channel") != null) {
            config.setAdminChannelId(event.getOption("admin_channel").getAsChannel().getIdLong());
            eb.addField("", "\n✎ __" + event.getOption("admin_channel").getAsChannel()
                    + "__ is designated as the administrators channel.", false);
        }
        config.setBotId(guild.getSelfMember().getIdLong());
        if (event.getOption("unrelated-count") != null) {
            config.setUnrelatedEmoteCount(event.getOption("unrelated-count").getAsInt());
            eb.addField("", "\n✎ __" + event.getOption("unrelated-count").getAsInt()
                    + "__ this is how many seconds the message about deleting a post in the guild is now displayed", false);
        }
        if (event.getOption("unrelated-emote") != null) {
            config.setUnrelatedEmoteId(event.getOption("unrelated-emote").getAsLong());
            eb.addField("", "\n✎ " + guild.getEmojiById(event.getOption("unrelated-emote").getAsLong())
                    + " is installed as an emoji to remove unrelated", false);
        }
        if (event.getOption("join-channel") != null) {
            config.setWelcomeChannelId(event.getOption("join-channel").getAsChannel().getIdLong());
            eb.addField("", "\n✎ __" + event.getOption("join-channel").getAsChannel()
                    + "__ is designated as a channel for messages about users entering the server", false);
        }
        if (event.getOption("leave-channel") != null) {
            config.setGoodbyeChannelId(event.getOption("leave-channel").getAsChannel().getIdLong());
            eb.addField("", "\n✎ __" + event.getOption("leave-channel").getAsChannel()
                    + "__ is designated as a channel for messages about users who have left the server", false);
        }
        if (event.getOption("logs") != null) {
            config.setLogChannelId(event.getOption("logs").getAsChannel().getIdLong());
            eb.addField("", "\n✎ __" + event.getOption("logs").getAsChannel()
                    + "__ is designated as a channel for logs", false);
        }
        if (event.getOption("bot-ready") != null) {
            config.setBotReadinessChannelId(event.getOption("bot-ready").getAsChannel().getIdLong());
            eb.addField("", "\n✎ __" + event.getOption("bot-ready").getAsChannel()
                    + "__ is assigned as a channel for bot updates", false);
        }
        if (event.getOption("unrelated_delete_time") != null) {
            config.setUnrelatedDeleteTimeSec(event.getOption("unrelated_delete_time").getAsInt());
            eb.addField("", "\n✎ The following time is set to __" + event.getOption("unrelated_delete_time").getAsInt()
                    + "__ to display the message about the removal of the unrelated", false);
        }
        if (event.getOption("music_player") != null) {
            config.setFunctionMusicPlayer(event.getOption("music_player").getAsBoolean());
            eb.addField("", "\n✎ The functionality of the music player is now __"
                    + getBooleanOnText(event.getOption("music_player").getAsBoolean()) + "__", false);
            if (event.getOption("music_player").getAsBoolean()) {
                zaglushka = true;
            }
        }
        if (event.getOption("role_saver") != null) {
            config.setFunctionRoleSaver(event.getOption("role_saver").getAsBoolean());
            eb.addField("", "\n✎ The functionality of saving roles now __"
                    + getBooleanOnText(event.getOption("role_saver").getAsBoolean()) + "__", false);
            if (event.getOption("role_saver").getAsBoolean()) {
                zaglushka = true;
            }
        }
        if (event.getOption("dice_roller") != null) {
            config.setFunctionDiceRoller(event.getOption("dice_roller").getAsBoolean());
            eb.addField("", "\n✎ The dice-throwing functionality is now __"
                    + getBooleanOnText(event.getOption("dice_roller").getAsBoolean()) + "__", false);
            if (event.getOption("dice_roller").getAsBoolean()) {
                zaglushka = true;
            }
        }
        if (event.getOption("unrelated_deleter") != null) {
            config.setFunctionUnrelatedDeleter(event.getOption("unrelated_deleter").getAsBoolean());
            eb.addField("", "\n✎ The unrelated removal functionality is now __"
                    + getBooleanOnText(event.getOption("unrelated_deleter").getAsBoolean()) + "__", false);
        }
        //TODO: Убрать заглушку реализовав функционалы
        if (zaglushka) {
            eb.setAuthor("Attention! At the moment, the functionality {music_player, role_saver, dice_roller} does not work. Wait in the next versions.");
        }

        eb.setColor(Color.YELLOW);
        eb.setTimestamp(OffsetDateTime.now());

        configRepository.save(config);
        event.reply(content).setEmbeds(eb.build()).setEphemeral(true).queue();
        guild.getTextChannelById(config.getLogChannelId()).sendMessage("").setEmbeds(eb.build()).queue();
        for (MessageEmbed.Field field : eb.build().getFields()) {
            logger.info("CONFIG UPDATED: " + field.getValue());
        }

    }


    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var commands = event.getJDA().updateCommands()
                .addCommands(getSetupSlashCommandData())
                .addCommands(getLanguageSelectSlashCommandData());
        commands.queue();

        // Only accept commands from guilds
        if (event.getGuild() == null) return;
        switch (event.getName()) {
            case "language_select" -> // 2 stage command with a button prompt
//                start(event, event.getOption("content").getAsString());
                    languageSelect(event);
            case "setup" -> setup(event, "");
            default -> event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
        }

    }

    @NotNull
    private static SlashCommandData getLanguageSelectSlashCommandData() {
        return new CommandDataImpl("language_select", "Choice language")
//                .addOption(STRING, "content", "ТЕСТОВАЯ КОМАНДА", false)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
    }

    @NotNull
    private static SlashCommandData getSetupSlashCommandData() {
        return new CommandDataImpl("setup", "BOT SETTINGS MENU")
                .addOption(CHANNEL, "admin_channel", "The channel where admins sit:", false)
                .addOption(INTEGER, "unrelated-count", "Number of emojis to delete a message:", false)
                .addOption(STRING, "unrelated-emote", "Emoji ID to delete unrelated:", false)
                .addOption(CHANNEL, "join-channel", "The channel in which the bot will welcome users:", false)
                .addOption(CHANNEL, "leave-channel", "The channel in which the bot will say goodbye to users:", false)
                .addOption(CHANNEL, "logs", "The channel to which the logs will fly:", false)
                .addOption(CHANNEL, "bot-ready", "The channel into which messages about bot updates will fall:", false)
                .addOption(INTEGER, "unrelated_delete_time", "Delay time of the unrelated deletion message (in seconds):", false)
                .addOption(BOOLEAN, "music_player", "Enabling the music player functionality:", false)
                .addOption(BOOLEAN, "role_saver", "Enabling the role saving functionality:", false)
                .addOption(BOOLEAN, "dice_roller", "Enabling the functionality for throwing cubes:", false)
                .addOption(BOOLEAN, "unrelated_deleter", "Enabling the unrelated removal functionality:", false)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
    }


    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        var guild = event.getGuild();
        ConfigsTable config = configRepository.findByGuildId(guild.getIdLong()).orElseGet(() -> new ConfigsTable(guild));
        String componentId = event.getComponentId();
        Languages lang = null;
        EmbedBuilder eb = new EmbedBuilder();
        switch (componentId) {
            case "language.english" -> {
                eb.setTitle("The English language of the customizer is selected.").setColor(Color.blue);
                lang = Languages.ENGLISH;
            }
            case "language.russian" -> {
                eb.setTitle("Выбран русский язык настройщика.").setColor(Color.green);
                lang = Languages.RUSSIAN;
            }
            case "language.ukrainian" -> {
                eb.setTitle("Обрано українську мову настроювача.").setColor(Color.yellow);
                lang = Languages.UKRAINE;
            }
            case "language.china" -> {
                eb.setTitle("选择定制器的中文语言。").setColor(Color.red);
                lang = Languages.CHINA;
            }

        }
        event.getMessage().delete().queue();
        event.getChannel().sendMessageEmbeds(eb.build()).delay(7, TimeUnit.SECONDS).flatMap(Message::delete).queue();
        config.setBotLanguage(lang.getLanguage());
        event.deferEdit().queue();
        configRepository.save(config);
        logger.info("The bot language is set in the guild (" + guild.getName() + "(" + guild.getIdLong() + ")): " + lang);
    }

    public String getBooleanOnText(boolean status) {
        if (status) {
            return "ON";
        } else {
            return "OFF";
        }
    }


}

