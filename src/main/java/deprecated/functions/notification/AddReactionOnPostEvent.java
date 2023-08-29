//package deprecated.functions.notification;
//
//import net.dv8tion.jda.api.EmbedBuilder;
//import net.dv8tion.jda.api.entities.Message;
//import net.dv8tion.jda.api.entities.emoji.Emoji;
//import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
//import net.dv8tion.jda.api.hooks.ListenerAdapter;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.beans.factory.annotation.Value;
//
//import java.awt.*;
//import java.util.Objects;
//
//@Deprecated
//public class AddReactionOnPostEvent extends ListenerAdapter {
//
//    @Value("${emote.NotificationUsers}")
//    private long notificationUsers;
//    @Value("${emote.NotificationOwner}")
//    private long notificationOwner;
//    @Value("${channel.NotificationChannel}")
//    private long notificationChannel;
//    @Value("${bot.selfUser}")
//    private long selfUser;
//    @Value("${emote.NotificationOwner.full}")
//    private String NotificationOwnerFull;
//    @Value("${emote.NotificationUsers}")
//    private long NotificationUsers;
//
//    @Override
//    @Deprecated
//    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
//        if (event.getEmoji().equals(event.getGuild().getEmojiById(notificationOwner))) {
//            Message msg = event.retrieveMessage().complete();
//            //Emote emoteGet = event.getReactionEmote().getEmote();
//            Emoji emoteNotification = event.getGuild().getEmojiById(notificationUsers);
//            if ((msg.getChannel().getIdLong() == notificationChannel) &&
//                    !(Objects.requireNonNull(event.getMember()).getIdLong() == selfUser) &&
//                    !(msg.getAuthor().getIdLong() == selfUser) &&
//                    (msg.getAuthor().getIdLong() == event.getMember().getUser().getIdLong()) &&
//                    (!event.getEmoji().equals(emoteNotification))) {
//
//
//                msg.addReaction(Emoji.fromCustom("dovolen", NotificationUsers, false)).complete();
//
//                EmbedBuilder eb = new EmbedBuilder();
//                eb.setTitle("Подписка на событие активирована. " +
//                        "Не забудьте нажать на реакцию (" + NotificationOwnerFull + ") для активации рассылки.");
//                eb.setColor(Color.GREEN);
//
//                event.getMember().getUser().openPrivateChannel().queue(channel -> channel.sendMessageEmbeds(eb.build()).queue());
//
//            }
//        }
//    }
//}