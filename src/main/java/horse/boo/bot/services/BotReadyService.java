package horse.boo.bot.services;

import horse.boo.bot.database.repository.ConfigRepository;
import horse.boo.bot.database.repository.LocaleRepository;
import horse.boo.bot.database.table.ConfigsTable;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions.enabledFor;
import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

@Component
public class BotReadyService extends ListenerAdapter {
    private final Logger logger = LoggerFactory.getLogger(BotReadyService.class);
    private final ConfigRepository configRepository;
    private final LocaleRepository localeRepository;

    public BotReadyService(ConfigRepository configRepository,
                           LocaleRepository localeRepository) {
        this.configRepository = configRepository;
        this.localeRepository = localeRepository;
    }


    /**
     * @param event - реагирует на первое подключение бота к гильдии
     *              Записывает начальный конфиг для каждой гильдии к которой подключается.
     *              Срабатывает только при приглашении бота в гильдию.
     */
    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        var guild = event.getGuild();
        if (configRepository.getConfigByGuildId(guild.getIdLong()) == null) {
            ConfigsTable config = new ConfigsTable(guild);
            configRepository.save(config);
            logger.info("Joined a new guild! Name: " + guild.getName());
            logger.info("Saved the default config: " + config);
        }
        if (localeRepository.findLocalesTableByGuildId(guild.getIdLong()) == null) {
            //TODO: Написать функционал создания языкового набора при отсутствии
        }
    }

    /**
     * @param event - реагирует на включение (этого) бота находящегося в гильдии
     *              Сообщает об обновлениях при запуске бота.
     */
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        Guild guild = event.getGuild();
        logger.info("I work in the guild: " + guild.getName() + " (id=" + guild.getIdLong() + ")");

        event.getGuild().updateCommands().addCommands(
                Commands.slash("select_language", "Choice language")
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),

                Commands.slash("setup", "BOT SETTINGS MENU")
                        .addOption(CHANNEL, "admin_channel", "The channel where admins sit:", false)
                        .addOption(INTEGER, "unrelated-count", "Number of emojis to delete a message:", false)
                        .addOption(STRING, "unrelated-emote", "Emoji ID to delete unrelated:", false)
                        .addOption(CHANNEL, "join-channel", "The channel in which the bot will welcome users:", false)
                        .addOption(CHANNEL, "leave-channel", "The channel in which the bot will say goodbye to users:", false)
                        .addOption(CHANNEL, "logs", "The channel to which the logs will fly:", false)
                        .addOption(CHANNEL, "bot_info_channel", "The channel into which messages about bot updates will fall:", false)
                        .addOption(INTEGER, "unrelated_delete_time", "Delay time of the unrelated deletion message (in seconds):", false)
                        .addOption(BOOLEAN, "music_player", "Enabling the music player functionality:", false)
                        .addOption(BOOLEAN, "remembering_roles", "Enabling the role remembering functionality:", false)
                        .addOption(BOOLEAN, "dice_roller", "Enabling the functionality for throwing dices:", false)
                        .addOption(BOOLEAN, "unrelated_deleter", "Enabling the unrelated removal functionality:", false)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),

                Commands.slash("disable_functionals_in_channel", "Disable functionals in select channel")
                        .addOption(CHANNEL, "disable_in_channel", "The channel for which you need to disable the functionality:", true)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),

                Commands.slash("enable_functionals_in_channel", "Enable functionals in select channel")
                        .addOption(CHANNEL, "enable_in_channel", "The channel for which you need to enable the functionality:", true)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),

                Commands.slash("constructor", "Embeds constructor").addSubcommands()
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),

                Commands.slash("roll", "Enter a value or press Enter to select")
                        .addOption(STRING, "dice", "From 1d2 to 100d100", false)
                        .setDefaultPermissions(enabledFor(Permission.VIEW_CHANNEL)),

                Commands.slash("send_update", "!!!ONLY FOR THE BOT DEVELOPER!!!")
                        .setDefaultPermissions(enabledFor(Permission.ADMINISTRATOR))

        ).queue();

    }

}