package horse.boo.bot.services;

import horse.boo.bot.database.repository.ConfigRepository;
import horse.boo.bot.database.repository.LocaleRepository;
import horse.boo.bot.database.table.ConfigsTable;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
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
public class MemberJoinService extends ListenerAdapter {
    private final Logger logger = LoggerFactory.getLogger(MemberJoinService.class);
    private final ConfigRepository configRepository;
    private final LocaleRepository localeRepository;


    public MemberJoinService(ConfigRepository configRepository,
                             LocaleRepository localeRepository) {
        this.configRepository = configRepository;
        this.localeRepository = localeRepository;
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        User user = event.getUser();
        ConfigsTable config = configRepository.getConfigByGuildId(guild.getIdLong());
        String language = config.getBotLanguage();
        boolean stopped = true;
        String pingUser = user.getAsMention();
        String stringAbove = localeRepository.getValueByLanguageAndLocaleNameAndGuild(language, "greetings_" + TYPE + "_stringAbove", guild);
        while (stopped) {
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (stringAbove != null) {
                guild.getTextChannelById(config.getWelcomeChannelId()).sendMessage(stringAbove +
                        " " + pingUser + " (" + user.getEffectiveName() + ")!\n").setEmbeds(greetingEmbed(guild, user, language)).queue();
            } else {
                guild.getTextChannelById(config.getWelcomeChannelId()).sendMessage(
                        pingUser + " (" + user.getEffectiveName() + ")").setEmbeds(greetingEmbed(guild, user, language)).queue();
            }

            stopped = false;
        }
        config.sendInLogChannel(guild,"**__A new user has joined:__**" +
                "\nName: " + user.getName() +
                "\nID: " + user.getId() +
                "\nMention: " + user.getAsMention() +
                "\nDate of registration: " + user.getTimeCreated().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").withZone(ZoneOffset.UTC)) +
                "\nDate of the event: " + OffsetDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").withZone(ZoneOffset.UTC)) +
                "\nAvatar: " + user.getAvatarUrl());
        logger.info("A new user has joined: " + user + " in " + guild);
    }


    private MessageEmbed greetingEmbed(Guild guild, User user, String language) {
        //TODO: Добавить функционал с кастомными наборами фраз через замену default
        String title = localeRepository.getValueByLanguageAndLocaleNameAndGuild(language, "greetings_" + TYPE + "_title", guild);
        String fieldName = localeRepository.getValueByLanguageAndLocaleNameAndGuild(language, "greetings_" + TYPE + "_fieldName", guild);
        String fieldValue = localeRepository.getValueByLanguageAndLocaleNameAndGuild(language, "greetings_" + TYPE + "_fieldValue", guild);
        String footerText = localeRepository.getValueByLanguageAndLocaleNameAndGuild(language, "greetings_" + TYPE + "_footerText", guild);
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
        eb.setColor(Color.YELLOW);
        eb.setThumbnail(img);
        eb.setTimestamp(OffsetDateTime.now());
        if (footerText != null) {
            eb.setFooter(footerText, guild.getIconUrl());
        }
        return eb.build();
    }

    @Override
    public String toString() {
        return "MemberJoinService{" +
                "configRepository=" + configRepository.toString() +
                ", localeRepository=" + localeRepository.toString() +
                ", type='" + TYPE + '\'' +
                '}';
    }
}