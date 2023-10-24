package horse.boo.bot.database.repository;

import horse.boo.bot.database.table.LocalesTable;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocaleRepository extends JpaRepository<LocalesTable, Long> {

    @Transactional
    @Modifying
    void deleteById(@NotNull Long id);

    boolean existsById(@NotNull Long id);

    Boolean existsByGuildId(Long guildId);

    LocalesTable findByGuildId(Long guildId);
    LocalesTable findByGuildIdAndLocaleName(Long guildId, String localeName);

    List<LocalesTable> findAllByGuildId(Long guildId);

    List<LocalesTable> getLocalesTableByGuildId(Long guildId);

    LocalesTable getLocalesTableByLocaleName(String localeName);

    LocalesTable getLocalesTableByLocaleNameAndGuildId(String localeName, long guildId);


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
