package horse.boo.bot.setup.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


public class GuildConfigService {

    private static final ObjectMapper mapper = new ObjectMapper();

    public GuildConfigService() {

    }

    public GuildConfig getActualGuildConfig(Guild guild) throws IOException {
        Message configMessage = getConfigMessage(guild);
        return mapper.readValue(configMessage.getContentRaw(), GuildConfig.class);
    }

    public void updateGuildConfigMessage(Guild guild, GuildConfig newGuildConfig) throws IOException {
        Message configMessage = getConfigMessage(guild);
        String actualConfigMessage = configMessage.getContentRaw();
        String newGuildConfigJson = mapper.writeValueAsString(newGuildConfig);
        if (!Objects.equals(actualConfigMessage, newGuildConfigJson))
            configMessage.editMessage(newGuildConfigJson).complete();
    }


    public Message getConfigMessage(Guild guild) {
        TextChannel channel = guild.getTextChannelsByName("system-channel-lyra", true).get(0);
        List<Message> messageHistory = MessageHistory.getHistoryFromBeginning(channel).complete()
                .getRetrievedHistory();
        return messageHistory.get(messageHistory.size() - 1);
    }

}
