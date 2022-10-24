package horse.boo.bot.setup;

import horse.boo.bot.setup.config.BotSystemChannelService;
import horse.boo.bot.setup.config.GuildConfigService;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class InitialSetupEvent extends ListenerAdapter {
    GuildConfigService gcs = new GuildConfigService();

    public InitialSetupEvent() {
    }

    @SneakyThrows
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        //TODO: Вынести сюда все шаги по настройке.
        Guild guild = event.getGuild();
        Message msg = event.getMessage();
        if (msg.getContentRaw().equals("/lyrastart")) {
            System.out.println("lyrastart");
            var cfg = gcs.getActualGuildConfig(guild);
            cfg.setBotId("cx235r2xc3x");
            gcs.updateGuildConfigMessage(guild, cfg);

        }
    }
}
