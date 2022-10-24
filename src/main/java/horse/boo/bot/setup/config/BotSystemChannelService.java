package horse.boo.bot.setup.config;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class BotSystemChannelService {

    public String getOrCreateAndGetConfigMessage(Guild guild) {
        TextChannel channel = guild.getTextChannelsByName("system-channel-lyra", true).get(0);
        String defaultTextChannelId = guild.getDefaultChannel().getId();
        String defaultTextChannelName = guild.getDefaultChannel().getName();
        var msgHistory = channel.getHistory().getRetrievedHistory();
        Path path = Paths.get("src/main/resources/config.json");
        System.out.println(path.getFileName());

        StringBuilder sb = new StringBuilder();
        try (Stream<String> stream = Files.lines(path)) {
            stream.forEach(s -> sb.append(s).append("\n"));

        } catch (IOException ex) {
            // Handle exception
        }
        String initialJsonConfig = sb.toString();
        String finalConfigMessage = null;
        if (msgHistory.isEmpty()) {
            channel.sendMessage(initialJsonConfig).queue();
            channel.sendMessage("--------\n--------\n--------\n").queue();
            System.out.println("INITIAL_SEND_JSON_CONFIG ---> \n" + initialJsonConfig);

            GuildConfig config = GuildConfig
                    .builder()
                    .botId(guild
                            .getSelfMember()
                            .getId())
                    .botName(guild
                            .getSelfMember()
                            .getEffectiveName())
                    .countsOfftopEmoteCount("10")
                    .notificationChannelId(defaultTextChannelId)
                    .notificationChannelName(defaultTextChannelName)
                    .joinAndLeaveChannelId(defaultTextChannelId)
                    .joinAndLeaveChannelName(defaultTextChannelName)
                    .botReadyChannelId(defaultTextChannelId)
                    .botReadyChannelName(defaultTextChannelName)
                    .offtopDeleteTime("10000")
                    .musicFunctionalEnable(true)
                    .rollFunctionalEnable(true)
                    .saveRoleFunctionalEnable(true)
                    .build();

            finalConfigMessage = msgHistory.get(0).getContentRaw();
        } else {
            finalConfigMessage = msgHistory.get(0).getContentRaw();
        }
        return finalConfigMessage;
    }
}
