package horse.boo.bot.database.repository;

import horse.boo.bot.database.table.LocalesTable;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface LocaleRepository extends JpaRepository<LocalesTable, Long> {

    @Transactional
    @Modifying
    void deleteByGuildId(@NotNull Long guild_id);

    Boolean existsByGuildId(Long guild_id);
    LocalesTable getByGuildIdAndLanguageAndLocaleTypeAndModeTypeAndFieldType(Long guildId, String language, String localeType, String modeType, String fieldType);
    List<LocalesTable> findAllByGuildId(Long guild_id);

    List<LocalesTable> getLocalesTableByGuildId(Long guild_id);

    @Query("SELECT locale_neww FROM LocalesTable locale_neww")
    List<LocalesTable> index();
}
