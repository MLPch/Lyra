//package horse.boo.bot.setup.config;
//
//import horse.boo.bot.database.dao.ConfigDAO;
//
//import horse.boo.bot.database.entity.Config;
//import horse.boo.bot.enums.Languages;
//import net.dv8tion.jda.api.entities.Guild;
//import org.jetbrains.annotations.NotNull;
//
//import java.io.IOException;
//import java.util.Objects;
//
//
//public class ConfigService {
//
//    ConfigDAO configDAO;
//
//    public ConfigService(ConfigDAO configDAO) {
//        this.configDAO = configDAO;
//    }
//
//    private Config getInitialGuildConfig(Guild guild){
//
//        var defaultChannel = Objects.requireNonNull(guild.getDefaultChannel()).getIdLong();
//
//
//         Config config = new Config.Builder(guild.getIdLong())
//                .admin_channel_id(0000000000000)
//                .bot_id(guild.getSelfMember().getIdLong())
//                .bot_language(Languages.ENGLISH.getLanguage())
//                .offtop_emote_count(999)
//                .offtop_emote_id(0000000000000)
//                .welcome_channel_id(defaultChannel)
//                .goodbye_channel_id(defaultChannel)
//                .log_channel_id(0000000000000)
//                .bot_readiness_channel_id(defaultChannel)
//                .offtop_delete_time_sec(15)
//                .function_music_player(false)
//                .function_role_saver(false)
//                .function_dice_roller(false)
//                .function_offlop_deleter(false)
//                .build();
//        return config;
//    }
//
//    public void setInitialGuildConfig(Guild guild) throws IOException {
//        configDAO.save(getInitialGuildConfig(guild));
//    }
//
//    public Config getGuildConfig(Guild guild) {
//        return configDAO.getConfigByGuildId(guild.getIdLong());
//    }
//
//    public void setGuildConfig(Config config) {
//        configDAO.save(config);
//    }
//
//    public void editGuildConfig(long guildId, @NotNull Config updatedConfig) {
//        configDAO.update(guildId, updatedConfig);
//    }
//
//
//    public void deleteGuildConfig(Guild guild) {
//        configDAO.delete(guild.getIdLong());
//    }
//}
