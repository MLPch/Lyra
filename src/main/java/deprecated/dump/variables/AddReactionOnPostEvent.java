//package horse.boo.bot.events;
//
//import net.dv8tion.jda.api.EmbedBuilder;
//import net.dv8tion.jda.api.entities.Emote;
//import net.dv8tion.jda.api.entities.Message;
//import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
//import net.dv8tion.jda.api.hooks.ListenerAdapter;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.beans.factory.annotation.Value;
//
//import java.awt.*;
//import java.util.Objects;
//
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
//    @Value("${emote.NotificationUsers.unicode}")
//    private String NotificationUsersUnicode;
//
//    @Override
//    @Deprecated
//    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
//        if (!(event.getReactionEmote().isEmoji()) &&
//                (event.getReactionEmote().getEmote().equals(event.getGuild().getEmoteById(notificationOwner)))) {
//            Message msg = event.retrieveMessage().complete();
//            //Emote emoteGet = event.getReactionEmote().getEmote();
//            Emote emoteNotification = event.getGuild().getEmoteById(notificationUsers);
//            if ((msg.getChannel().getIdLong() == notificationChannel) &&
//                    !(Objects.requireNonNull(event.getMember()).getIdLong() == selfUser) &&
//                    !(msg.getAuthor().getIdLong() == selfUser) &&
//                    (msg.getAuthor().getIdLong() == event.getMember().getUser().getIdLong()) &&
//                    (!event.getReactionEmote().getEmote().equals(emoteNotification))) {
//
//
//                msg.addReaction(NotificationUsersUnicode).complete();
//
//                EmbedBuilder eb = new EmbedBuilder();
//                eb.setTitle("Подписка на событие активирована. " +
//                        "Не забудьте нажать на реакцию (" + NotificationOwnerFull + ") для активации рассылки.");
//                eb.setColor(Color.GREEN);
//
//                event.getMember().getUser().openPrivateChannel().queue(channel -> channel.sendMessage(eb.build()).queue());
//
//            }
//        }
//    }
//}