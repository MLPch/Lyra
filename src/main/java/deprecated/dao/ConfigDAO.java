//package horse.boo.bot.database.dao;
//
//import horse.boo.bot.database.entity.Config;
//import org.jetbrains.annotations.NotNull;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Component
//@Repository
//public class ConfigDAO {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    public ConfigDAO(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    public List<Config> index() {
//        return jdbcTemplate.query("SELECT * FROM config", new BeanPropertyRowMapper<>(Config.class));
//    }
//
//    public Config show(long id) {
//        return jdbcTemplate.query("SELECT * FROM config WHERE id=?",
//                        new BeanPropertyRowMapper<>(Config.class),
//                        new Object[]{id})
//                .stream().findAny().orElse(null);
//    }
//
//    public void save(@NotNull Config config) {
//        jdbcTemplate.update("INSERT INTO config VALUES(default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
//                config.getGuild_id(),
//                config.getAdmin_channel_id(),
//                config.getBot_id(),
//                config.getBot_language(),
//                config.getOfftop_emote_count(),
//                config.getOfftop_emote_id(),
//                config.getWelcome_channel_id(),
//                config.getGoodbye_channel_id(),
//                config.getLog_channel_id(),
//                config.getBot_readiness_channel_id(),
//                config.getOfftop_delete_time_sec(),
//                config.isFunction_music_player(),
//                config.isFunction_role_saver(),
//                config.isFunction_dice_roller(),
//                config.isFunction_offlop_deleter());
//    }
//
//    public void update(long guild_id, @NotNull Config updatedConfig) {
//        jdbcTemplate.update("UPDATE config SET " +
//                        "admin_channel_id=?, " +
//                        "bot_id=?, " +
//                        "bot_language=?, " +
//                        "offtop_emote_count=?, " +
//                        "offtop_emote_id=?, " +
//                        "welcome_channel_id=?, " +
//                        "goodbye_channel_id=?, " +
//                        "log_channel_id=?, " +
//                        "bot_readiness_channel_id=?, " +
//                        "offtop_delete_time_sec=?, " +
//                        "function_music_player=?, " +
//                        "function_role_saver=?, " +
//                        "function_dice_roller=?, " +
//                        "function_offlop_deleter=?  " +
//                        "WHERE guild_id=?",
//                updatedConfig.getAdmin_channel_id(),
//                updatedConfig.getBot_id(),
//                updatedConfig.getBot_language(),
//                updatedConfig.getOfftop_emote_count(),
//                updatedConfig.getOfftop_emote_id(),
//                updatedConfig.getWelcome_channel_id(),
//                updatedConfig.getGoodbye_channel_id(),
//                updatedConfig.getLog_channel_id(),
//                updatedConfig.getBot_readiness_channel_id(),
//                updatedConfig.getOfftop_delete_time_sec(),
//                updatedConfig.isFunction_music_player(),
//                updatedConfig.isFunction_role_saver(),
//                updatedConfig.isFunction_dice_roller(),
//                updatedConfig.isFunction_offlop_deleter(),
//                guild_id);
//    }
//
//    public void delete(long guild_id) {
//        jdbcTemplate.update("DELETE FROM config WHERE guild_id=?", guild_id);
//    }
//
//    public List<Config> configList() {
//        List<Config> result = jdbcTemplate.query("SELECT * FROM config", new BeanPropertyRowMapper<>(Config.class));
//        System.out.println("I find new config! Look: " + result);
//        return result;
//    }
//
//    public Config getConfigByGuildId(long guild_id) {
//        return jdbcTemplate.query("SELECT * FROM config WHERE guild_id=?",
//                        new BeanPropertyRowMapper<>(Config.class),
//                        new Object[]{guild_id})
//                .stream().findAny().orElse(null);
//    }
//}