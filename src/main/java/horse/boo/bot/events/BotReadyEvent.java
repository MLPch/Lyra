package horse.boo.bot.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
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
    @Value("${emote.OftEmote.full}")
    private String oftEmoteFull;

    @Override
    @Deprecated
    public void onReady(net.dv8tion.jda.api.events.@NotNull ReadyEvent event) {

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Последнее обновление: 12.05.2022");
        eb.addField("Что нового:", "\n--Изменены приветствие и прощание." +
                        "\n--Кол-во необходимых для удаления реакций теперь равно четырём(4)." +
                        "\n--Баги превращены в фичи."
                , true);
        eb.setFooter(botName + "_Ver: 4.3.6");
        eb.setColor(Color.GREEN);
        Objects.requireNonNull(event.getJDA().getTextChannelById(botReadyChannel)).sendMessage(eb.build()).queue();
    }
}