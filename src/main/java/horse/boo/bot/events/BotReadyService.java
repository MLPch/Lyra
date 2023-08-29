package horse.boo.bot.events;

import horse.boo.bot.database.repository.ConfigRepository;
import horse.boo.bot.database.table.ConfigsTable;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class BotReadyService extends ListenerAdapter {
    private final Logger logger = LoggerFactory.getLogger(BotReadyService.class);
    private final ConfigRepository configRepository;

    public BotReadyService(ConfigRepository configRepository) {
        this.configRepository = configRepository;
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
    }

    /**
     * @param event - реагирует на включение (этого) бота находящегося в гильдии
     *              Сообщает об обновлениях при запуске бота.
     */
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        Guild guild = event.getGuild();
        ConfigsTable config = configRepository.getConfigByGuildId(guild.getIdLong());

//        addField("Что нового:", "\n--Изменены приветствие и прощание." +
//                "\n--Кол-во необходимых для удаления реакций теперь равно четырём(4)." +
//                "\n--Баги превращены в фичи.", true);
        MessageEmbed eb = new EmbedBuilder()
                .setTitle("Last Update: 19.08.2023")
                .addField("What's new:", """

                        * Fix old bugs
                        * Add new bugs
                        * Added some pieces
                        * Added disabling of the unrelated functionality and its configuration
                        * Added support for multiple languages
                        * Added slash commands (so far only for admins)
                        * Added auto-ban functionality for a bad mood, so I advise no one to be sad! =)""", false)
                .setFooter("Lyra_Heartstrings   Ver: 5.0.0")
                .setImage(guild.getSelfMember().getAvatarUrl())
                .setColor(Color.GREEN).build();
        guild.getTextChannelById(config.getBotReadinessChannelId()).sendMessageEmbeds(eb).queue();
        logger.info("I work in the guild:" + guild.getName());
    }

}