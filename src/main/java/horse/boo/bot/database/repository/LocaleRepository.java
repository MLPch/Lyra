package horse.boo.bot.database.repository;

import horse.boo.bot.database.table.LocalesTable;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocaleRepository extends JpaRepository<LocalesTable, Long> {

    @Transactional
    @Modifying
    void deleteByGuildId(@NotNull Long guild_id);
    Boolean existsByGuildId(Long guild_id);
    Optional<LocalesTable> findByGuildId(Long guild_id);
    List<LocalesTable> findAllByGuildId(Long guild_id);
    List<LocalesTable> findLocalesTableByGuildId(Long guild_id);
    List<LocalesTable> getLocalesTableByGuildId(Long guild_id);
    LocalesTable getLocalesTableByLocaleName(String localeName);
    LocalesTable findByGuildIdAndLocaleName(Long guild_id, String localeName);
    LocalesTable getLocalesTableByLocaleNameAndGuildId(String localeName, long guild_id);
    @Query("SELECT locale FROM LocalesTable locale")
    List<LocalesTable> index();

//    @Modifying
//    @Query("update LocalesTable lt set lt.guildId = :guild_id where lt.id = :id")
//    void updateGuildId(long id, LocalesTable locale);

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
