package horse.boo.bot.setup.config;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class GuildConfig {

    @JsonProperty("bot.id")
    private String botId;
    @JsonProperty("counts.offtopEmoteCount")
    private String offtopEmoteCount;

    @JsonProperty("emotes.offtopEmoteId")
    private String offtopEmoteId;

    @JsonProperty("channels.joinAndLeaveChannel.id")
    private String joinAndLeaveChannelId;

    @JsonProperty("channels.botReadyChannel.id")
    private String botReadyChannelId;

    @JsonProperty("times.offtopDeleteTime")
    private String offtopDeleteTime;

    @JsonProperty("functions.musicFunctional.enable")
    private boolean musicFunctionalEnable;

    @JsonProperty("functions.saveRoleFunctional.enable")
    private boolean saveRoleFunctionalEnable;

    @JsonProperty("functions.rollFunctional.enable")
    private boolean rollFunctionalEnable;
    @JsonProperty("functions.offtopFunctional.enable")
    private boolean offtopFunctionalEnable;


    @Override
    public String toString() {
        return "{" +
                "botId=" + botId +
                ", countsOfftopEmoteCount=" + offtopEmoteCount +
                ", joinAndLeaveChannelId=" + joinAndLeaveChannelId +
                ", botReadyChannelId=" + botReadyChannelId +
                ", offtopDeleteTime=" + offtopDeleteTime +
                ", offtopEmoteEmoteId=" + offtopEmoteId +
                ", musicFunctionalEnable=" + musicFunctionalEnable +
                ", saveRoleFunctionalEnable=" + saveRoleFunctionalEnable +
                ", rollFunctionalEnable=" + rollFunctionalEnable +
                ", offtopFunctionalEnable=" + offtopFunctionalEnable +
                '}';
    }
}
