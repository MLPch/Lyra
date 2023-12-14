package deprecated.database.repository;

import deprecated.database.table.LocalesOldTable;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocaleOldRepository extends JpaRepository<LocalesOldTable, Long> {

    @Transactional
    @Modifying
    void deleteByGuildId(@NotNull Long guild_id);
    Boolean existsByGuildId(Long guild_id);
    Optional<LocalesOldTable> findByGuildId(Long guild_id);
    List<LocalesOldTable> findAllByGuildId(Long guild_id);
    List<LocalesOldTable> findLocalesTableByGuildId(Long guild_id);
    List<LocalesOldTable> getLocalesTableByGuildId(Long guild_id);
    LocalesOldTable getLocalesTableByLocaleName(String localeName);
    LocalesOldTable findByGuildIdAndLocaleName(Long guild_id, String localeName);
    LocalesOldTable getLocalesTableByLocaleNameAndGuildId(String localeName, long guild_id);
    @Query("SELECT locale FROM LocalesOldTable locale")
    List<LocalesOldTable> index();

    /**
     * @param language   - Язык искомого значения
     * @param guild      - Гильдия для которой происходит поиск значения
     * @param localeName - Название и тип значения
     * @return - Строка с заранее записанным в БД значением соответствующая параметру language.
     */
    default String getValueByLanguageAndLocaleNameAndGuild(@NotNull String language, String localeName, @NotNull Guild guild) {
        LocalesOldTable lt = getLocalesTableByLocaleNameAndGuildId(localeName, guild.getIdLong());
        return switch (language) {
            case "english" -> lt.getLocaleEN();
            case "russian" -> lt.getLocaleRU();
            case "ukraine" -> lt.getLocaleUA();
            case "china" -> lt.getLocaleCN();
            default -> "";
        };
    }
}
