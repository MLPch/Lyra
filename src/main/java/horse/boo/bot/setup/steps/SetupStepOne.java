package horse.boo.bot.setup.steps;

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

    public SetupStepOne() {

    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        var guild = event.getGuild();
        var channel = guild.getTextChannelById(getSystemLyraChannel(guild).getId());
        var msgHistory = getMessagesFromSystemLyraChannel(guild);
        assert channel != null;

//        System.out.println("Config history in guild " + event.getGuild() + ":\n" + msgHistory);
//        System.out.println(msgHistory.get(msgHistory.size() - 1).getContentRaw());
        Path path = Paths.get("src/main/resources/config.json");
        System.out.println(path.getFileName());

        StringBuilder sb = new StringBuilder();
        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(s -> sb.append(s).append("\n"));

        } catch (IOException ex) {
            // Handle exception
        }
        String initialJsonConfig = sb.toString();

        if (msgHistory.isEmpty()) {
            channel.sendMessage(initialJsonConfig).queue();
            channel.sendMessage("--------\n--------\n--------\n").queue();
            System.out.println("INITIAL_SEND_JSON_CONFIG ---> \n" + initialJsonConfig);
        }

//        msgHistory.get(msgHistory.size() - 1).editMessage("").queue();
//        System.out.println(msgHistory.get(msgHistory.size() - 1).getContentRaw());

    }


    public Languages onButtonClick(ButtonInteractionEvent event) {
        Languages language = Languages.ENGLISH;
        if (event.getComponentId().equals("1")) {
            event.getMessage().delete().queue();
            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle("Выбран русский язык настройщика.")
                    .setColor(Color.red);
            event.getChannel()
                    .sendMessageEmbeds(eb.build())
                    .delay(4, TimeUnit.SECONDS)
                    .flatMap(Message::delete)
                    .queue();

            language = Languages.RUSSIAN;
        } else if (event.getComponentId().equals("2")) {
            event.getMessage().delete().queue();
            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle("The English language of the customizer is selected.")
                    .setColor(Color.blue);
            event.getChannel()
                    .sendMessageEmbeds(eb.build())
                    .delay(4, TimeUnit.SECONDS)
                    .flatMap(Message::delete)
                    .queue();
            language = Languages.ENGLISH;
        }
        return language;
    }


    public GuildChannel createSystemLyraChannel(Guild guild) {
        System.out.println("createSystemLyraChannel");
        return guild.createTextChannel(systemChannel)
                .addPermissionOverride(Objects.requireNonNull(guild.getMemberById("320332718921482241")), EnumSet.of(Permission.ADMINISTRATOR), null)
                .addPermissionOverride(Objects.requireNonNull(guild.getBotRole()), EnumSet.of(Permission.ADMINISTRATOR), null)
                .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                .timeout(5, TimeUnit.SECONDS)
                .complete();
    }


    private GuildChannel getSystemLyraChannel(Guild guild) {
        System.out.println("getSystemLyraChannel");
        return guild.getChannels()
                .stream()
                .filter(guildChannel -> guildChannel.getName().equals(systemChannel))
                .findFirst().orElseGet(() -> createSystemLyraChannel(guild));
    }


    private List<Message> getMessagesFromSystemLyraChannel(Guild guild) {
        var channel = getSystemLyraChannel(guild);
        System.out.println("getMessagesFromSystemLyraChannel");
        return MessageHistory.getHistoryFromBeginning((MessageChannel) channel).complete()
                .getRetrievedHistory();
    }


}
