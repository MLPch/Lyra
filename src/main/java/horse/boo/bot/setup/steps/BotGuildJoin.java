package horse.boo.bot.setup.steps;

import horse.boo.bot.setup.config.BotSystemChannelService;
import horse.boo.bot.setup.config.GuildConfigService;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

public class BotGuildJoin extends ListenerAdapter {
    BotSystemChannelService botSystemChannelService = new BotSystemChannelService();


    GuildConfigService gcs = new GuildConfigService();

    @SneakyThrows
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        var guild = event.getGuild();
//        var channel = botSystemChannelService.getSystemLyraChannel(guild);
        var defaultChannel = Objects.requireNonNull(event.getGuild().getDefaultChannel()).getIdLong();
        var cfg = gcs.getActualGuildConfig(guild);
        cfg.setBotReadyChannelId(defaultChannel);
        cfg.setJoinAndLeaveChannelId(defaultChannel);
        cfg.setBotId(event.getGuild().getSelfMember().getIdLong());
        gcs.updateGuildConfigMessage(guild, cfg);
        try {
            botSystemChannelService.getOrCreateAndGetConfigMessage(guild);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
