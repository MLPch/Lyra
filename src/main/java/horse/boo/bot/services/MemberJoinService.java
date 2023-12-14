package horse.boo.bot.services;

import horse.boo.bot.database.enums.FieldType;
import horse.boo.bot.database.enums.Languages;
import horse.boo.bot.database.repository.ConfigRepository;
import horse.boo.bot.database.repository.LocaleRepository;
import horse.boo.bot.database.table.ConfigsTable;
import horse.boo.bot.database.table.LocalesTable;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static horse.boo.bot.DiscordClient.TYPE;
import static horse.boo.bot.database.enums.FieldType.*;
import static horse.boo.bot.database.enums.LocaleType.GREETINGS;

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
        boolean stopped = true;
        String pingUser = user.getAsMention();
        String stringAbove = getEmbedValueFromDB(guild, EMBED_STRING_ABOVE);
        while (stopped) {
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (stringAbove != null) {
                guild.getTextChannelById(config.getWelcomeChannelId()).sendMessage(stringAbove +
                                                                                   " " + pingUser + " (" + user.getEffectiveName() + ")!\n").setEmbeds(greetingEmbed(guild, user)).queue();
            } else {
                guild.getTextChannelById(config.getWelcomeChannelId()).sendMessage(
                        pingUser + " (" + user.getEffectiveName() + ")").setEmbeds(greetingEmbed(guild, user)).queue();
            }

            stopped = false;
        }
        config.sendInLogChannel(guild, "**__A new user has joined:__**" +
                                       "\nName: " + user.getName() +
                                       "\nID: " + user.getId() +
                                       "\nMention: " + user.getAsMention() +
                                       "\nDate of registration: " + user.getTimeCreated().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").withZone(ZoneOffset.UTC)) +
                                       "\nDate of the event: " + OffsetDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").withZone(ZoneOffset.UTC)) +
                                       "\nAvatar: " + user.getAvatarUrl());
        logger.info("A new user has joined: " + user + " in " + guild);
    }


    @NotNull
    private MessageEmbed greetingEmbed(Guild guild, @NotNull User user) {
        //TODO: Добавить функционал с кастомными наборами фраз через замену default
        String title = getEmbedValueFromDB(guild, EMBED_TITLE);
        String fieldName = getEmbedValueFromDB(guild, EMBED_FIELD_NAME);
        String fieldValue = getEmbedValueFromDB(guild, EMBED_FIELD_VALUE);
        String footerText = getEmbedValueFromDB(guild, EMBED_FOOTER_TEXT);
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

    private String getEmbedValueFromDB(@NotNull Guild guild, @NotNull FieldType fieldType) {
        ConfigsTable config = configRepository.getConfigByGuildId(guild.getIdLong());
        return localeRepository.getByGuildIdAndLanguageAndLocaleTypeAndModeTypeAndFieldType(
                guild.getIdLong(),
                config.getBotLanguage(),
                GREETINGS.name(),
                TYPE,
                fieldType.getFieldType()).getValue();
    }

    public List<LocalesTable> greetingInitLocalesTable(Guild guild, String mode) {
        List<LocalesTable> localesTableList = new ArrayList<>();
        for (FieldType fieldT : FieldType.values()) {
            switch (fieldT) {
                case EMBED_STRING_ABOVE -> {
                    for (Languages language : Languages.values()) {
                        LocalesTable initGreetingLocale = getLocalesNewTable(guild, mode, fieldT, language);
                        switch (language) {
                            case ENGLISH, CHINESE, UKRAINE, RUSSIAN -> {
                                initGreetingLocale.setValue(" ");
                                localesTableList.add(initGreetingLocale);
                            }
                        }
                    }
                }
                case EMBED_TITLE -> {
                    for (Languages language : Languages.values()) {
                        LocalesTable initGreetingLocale = getLocalesNewTable(guild, mode, fieldT, language);
                        switch (language) {
                            case ENGLISH -> {
                                initGreetingLocale.setValue("Welcome!");
                                localesTableList.add(initGreetingLocale);
                            }
                            case RUSSIAN -> {
                                initGreetingLocale.setValue("Добро пожаловать!");
                                localesTableList.add(initGreetingLocale);
                            }
                            case UKRAINE -> {
                                initGreetingLocale.setValue(" ");
                                localesTableList.add(initGreetingLocale);
                            }
                            case CHINESE -> {
                                initGreetingLocale.setValue("欢迎！");
                                localesTableList.add(initGreetingLocale);
                            }
                        }
                    }
                }
                case EMBED_FIELD_NAME -> {
                    for (Languages language : Languages.values()) {
                        LocalesTable initGreetingLocale = getLocalesNewTable(guild, mode, fieldT, language);
                        switch (language) {
                            case ENGLISH -> {
                                initGreetingLocale.setValue("I hope you like it here!");
                                localesTableList.add(initGreetingLocale);
                            }
                            case RUSSIAN -> {
                                initGreetingLocale.setValue("Надеюсь тебе тут понравится!");
                                localesTableList.add(initGreetingLocale);
                            }
                            case UKRAINE -> {
                                initGreetingLocale.setValue("Сподіваюся тобі тут сподобається!");
                                localesTableList.add(initGreetingLocale);
                            }
                            case CHINESE -> {
                                initGreetingLocale.setValue("我希望你喜欢这里！");
                                localesTableList.add(initGreetingLocale);
                            }
                        }
                    }
                }
                case EMBED_FIELD_VALUE -> {
                    for (Languages language : Languages.values()) {
                        LocalesTable initGreetingLocale = getLocalesNewTable(guild, mode, fieldT, language);
                        switch (language) {
                            case ENGLISH -> {
                                initGreetingLocale.setValue(
                                        "Everything that is not allowed is prohibited on the server, " +
                                        "but everything that is not prohibited is allowed");
                                localesTableList.add(initGreetingLocale);
                            }
                            case RUSSIAN -> {
                                initGreetingLocale.setValue("На сервере запрещено всё что не разрешено, " +
                                                            "но разрешено всё что не запрещено");
                                localesTableList.add(initGreetingLocale);
                            }
                            case UKRAINE -> {
                                initGreetingLocale.setValue("На сервері заборонено все що не дозволено, " +
                                                            "але дозволено все що не заборонено");
                                localesTableList.add(initGreetingLocale);
                            }
                            case CHINESE -> {
                                initGreetingLocale.setValue("在服务器上禁止所有不允许的内容，但允许所有不允许的内容");
                                localesTableList.add(initGreetingLocale);
                            }
                        }
                    }
                }
                case EMBED_FOOTER_TEXT -> {
                    for (Languages language : Languages.values()) {
                        LocalesTable initGreetingLocale = getLocalesNewTable(guild, mode, fieldT, language);
                        switch (language) {
                            case ENGLISH -> {
                                initGreetingLocale.setValue("Time of appearance on the server");
                                localesTableList.add(initGreetingLocale);
                            }
                            case RUSSIAN -> {
                                initGreetingLocale.setValue("Время появления на сервере");
                                localesTableList.add(initGreetingLocale);
                            }
                            case UKRAINE -> {
                                initGreetingLocale.setValue("Час появи на сервері");
                                localesTableList.add(initGreetingLocale);
                            }
                            case CHINESE -> {
                                initGreetingLocale.setValue("在服务器上出现的时间");
                                localesTableList.add(initGreetingLocale);
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
        LocalesTable initGreetingLocale = new LocalesTable();
        initGreetingLocale.setGuildName(guild.getName());
        initGreetingLocale.setGuildId(guild.getIdLong());
        initGreetingLocale.setModeType(mode);
        initGreetingLocale.setLocaleType(GREETINGS.name());
        initGreetingLocale.setFieldType(fieldT.getFieldType());
        initGreetingLocale.setLanguage(language.getLanguage());
        return initGreetingLocale;
    }

}