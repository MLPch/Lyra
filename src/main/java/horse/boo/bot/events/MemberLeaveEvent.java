package horse.boo.bot.events;

import horse.boo.bot.database.dao.LocaleDAO;
import horse.boo.bot.setup.config.GuildConfig;
import horse.boo.bot.setup.config.GuildConfigService;
import horse.boo.bot.setup.steps.Languages;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;


import java.awt.*;
import java.time.OffsetDateTime;
import java.util.Objects;

public class MemberLeaveEvent extends ListenerAdapter {

    @Autowired
    LocaleDAO localeDAO;

    @Autowired
    GuildConfigService guildConfigService;

    @SneakyThrows
    @Override

    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        boolean stopped = true;
        User user = event.getUser();
        GuildConfig guildConfig = guildConfigService.getActualGuildConfig(event.getGuild());
        Languages language = guildConfig.getBotLanguage();
        TextChannel channel = event.getGuild().getTextChannelById(guildConfig.getJoinAndLeaveChannelId());
        String stringAboveEmbed = localeDAO.getLocaleByName("greetings_default_stringAbove").getLocaleStringByLanguage(language);


        while (stopped) {
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            assert channel != null;
            channel.sendMessage(stringAboveEmbed + user.getName() + "(" + user.getAsMention() + ")!").setEmbeds(farewellEmbed(event)).queue();
            stopped = false;
        }

    }

    @SneakyThrows
    private MessageEmbed farewellEmbed(GuildMemberRemoveEvent event) {


        GuildConfig guildConfig = guildConfigService.getActualGuildConfig(event.getGuild());

        Languages language = guildConfig.getBotLanguage();

        String fieldName1 = localeDAO.getLocaleByName("farewell_default_fieldName").getLocaleStringByLanguage(language);
        String fieldValue1 = localeDAO.getLocaleByName("farewell_default_fieldValue").getLocaleStringByLanguage(language);
        String footerText1 = localeDAO.getLocaleByName("farewell_default_footerText").getLocaleStringByLanguage(language);


        String img = Objects.requireNonNull(event.getMember()).getUser().getEffectiveAvatarUrl();

        EmbedBuilder eb = new EmbedBuilder();
        eb.addField(fieldName1,
                fieldValue1, true);
        eb.setColor(Color.BLUE);
        eb.setThumbnail(img);
        eb.setTimestamp(OffsetDateTime.now());
        eb.setFooter(footerText1, event.getGuild().getIconUrl());

        return eb.build();
    }
}