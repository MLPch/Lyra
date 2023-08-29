//package horse.boo.bot.setup.config;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import net.dv8tion.jda.api.entities.Guild;
//import net.dv8tion.jda.api.entities.Message;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Objects;
//
//@Component
//public class GuildLocaleConfigService {
//
//    private static final ObjectMapper mapper = new ObjectMapper();
//
//    public GuildLocaleConfigService() {
//
//    }
//
//    /**
//     * @param guild guild
//     * @return GuildLocaleService
//     * @throws IOException mhm
//     */
//    public GuildLocaleService getActualGuildLocaleConfig(Guild guild) throws IOException {
//        return mapper.readValue(getLocaleConfigMessage(guild).getContentRaw(), GuildLocaleService.class);
//    }
//
//    /**
//     * @param guild
//     * @param newGuildLocaleService
//     * @throws IOException
//     */
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    public void updateGuildLocaleConfigMessage(Guild guild, GuildLocaleService newGuildLocaleService) throws IOException {
//        Message configMessage = getLocaleConfigMessage(guild);
//        String actualConfigMessage = configMessage.getContentRaw();
//        String newGuildConfigJson = mapper.writeValueAsString(newGuildLocaleService);
//        if (!Objects.equals(actualConfigMessage, newGuildConfigJson))
//            configMessage.editMessage(newGuildConfigJson).complete();
//    }
//
//
//    /**
//     * @param guild
//     * @return
//     * @throws IOException
//     */
//
//
//}
