package horse.boo.bot.database.repository;

import horse.boo.bot.database.table.ConfigsTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfigRepository extends JpaRepository<ConfigsTable, Long> {

    Optional<ConfigsTable> findByGuildId(Long guild_id);

    ConfigsTable getConfigByGuildId(Long guild_id);

    @Query(value = "SELECT config from ConfigsTable config")
    List<ConfigsTable> findAllConfigs();
}
