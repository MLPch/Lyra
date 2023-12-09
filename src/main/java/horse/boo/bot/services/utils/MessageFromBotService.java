package horse.boo.bot.services.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;


@Component
public class MessageFromBotService extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Message msg = event.getMessage();

        if ((msg.getContentRaw().contains("lyrasay")) && (msg.getAuthor().getIdLong() == 320332718921482241L)) {
            EmbedBuilder eb = new EmbedBuilder();
            String fieldValue = msg.getContentRaw().replace("lyrasay", "");
            if (msg.getContentRaw().length() > 7) {
                if (msg.getContentRaw().length() > 1024) {
                    fieldValue = msg.getContentRaw().substring(7, 1030);
                }
                eb.addField("", fieldValue, true);
            }
            if (msg.getAttachments().get(0) != null) {
                eb.setImage(msg.getAttachments().get(0).getUrl());
            }
            event.getChannel().sendMessageEmbeds(eb.build())
                    .queue();
        }
    }
}
