package horse.boo.bot.database.repository;

import horse.boo.bot.database.table.IgnoreChannelTable;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IgnoreChannelRepository extends JpaRepository<IgnoreChannelTable, Long> {

    @Transactional
    @Modifying
    void deleteByChannelId(@NotNull Long channelId);

    Boolean existsByChannelId(Long channelId);

}
