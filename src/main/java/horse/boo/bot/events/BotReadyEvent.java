package horse.boo.bot.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class BotReadyEvent extends ListenerAdapter {

    @Value("${channel.BotReadyChannel}")
    private String botReadyChannel;

    @Override
    public void onReady(net.dv8tion.jda.api.events.@NotNull ReadyEvent event) {
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model;
        try {
            model = reader.read(new FileReader("pom.xml"));
        } catch (IOException | XmlPullParserException e) {
            throw new RuntimeException(e);
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Последнее обновление: 12.05.2022");
        eb.addField("Что нового:", "\n--Изменены приветствие и прощание." +
                        "\n--Кол-во необходимых для удаления реакций теперь равно четырём(4)." +
                        "\n--Баги превращены в фичи."
                , true);
        eb.setFooter(model.getName() + "_Ver: " + model.getVersion());
        eb.setColor(Color.GREEN);
        Objects.requireNonNull(event.getJDA().getTextChannelById(botReadyChannel)).sendMessageEmbeds(eb.build()).queue();
    }
}