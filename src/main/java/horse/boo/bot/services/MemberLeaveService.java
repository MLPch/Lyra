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
import java.util.ArrayList;
import java.util.List;

import static horse.boo.bot.DiscordClient.TYPE;
import static horse.boo.bot.database.enums.FieldType.*;
import static horse.boo.bot.database.enums.LocaleType.FAREWELL;

@Component
public class MemberLeaveService extends ListenerAdapter {
    private final Logger logger = LoggerFactory.getLogger(MemberLeaveService.class);
    private final ConfigRepository configRepository;
    private final LocaleRepository localeRepository;

    public MemberLeaveService(ConfigRepository configRepository,
                              LocaleRepository localeRepository) {
        this.configRepository = configRepository;
        this.localeRepository = localeRepository;
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
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
                guild.getTextChannelById(config.getGoodbyeChannelId()).sendMessage(stringAbove +
                        " " + pingUser + " (" + user.getEffectiveName() + ")!\n").setEmbeds(farewellEmbed(guild, user)).queue();
            } else {
                guild.getTextChannelById(config.getGoodbyeChannelId()).sendMessage(
                        pingUser + " (" + user.getEffectiveName() + ")").setEmbeds(farewellEmbed(guild, user)).queue();
            }

            stopped = false;
        }
        config.sendInLogChannel(guild,"**__The user has left the guild:__**" +
                "\nName: " + user.getName() +
                "\nID: " + user.getId() +
                "\nMention: " + user.getAsMention() +
                "\nAvatar: " + user.getAvatarUrl() +
                "\nDate: " + OffsetDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").withZone(ZoneOffset.UTC)));
        logger.info("The user has left the guild: " + user + " from " + guild);
    }


    @NotNull
    private MessageEmbed farewellEmbed(Guild guild, @NotNull User user) {
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
        eb.setColor(Color.BLUE);
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
                FAREWELL.name(),
                TYPE,
                fieldType.getFieldType()).getValue();
    }

    public java.util.List<LocalesTable> farewellInitLocalesTable(Guild guild, String mode) {
        List<LocalesTable> localesTableList = new ArrayList<>();
        for (FieldType fieldT : FieldType.values()) {
            switch (fieldT) {
                case EMBED_STRING_ABOVE -> {
                    for (Languages language : Languages.values()) {
                        LocalesTable defaultFarewellLocale = getLocalesNewTable(guild, mode, fieldT, language);
                        switch (language) {
                            case ENGLISH -> {
                                defaultFarewellLocale.setValue("We lost");
                                localesTableList.add(defaultFarewellLocale);
                            }
                            case RUSSIAN -> {
                                defaultFarewellLocale.setValue("Нас покинул");
                                localesTableList.add(defaultFarewellLocale);
                            }
                            case UKRAINE -> {
                                defaultFarewellLocale.setValue("Нас покинув");
                                localesTableList.add(defaultFarewellLocale);
                            }
                            case CHINESE -> {
                                defaultFarewellLocale.setValue("我们输了");
                                localesTableList.add(defaultFarewellLocale);
                            }
                        }
                    }
                }
                case EMBED_TITLE, EMBED_FIELD_VALUE -> {
                    for (Languages language : Languages.values()) {
                        LocalesTable defaultFarewellLocale = getLocalesNewTable(guild, mode, fieldT, language);
                        switch (language) {
                            case ENGLISH, RUSSIAN, UKRAINE, CHINESE -> {
                                defaultFarewellLocale.setValue(" ");
                                localesTableList.add(defaultFarewellLocale);
                            }
                        }
                    }
                }
                case EMBED_FIELD_NAME -> {
                    for (Languages language : Languages.values()) {
                        LocalesTable defaultFarewellLocale = getLocalesNewTable(guild, mode, fieldT, language);
                        switch (language) {
                            case ENGLISH -> {
                                defaultFarewellLocale.setValue("Well, left and left.");
                                localesTableList.add(defaultFarewellLocale);
                            }
                            case RUSSIAN -> {
                                defaultFarewellLocale.setValue("Ну ушёл и ушёл.");
                                localesTableList.add(defaultFarewellLocale);
                            }
                            case UKRAINE -> {
                                defaultFarewellLocale.setValue("Ну пішов і пішов.");
                                localesTableList.add(defaultFarewellLocale);
                            }
                            case CHINESE -> {
                                defaultFarewellLocale.setValue("他走了又走了。");
                                localesTableList.add(defaultFarewellLocale);
                            }
                        }
                    }
                }
                case EMBED_FOOTER_TEXT -> {
                    for (Languages language : Languages.values()) {
                        LocalesTable defaultFarewellLocale = getLocalesNewTable(guild, mode, fieldT, language);
                        switch (language) {
                            case ENGLISH -> {
                                defaultFarewellLocale.setValue("The door slammed:");
                                localesTableList.add(defaultFarewellLocale);
                            }
                            case RUSSIAN -> {
                                defaultFarewellLocale.setValue("Дверь хлопнула:");
                                localesTableList.add(defaultFarewellLocale);
                            }
                            case UKRAINE -> {
                                defaultFarewellLocale.setValue("Двері грюкнули:");
                                localesTableList.add(defaultFarewellLocale);
                            }
                            case CHINESE -> {
                                defaultFarewellLocale.setValue("门砰地关上了:");
                                localesTableList.add(defaultFarewellLocale);
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
        LocalesTable defaultFarewellLocale = new LocalesTable();
        defaultFarewellLocale.setGuildName(guild.getName());
        defaultFarewellLocale.setGuildId(guild.getIdLong());
        defaultFarewellLocale.setModeType(mode);
        defaultFarewellLocale.setLocaleType(FAREWELL.name());
        defaultFarewellLocale.setFieldType(fieldT.getFieldType());
        defaultFarewellLocale.setLanguage(language.getLanguage());
        return defaultFarewellLocale;
    }
}