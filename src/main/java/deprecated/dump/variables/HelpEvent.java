//package horse.boo.bot.events;
//
//import net.dv8tion.jda.api.EmbedBuilder;
//import net.dv8tion.jda.api.entities.Message;
//import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
//import net.dv8tion.jda.api.hooks.ListenerAdapter;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.beans.factory.annotation.Value;
//
//import java.awt.*;
//
//
//public class HelpEvent extends ListenerAdapter {
//
//    @Value("${emote.NotificationOwner.full}")
//    private String notificationOwnerFull;
//    @Value("${emote.NotificationUsers.full}")
//    private String notificationUsersFull;
//    @Value("${channel.NotificationChannel}")
//    private long notificationChannel;
//
//    @Override
//    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
//        Message msg = event.getMessage();
//        if (msg.getContentRaw().equals("/lyrahelp")) {
//
//            EmbedBuilder eb = new EmbedBuilder();
//            eb.setTitle("Инструкция:");
//            eb.addField("Для добавления и использования возможности уведомления на пост необходимо:",
//                    "1) Создать пост или быть автором поста в канале <#" + notificationChannel + "> на который" +
//                            " необходимо применить функцию.\n" +
//                            "2) Поставить реакцию (" + notificationOwnerFull + ") на свой пост.\n" +
//                            "3) После этого пользователи могут нажать на появившуюся реакцию (" + notificationUsersFull + ").\n" +
//                            "4) Нажать на реакцию (" + notificationOwnerFull + ") ещё раз.\n" +
//                            "5) После этого запускается цикл отправляющий текст сообщения и его вложения в ЛС каждому " +
//                            "пользователю нажавшему на реакцию (" + notificationUsersFull + ").\n" +
//                            "6) Если всё прошло хорошо - на пост поставится другая реакция с галочкой (✅)", true);
//            eb.setColor(Color.ORANGE);
//            msg.replyEmbeds(eb.build()).complete();
//
//        } else if (msg.getContentRaw().equals("/lyrahelpdetail")) {
//
//            EmbedBuilder eb = new EmbedBuilder();
//            eb.setTitle("Самая подробная инструкция которую только можно себе представить:");
//            eb.addField("Захотел ты короче сделать анонс и не срать эвриванами по 10 раз, ок да?",
//                    "1) Значится заходишь в канал <#" + notificationChannel + "> ага, ибо больше некуда, иначе" +
//                            " тебя закэнсилят и наплюют в суп.\n" +
//                            "2) Пишешь свою фигню которую хочешь протолкнуть, будь то зачитка, просмотр кинчика или" +
//                            " ещё какой ивент.\n" +
//                            "3) Отправляешь своё сообщение, это самый важный шаг.\n" +
//                            "4) Ставишь под свое чудесное сообщение РЕАКЦИЮ которая в скобочках (" + notificationOwnerFull + ").\n" +
//                            "5) Смотришь на своё сообщение на котором волшебным образом должна появится РЕАКЦИЯ из " +
//                            "скобочек (" + notificationUsersFull + ")\n" +
//                            "6) Если РЕАКЦИЯ появилась, то жди ровно до того момента, когда анонсированный тобою движ начнётся.\n" +
//                            "7) Как только тебе в голову стукнет мысль о том что пора начинать - жми на РЕАКЦИЮ из" +
//                            " скобочек (" + notificationOwnerFull + ").\n" +
//                            "8) На этом твоя работа закончена. Лира сама посмотрит список тех кто нажал на ("
//                            + notificationOwnerFull + ") и отправит им письма, не менее волшебные чем письма из Хогвартса.\n" +
//                            "9) Будь лапой.", true);
//            eb.setColor(Color.ORANGE);
//            msg.replyEmbeds(eb.build()).complete();
//        }
//    }
//}