package horse.boo.bot.setup.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;

import java.io.IOException;
import java.util.Objects;


public class GuildConfigService {

    private static final ObjectMapper mapper = new ObjectMapper();

    public GuildConfigService() {

    }

    public GuildConfig getActualGuildConfig(Guild guild) throws IOException {
        return mapper.readValue(getConfigMessage(guild).getContentRaw(), GuildConfig.class);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void updateGuildConfigMessage(Guild guild, GuildConfig newGuildConfig) throws IOException {
        Message configMessage = getConfigMessage(guild);
        String actualConfigMessage = configMessage.getContentRaw();
        String newGuildConfigJson = mapper.writeValueAsString(newGuildConfig);
        if (!Objects.equals(actualConfigMessage, newGuildConfigJson))
            configMessage.editMessage(newGuildConfigJson).complete();
    }


    private Message getConfigMessage(Guild guild) throws IOException {
        BotSystemChannelService bscs = new BotSystemChannelService();
        return bscs.getOrCreateAndGetConfigMessage(guild);
    }

}
