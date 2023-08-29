package horse.boo.bot.events;

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
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Component
public class MemberLeaveService extends ListenerAdapter {
    private final Logger logger = LoggerFactory.getLogger(BotReadyService.class);
    private final ConfigRepository configRepository;
    private final LocaleRepository localeRepository;

    public String type = "default";

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


        while (stopped) {
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            guild.getTextChannelById(config.getGoodbyeChannelId()).sendMessage(localeRepository.getValueByLanguageAndLocaleNameAndGuild(language, "farewell_" + type + "_stringAbove", guild)
                    + " " + pingUser + " (" + user.getEffectiveName() + ")!\n").setEmbeds(farewellEmbed(guild, user, language)).queue();
            stopped = false;
        }
        guild.getTextChannelById(config.getLogChannelId()).sendMessage("**__The user has left the guild:__**" +
                "\nName: " + user.getName() +
                "\nID: " + user.getId() +
                "\nMention: " + user.getAsMention() +
                "\nAvatar: " + user.getAvatarUrl() +
                "\nDate: " + OffsetDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))).complete();
        logger.info("The user has left the guild: " + user);
    }

    private MessageEmbed farewellEmbed(Guild guild, User user, String language) {
        String fieldName = localeRepository.getValueByLanguageAndLocaleNameAndGuild(language, "farewell_" + type + "_fieldName", guild);
        String fieldValue = localeRepository.getValueByLanguageAndLocaleNameAndGuild(language, "farewell_" + type + "_fieldValue", guild);
        String footerText = localeRepository.getValueByLanguageAndLocaleNameAndGuild(language, "farewell_" + type + "_footerText", guild);

        String img = user.getEffectiveAvatarUrl();

        return new EmbedBuilder().addField(fieldName, fieldValue, true).setColor(Color.BLUE).setThumbnail(img).setTimestamp(OffsetDateTime.now()).setFooter(footerText, guild.getIconUrl()).build();
    }

}