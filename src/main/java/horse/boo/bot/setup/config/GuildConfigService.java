package horse.boo.bot.setup.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

@Component
public class GuildConfigService {

    private static final ObjectMapper mapper = new ObjectMapper();

    public GuildConfigService() {

    }

    /**
     * @param guild guild
     * @return GuildConfig
     * @throws IOException mhm
     */
    public GuildConfig getActualGuildConfig(Guild guild) throws IOException {
        return mapper.readValue(getConfigMessage(guild).getContentRaw(), GuildConfig.class);
    }

    /**
     * @param guild
     * @param newGuildConfig
     * @throws IOException
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void updateGuildConfigMessage(Guild guild, GuildConfig newGuildConfig) throws IOException {
        Message configMessage = getConfigMessage(guild);
        String actualConfigMessage = configMessage.getContentRaw();
        String newGuildConfigJson = mapper.writeValueAsString(newGuildConfig);
        if (!Objects.equals(actualConfigMessage, newGuildConfigJson))
            configMessage.editMessage(newGuildConfigJson).complete();
    }


    /**
     * @param guild
     * @return
     * @throws IOException
     */
    private Message getConfigMessage(Guild guild) throws IOException {
        BotSystemChannelService bscs = new BotSystemChannelService();
        return bscs.getOrCreateAndGetConfigMessage(guild);
    }

}
