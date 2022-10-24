package horse.boo.bot.setup.config;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class BotSystemChannelService {
    private final String systemChannel = "system-channel-lyra";

    public Message getOrCreateAndGetConfigMessage(Guild guild) throws IOException {
        List<Message> msgHistory = getMessagesFromSystemLyraChannel(guild);

        if (msgHistory.isEmpty()) {
            sendConfigInTextChannel((TextChannel) getSystemLyraChannel(guild));
            msgHistory = getMessagesFromSystemLyraChannel(guild);
        }
        return msgHistory.get(msgHistory.size() - 1);
    }

    private void sendConfigInTextChannel(@NotNull TextChannel channel) {
        channel.sendMessage(initialGuildConfig()).complete();
    }

    private @NotNull String initialGuildConfig() {
        Path path = Paths.get("src/main/resources/config.json");

        StringBuilder sb = new StringBuilder();

        try (Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8)) {
            stream.forEach(s -> sb.append(s).append("\n"));
        } catch (IOException ex) {
            // Handle exception
        }
        return sb.toString();
    }

    private TextChannel createSystemLyraChannel(@NotNull Guild guild) {
        return guild.createTextChannel(systemChannel)
                .addPermissionOverride(Objects.requireNonNull(guild.getMemberById("320332718921482241")), EnumSet.of(Permission.ADMINISTRATOR), null)
                .addPermissionOverride(Objects.requireNonNull(guild.getBotRole()), EnumSet.of(Permission.ADMINISTRATOR), null)
                .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                .timeout(5, TimeUnit.SECONDS)
                .complete();
    }


    public GuildChannel getSystemLyraChannel(@NotNull Guild guild) {
        return guild.getChannels()
                .stream()
                .filter(guildChannel -> guildChannel.getName().equals(systemChannel))
                .findFirst().orElseGet(() -> createSystemLyraChannel(guild));
    }


    public List<Message> getMessagesFromSystemLyraChannel(Guild guild) {
        var channel = getSystemLyraChannel(guild);
        return MessageHistory.getHistoryFromBeginning((MessageChannel) channel).complete()
                .getRetrievedHistory();
    }

}
