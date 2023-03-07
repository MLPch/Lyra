package horse.boo.bot.events;

import horse.boo.bot.database.dao.LocaleDAO;
import horse.boo.bot.setup.config.GuildConfig;
import horse.boo.bot.setup.config.GuildConfigService;
import horse.boo.bot.setup.steps.Languages;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.time.OffsetDateTime;

public class MemberJoinEvent extends ListenerAdapter {
    @Autowired
    LocaleDAO localeDAO;
    @Autowired
    GuildConfigService guildConfigService;

    @SneakyThrows
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        GuildConfig guildConfig = guildConfigService.getActualGuildConfig(event.getGuild());
        Languages language = guildConfig.getBotLanguage();
        String stringAboveEmbed = localeDAO.getLocaleByName("greetings_default_stringAbove").getLocaleStringByLanguage(language);
        boolean stopped = true;
        String ping = event.getMember().getUser().getAsMention();
        TextChannel channel = event.getGuild().getTextChannelById(guildConfig.getJoinAndLeaveChannelId());

        while (stopped) {
            try {
                Thread.sleep(700);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            assert channel != null;
            channel.sendMessage(stringAboveEmbed + " " + ping + "!\n").setEmbeds(greetingEmbed(event)).queue();
            stopped = false;
        }
    }

    @SneakyThrows
    private MessageEmbed greetingEmbed(GuildMemberJoinEvent event) {


        GuildConfig guildConfig = guildConfigService.getActualGuildConfig(event.getGuild());

        Languages language = guildConfig.getBotLanguage();
        //TODO: Добавить функционал с кастомными наборами фраз через замену default
        String fieldName1 = localeDAO.getLocaleByName("greetings_default_fieldName").getLocaleStringByLanguage(language);
        String fieldValue1 = localeDAO.getLocaleByName("greetings_default_fieldValue").getLocaleStringByLanguage(language);
        String footerText1 = localeDAO.getLocaleByName("greetings_default_footerText").getLocaleStringByLanguage(language);


        String img = event.getMember().getUser().getEffectiveAvatarUrl();

        EmbedBuilder eb = new EmbedBuilder();
        eb.addField(fieldName1,
                fieldValue1, true);
        eb.setColor(Color.YELLOW);
        eb.setThumbnail(img);
        eb.setTimestamp(OffsetDateTime.now());
        eb.setFooter(footerText1, event.getGuild().getIconUrl());
        return eb.build();
    }

}