package horse.boo.bot.events;

import horse.boo.bot.setup.config.GuildConfig;
import horse.boo.bot.setup.config.GuildConfigService;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class BotReadyEvent extends ListenerAdapter {
    @Autowired
    GuildConfigService guildConfigService;

    @SneakyThrows
    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        GuildConfig guildConfig = guildConfigService.getActualGuildConfig(event.getGuild());
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
                "\n--Баги превращены в фичи.", true);
        eb.setFooter(model.getName() + "_Ver: " + model.getVersion());
        eb.setColor(Color.GREEN);
        Objects.requireNonNull(event.getJDA().getTextChannelById(guildConfig.getBotReadyChannelId())).sendMessageEmbeds(eb.build()).queue();
    }
}