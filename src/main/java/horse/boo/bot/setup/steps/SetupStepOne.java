package horse.boo.bot.setup.steps;

import horse.boo.bot.setup.config.BotSystemChannelService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class SetupStepOne extends ListenerAdapter {
    private final String systemChannel = "system-channel-lyra";
    BotSystemChannelService botSystemChannelService = new BotSystemChannelService();
    public SetupStepOne() {

    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        var guild = event.getGuild();
        var channel = botSystemChannelService.getSystemLyraChannel(guild);
        var msgHistory = botSystemChannelService.getMessagesFromSystemLyraChannel(guild);
        try {
            botSystemChannelService.getOrCreateAndGetConfigMessage(event.getGuild());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


//    public Languages onButtonClick(ButtonInteractionEvent event) {
//        Languages language = Languages.ENGLISH;
//        if (event.getComponentId().equals("1")) {
//            event.getMessage().delete().queue();
//            EmbedBuilder eb = new EmbedBuilder()
//                    .setTitle("Выбран русский язык настройщика.")
//                    .setColor(Color.red);
//            event.getChannel()
//                    .sendMessageEmbeds(eb.build())
//                    .delay(4, TimeUnit.SECONDS)
//                    .flatMap(Message::delete)
//                    .queue();
//
//            language = Languages.RUSSIAN;
//        } else if (event.getComponentId().equals("2")) {
//            event.getMessage().delete().queue();
//            EmbedBuilder eb = new EmbedBuilder()
//                    .setTitle("The English language of the customizer is selected.")
//                    .setColor(Color.blue);
//            event.getChannel()
//                    .sendMessageEmbeds(eb.build())
//                    .delay(4, TimeUnit.SECONDS)
//                    .flatMap(Message::delete)
//                    .queue();
//            language = Languages.ENGLISH;
//        }
//        return language;
//    }



}
