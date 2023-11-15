package horse.boo.bot.database.table;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ignore_channel")
public class IgnoreChannelTable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "guild_id")
    private Long guildId;

    @Column(name = "channel_id")
    private Long channelId;

    @Column(name = "create_rule_date")
    @CreationTimestamp
    private LocalDateTime createRuleTimestamp;

    public IgnoreChannelTable() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getGuildId() {
        return guildId;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public LocalDateTime getCreateRuleTimestamp() {
        return createRuleTimestamp;
    }

    public void setCreateRuleTimestamp(LocalDateTime createRuleTimestamp) {
        this.createRuleTimestamp = createRuleTimestamp;
    }

    @Override
    public String toString() {
        return "IgnoreChannelTable{" +
                "id=" + id +
                ", guildId=" + guildId +
                ", channelId=" + channelId +
                ", createRuleTimestamp=" + createRuleTimestamp +
                '}';
    }
}
