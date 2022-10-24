package horse.boo.bot.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Nonnull;
import java.awt.*;
import java.time.OffsetDateTime;
import java.util.Objects;

public class MemberJoinEvent extends ListenerAdapter {

    @Value("${channel.JoinAndLeaveChannel}")
    private long joinAndLeaveChannel;
    @Value("${channel.VyborKomnatChannel}")
    private long vyborKomnatChannel;

    @Override

    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        boolean stopped = true;
        Member member = event.getMember();
        String img = member.getUser().getEffectiveAvatarUrl();
        String ping = member.getUser().getAsMention();
        TextChannel channel = event.getGuild().getTextChannelById(joinAndLeaveChannel);

        EmbedBuilder eb = new EmbedBuilder();
        eb.addField("Добро пожаловать в лучшее место!",
                "Пожалуйста, посети канал <#"
                        + vyborKomnatChannel
                        + "> для выбора"
                        + " контента который ты хочешь видеть или для создания собственного канала. По любым вопросам ты можешь обращаться к "
                        + Objects.requireNonNull(event.getGuild().getRoleById(736492288171048961L)).getAsMention() + "!", true);
        eb.setColor(Color.YELLOW);
        eb.setThumbnail(img);
        eb.setTimestamp(OffsetDateTime.now());
        eb.setFooter("Время появления на сервере", event.getGuild().getIconUrl());


        while (stopped) {
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            assert channel != null;
            channel.sendMessage("Проходи, дорогой " + ping + "!\n").setEmbeds(eb.build()).queue();
            stopped = false;
        }
    }
}