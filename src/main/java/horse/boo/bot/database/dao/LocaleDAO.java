package horse.boo.bot.database.dao;

import horse.boo.bot.database.models.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocaleDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LocaleDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Locale> index() {
        return jdbcTemplate.query("SELECT * FROM lyra_locale", new BeanPropertyRowMapper<>(Locale.class));
    }

    public Locale show(long id) {
        return jdbcTemplate.query("SELECT * FROM lyra_locale WHERE id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Locale.class))
                .stream().findAny().orElse(null);
    }

    public void save(Locale locale) {
        jdbcTemplate.update("INSERT INTO lyra_locale VALUES(default, ?, ?, ?, ?, ?)", locale.getLocale_name(), locale.getLocale_en(),
                locale.getLocale_ru(), locale.getLocale_ua(), locale.getLocale_cn());
    }

    public void update(long id, Locale updatedLocale) {
        jdbcTemplate.update("UPDATE lyra_locale SET locale_name=?, locale_en=?, locale_ru=?, locale_ua=?, locale_cn=? WHERE id=?", updatedLocale.getLocale_name(),
                updatedLocale.getLocale_en(), updatedLocale.getLocale_ru(), updatedLocale.getLocale_ua(), updatedLocale.getLocale_cn(), id);
    }

    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM lyra_locale WHERE id=?", id);
    }
}
