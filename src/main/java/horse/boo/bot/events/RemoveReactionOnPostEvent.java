package horse.boo.bot.events;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class RemoveReactionOnPostEvent extends ListenerAdapter {

    @Value("${emote.NotificationUsers}")
    private long notificationUsers;
    @Value("${emote.NotificationOwner}")
    private long notificationOwner;
    @Value("${emote.OftEmote}")
    private long oftEmote;
    @Value("${channel.NotificationChannel}")
    private long notificationChannel;
    @Value("${bot.selfUser}")
    private long selfUser;

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        if (!(event.getReactionEmote().isEmoji())) {

            Message msg = event.retrieveMessage().complete();
            String contentMsg = msg.getContentRaw();
            List<Message.Attachment> attachmentsList = msg.getAttachments();

            boolean testUsingList = false;

            if (event.getReactionEmote().getEmote().equals(event.getGuild().getEmoteById(notificationOwner)) &&
                    !(event.getReactionEmote().getEmote().equals(event.getGuild().getEmoteById(oftEmote))) &&
                    !(event.getReactionEmote().isEmoji()) &&
                    !(msg.getAuthor().getIdLong() == selfUser) &&
                    (msg.getAuthor().getIdLong() == event.getUserIdLong()) &&
                    (msg.getChannel().getIdLong() == notificationChannel)) {

                for (MessageReaction react : msg.getReactions()) {
                    if (!(react.getReactionEmote().isEmoji()) &&
                            react.getReactionEmote().getIdLong() == notificationUsers) {
                        for (User nextUser : react.retrieveUsers().complete()) {
                            if (!(nextUser.getIdLong() == selfUser)) {
                                nextUser.openPrivateChannel().complete();
                                nextUser.openPrivateChannel().complete();
                                nextUser.openPrivateChannel().queue(channel -> channel.
                                        sendMessage("?????????? ?????????????????? ?? ?????????????????? ??????????????: " + "\n"
                                                + contentMsg).complete());
                                if (attachmentsList.size() != 0) {
                                    String contentImg = attachmentsList.get(0).getUrl();
                                    nextUser.openPrivateChannel().queue(channel ->
                                            channel.sendMessage(contentImg).complete());
                                }
                                react.clearReactions().complete();
                                testUsingList = true;
                                msg.addReaction("???").complete();
                            } else if (!testUsingList) {
                                react.clearReactions().complete();
                            }
                        }
                    }
                }
            }
        }
    }
}