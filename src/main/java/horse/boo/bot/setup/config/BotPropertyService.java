package horse.boo.bot.setup.config;

import horse.boo.bot.setup.steps.Languages;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;

public class BotPropertyService {
    @Autowired
    GuildConfigService guildConfigService;

    public Languages getLanguage(Guild guild) throws IOException {
        return guildConfigService.getActualGuildConfig(guild).getBotLanguage();
    }
}
