package horse.boo.bot.database.repository;

import horse.boo.bot.database.table.LocalesTable;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocaleRepository extends JpaRepository<LocalesTable, Long> {

    List<LocalesTable> findLocalesTableByGuildId(Long guild_id);
    LocalesTable findByGuildIdAndLocaleName(Long guild_id, String localeName);
    LocalesTable getLocalesTableByLocaleNameAndGuildId(String localeName, long guild_id);

    /**
     * @param language   - Язык искомого значения
     * @param guild      - Гильдия для которой происходит поиск значения
     * @param localeName - Название или тип значения
     * @return - Строка с заранее записанным в БД значением соответствующая параметру language.
     */
    default String getValueByLanguageAndLocaleNameAndGuild(@NotNull String language, String localeName, @NotNull Guild guild) {
        LocalesTable lt = getLocalesTableByLocaleNameAndGuildId(localeName, guild.getIdLong());
        return switch (language) {
            case "english" -> lt.getLocaleEN();
            case "russian" -> lt.getLocaleRU();
            case "ukraine" -> lt.getLocaleUA();
            case "china" -> lt.getLocaleCN();
            default -> "";
        };
    }
}
