package horse.boo.bot.services;

import horse.boo.bot.database.repository.ConfigRepository;
import horse.boo.bot.database.repository.LocaleRepository;
import horse.boo.bot.database.table.ConfigsTable;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static horse.boo.bot.DiscordClient.TYPE;

@Component
public class MemberLeaveService extends ListenerAdapter {
    private final Logger logger = LoggerFactory.getLogger(MemberLeaveService.class);
    private final ConfigRepository configRepository;
    private final LocaleRepository localeRepository;

    public MemberLeaveService(ConfigRepository configRepository, LocaleRepository localeRepository) {
        this.configRepository = configRepository;
        this.localeRepository = localeRepository;
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        Guild guild = event.getGuild();
        User user = event.getUser();
        ConfigsTable config = configRepository.getConfigByGuildId(guild.getIdLong());
        String language = config.getBotLanguage();
        boolean stopped = true;
        String pingUser = user.getAsMention();
        String stringAbove = localeRepository.getValueByLanguageAndLocaleNameAndGuild(language, "farewell_" + TYPE + "_stringAbove", guild);

        while (stopped) {
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (stringAbove != null) {
                guild.getTextChannelById(config.getGoodbyeChannelId()).sendMessage(stringAbove +
                        " " + pingUser + " (" + user.getEffectiveName() + ")!\n").setEmbeds(farewellEmbed(guild, user, language)).queue();
            } else {
                guild.getTextChannelById(config.getGoodbyeChannelId()).sendMessage(
                        pingUser + " (" + user.getEffectiveName() + ")").setEmbeds(farewellEmbed(guild, user, language)).queue();
            }

            stopped = false;
        }
        guild.getTextChannelById(config.getLogChannelId()).sendMessage("**__The user has left the guild:__**" +
                "\nName: " + user.getName() +
                "\nID: " + user.getId() +
                "\nMention: " + user.getAsMention() +
                "\nAvatar: " + user.getAvatarUrl() +
                "\nDate: " + OffsetDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").withZone(ZoneOffset.UTC))).complete();
        logger.info("The user has left the guild: " + user + " from " + guild);
    }


    @NotNull
    private MessageEmbed farewellEmbed(Guild guild, @NotNull User user, String language) {
        String title = localeRepository.getValueByLanguageAndLocaleNameAndGuild(language, "farewell_" + TYPE + "_title", guild);
        String fieldName = localeRepository.getValueByLanguageAndLocaleNameAndGuild(language, "farewell_" + TYPE + "_fieldName", guild);
        String fieldValue = localeRepository.getValueByLanguageAndLocaleNameAndGuild(language, "farewell_" + TYPE + "_fieldValue", guild);
        String footerText = localeRepository.getValueByLanguageAndLocaleNameAndGuild(language, "farewell_" + TYPE + "_footerText", guild);
        String img = user.getEffectiveAvatarUrl();

        EmbedBuilder eb = new EmbedBuilder();
        if (title != null) {
            eb.setTitle(title);
        }
        if (fieldValue != null || fieldName != null) {
            if (fieldName == null) {
                fieldName = " ";
            }
            if (fieldValue == null) {
                fieldValue = " ";
            }
            eb.addField(fieldName, fieldValue, true);
        }
        eb.setColor(Color.BLUE);
        eb.setThumbnail(img);
        eb.setTimestamp(OffsetDateTime.now());
        if (footerText != null) {
            eb.setFooter(footerText, guild.getIconUrl());
        }
        return eb.build();
    }

}