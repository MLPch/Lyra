package horse.boo.bot.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;

import java.awt.*;
import java.time.OffsetDateTime;

public class OfftopDeleteEvent extends ListenerAdapter {

    private boolean stopped = true;

    @Value("${emote.OftEmote}")
    private long oftEmote;
    @Value("${count.OftCount}")
    private int oftCount;
    @Value("${bot.selfUser}")
    private long selfUser;
    @Value("${time.OfftopDelete}")
    private short offtopDeleteEvent;

    @Override
    @Deprecated
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        event.getChannel().retrieveMessageById(event.getMessageId()).queue();
        Message msg = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
        User author = msg.getAuthor();

        for (MessageReaction react : msg.getReactions()) {
            if (!(react.getReactionEmote().isEmoji()) &&
                    react.getReactionEmote().getEmote().getIdLong() == oftEmote &&
                    (!(author.getIdLong() == selfUser)) &&
                    react.getCount() >= oftCount) {
                msg.delete().complete();

                EmbedBuilder eb = new EmbedBuilder();

                if (!author.isBot()) {
                    author.openPrivateChannel().complete();
                }
                String img = author.getEffectiveAvatarUrl();
                String ping = author.getAsMention();
                eb.setTitle("Your post was deleted as a result of everyone's disgust.");
                eb.setColor(Color.red);
                eb.setThumbnail(img);
                eb.setTimestamp(OffsetDateTime.now());
                eb.setFooter("Message cleaned up ", event.getGuild().getIconUrl());


                event.getChannel().sendMessage("Attention " + ping + "!\n").setEmbeds(eb.build()).complete();
                author.openPrivateChannel().queue(channel -> channel.sendMessage(eb.build()).complete());
            }
        }
    }

    @Override

    public void onMessageReceived(@NotNull MessageReceivedEvent evt) {

        if ((evt.getAuthor().getIdLong() == selfUser) &&
                (evt.getMessage().getContentRaw().contains("Attention")) &&
                (!evt.getMessage().getEmbeds().isEmpty())) {
            try {
                Thread.sleep(offtopDeleteEvent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            evt.getMessage().delete().complete();
        }
    }
}