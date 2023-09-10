package horse.boo.bot.services.utils;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PingService extends ListenerAdapter {

    private final Logger logger = LoggerFactory.getLogger(PingService.class);

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Message msg = event.getMessage();
        if ((msg.getContentRaw().equals("/lyraping")) &&
                (msg.getAuthor().getIdLong() == 320332718921482241L)) {
            MessageChannel channel = event.getChannel();
            long time = System.currentTimeMillis();
            channel.sendMessage("Test complete")
                    .queue(response -> {
                        response.editMessageFormat("Ping: %d ms", System.currentTimeMillis() - time).queue();
                        logger.info("current ping: %d ms", System.currentTimeMillis() - time);
                    });
        } else if ((msg.getContentRaw().equals("/lyraping")) &&
                !(msg.getAuthor().getIdLong() == 320332718921482241L)) {
            msg.addReaction(Emoji.fromUnicode("❌")).complete();
        }
    }
}