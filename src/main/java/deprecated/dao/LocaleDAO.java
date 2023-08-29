//package horse.boo.bot.database.dao;
//
//import horse.boo.bot.database.entity.Locale;
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
//public class LocaleDAO {
//    private final JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    public LocaleDAO(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    public List<Locale> index() {
//        return jdbcTemplate.query("SELECT * FROM locale",
//                new BeanPropertyRowMapper<>(Locale.class));
//    }
//
//    public Locale show(long id) {
//        return jdbcTemplate.query("SELECT * FROM locale WHERE id=?",
//                        new BeanPropertyRowMapper<>(Locale.class),
//                        new Object[]{id})
//                .stream().findAny().orElse(null);
//    }
//
//    public void save(@NotNull Locale locale) {
//        jdbcTemplate.update("INSERT INTO locale VALUES(default, ?, ?, ?, ?, ?, ?)",
//                locale.getConfig_id(),
//                locale.getLocale_name(),
//                locale.getLocale_en(),
//                locale.getLocale_ru(),
//                locale.getLocale_ua(),
//                locale.getLocale_cn());
//    }
//
//    public void update(long id, @NotNull Locale updatedLocale) {
//        jdbcTemplate.update("UPDATE locale SET locale_name=?, locale_en=?, locale_ru=?, locale_ua=?, locale_cn=? WHERE id=?",
//                updatedLocale.getLocale_name(),
//                updatedLocale.getLocale_en(),
//                updatedLocale.getLocale_ru(),
//                updatedLocale.getLocale_ua(),
//                updatedLocale.getLocale_cn(),
//                id);
//    }
//
//    public void delete(long id) {
//        jdbcTemplate.update("DELETE FROM locale WHERE id=?",
//                id);
//    }
//
//    public List<Locale> localeList() {
//        List<Locale> result = jdbcTemplate.query(
//                "SELECT * FROM locale", new BeanPropertyRowMapper<>(Locale.class));
//        System.out.println("I find new locale! Look: " + result);
//        return result;
//    }
//
//    public Locale getLocaleByName(String locale_name) {
//        return jdbcTemplate.query("SELECT * FROM locale WHERE locale_name=?",
//                        new BeanPropertyRowMapper<>(Locale.class),
//                        new Object[]{locale_name})
//                .stream().findAny().orElse(null);
//    }
//}
