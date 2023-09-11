package horse.boo.bot.services;

import horse.boo.bot.database.repository.ConfigRepository;
import horse.boo.bot.database.repository.LocaleRepository;
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
    private final LocaleRepository localeRepository;

    public BotReadyService(ConfigRepository configRepository, LocaleRepository localeRepository) {
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
        if (localeRepository.getLocalesTableByGuildId(guild.getIdLong()) == null) {
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

        guild.getTextChannelById(config.getBotReadinessChannelId()).sendMessageEmbeds(getMessageEmbed(guild, "bot")).queue();
        guild.getTextChannelById(config.getLogChannelId()).sendMessageEmbeds(getMessageEmbed(guild, "log")).queue();
        logger.info("I work in the guild:" + guild.getName());
    }

    @NotNull
    private static MessageEmbed getMessageEmbed(Guild guild, String embedType) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Last Update: 11.09.2023");
        if (embedType.equals("log")) {
            eb.addField("What's new:", """
                    * Fix old bugs
                    * Add new bugs
                    * Added some pieces
                    * Added disabling the functionality of deleting unrelated by slash command(ONLY FOR ADMINS)
                    * Added dice throwing functionality""", false);
        } else {
            eb.addField("What's new:", """
                    * Fix old bugs
                    * Add new bugs
                    * Added some pieces
                    * Added dice throwing functionality
                    * Added functionality to capture the government of Africa! =)""", false);
        }
        eb.setFooter("Lyra_Heartstrings   Ver: 5.3.5");
        eb.setImage(guild.getSelfMember().getAvatarUrl());
        eb.setColor(Color.GREEN).build();
        return eb.build();
    }

}