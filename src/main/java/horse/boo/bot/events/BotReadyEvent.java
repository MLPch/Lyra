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
        eb.setTitle("Последнее обновление: 25.01.2022");
        eb.addField("Что нового:", "Добавлена команда\"/lyrahelp\": Выводит в ответ рамку с инструкцией " +
                        "по эксплуатации функции уведомлений." +
                        "\n--Добавлена команда\"/lyrahelpdetail\": Выводит в ответ рамку с инструкцией по эксплуатации" +
                        " функции уведомлений, но в этом случае для тех самых непонятливых." +
                        "\n--Удаляющие реакции изменены на (" + oftEmoteFull + ")." +
                        "\n--Список обновлений теперь не удаляется и постится в <#" + botReadyChannel + ">" +
                        "\n--Уведомления об удалении сообщения теперь удаляются через 15 секунд." +
                        "\n--Баги превращены в фичи."
                , true);
        eb.setFooter(botName + "_Ver: 4.2.4");
        eb.setColor(Color.GREEN);
        Objects.requireNonNull(event.getJDA().getTextChannelById(botReadyChannel)).sendMessage(eb.build()).queue();
    }
}