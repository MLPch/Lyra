package horse.boo.bot.services;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class WednesdayPostingService extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Message msg = event.getMessage();
        List<String> wednesdayWords = new ArrayList<>();
        wednesdayWords.add("среда");
        wednesdayWords.add("среду");
        wednesdayWords.add("среде");
        wednesdayWords.add("средам");
        wednesdayWords.add("среды");
        wednesdayWords.add("после вторника");
        wednesdayWords.add("перед четвергом");
        wednesdayWords.add("sreda");
        wednesdayWords.add("cpeda");
        wednesdayWords.add("chtlf");
        wednesdayWords.add("wednesday");
        wednesdayWords.add("вэнсдэй");
        wednesdayWords.add("вэндсдэй");
        wednesdayWords.add("уэнздей");
        wednesdayWords.stream().filter(
                word -> (msg.getContentRaw().toLowerCase().contains(word))
                        && !(msg.getAuthor().isBot())).forEach(word -> event.getMessage().addReaction(
                Objects.requireNonNull(event.getJDA().getEmojiById(1181430362786770984L))).queue());
    }
}
