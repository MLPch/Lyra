package horse.boo.bot.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;

import java.awt.*;
import java.util.Objects;

public class BotReadyEvent extends ListenerAdapter {

    @Value("${channel.BotReadyChannel}")
    private String botReadyChannel;
    @Value("${bot.name}")
    private String botName;

    @Override
    @Deprecated
    public void onReady(@NotNull ReadyEvent event) {

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Last update: 02/21/2022");
        eb.addField("What's new:", "Glory to Their Majesties!" +
                        "\n--Added the ability to select roles." +
                        "\n--Bugs are turned into features, and features into bugs."
                , true);
        eb.setFooter(botName + "_Ver: 5.0.1");
        eb.setColor(Color.GREEN);
        Objects.requireNonNull(event.getJDA().getTextChannelById(botReadyChannel)).sendMessage(eb.build()).queue();
    }
}