package horse.boo.bot.setup.config;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import horse.boo.bot.setup.steps.Languages;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class GuildConfig {

    @JsonProperty("bot.id")
    private long botId;

    @JsonProperty("bot.language")
    private Languages botLanguage;

    @JsonProperty("count.offtopEmote")
    private long offtopEmoteCount;

    @JsonProperty("emote.offtopEmote.id")
    private long offtopEmoteId;

    @JsonProperty("channel.joinAndLeaveChannel.id")
    private long joinAndLeaveChannelId;

    @JsonProperty("channel.logsChannel.id")
    private long logsChannelId;

    @JsonProperty("channel.botReadyChannel.id")
    private long botReadyChannelId;

    @JsonProperty("time.offtopDelete")
    private long offtopDeleteTime;

    @JsonProperty("function.musicFunctional.enable")
    private boolean musicFunctionalEnable;

    @JsonProperty("function.saveRoleFunctional.enable")
    private boolean saveRoleFunctionalEnable;

    @JsonProperty("function.rollFunctional.enable")
    private boolean rollFunctionalEnable;
    @JsonProperty("function.offtopFunctional.enable")
    private boolean offtopFunctionalEnable;


    @Override
    public String toString() {
        return "{" +
                "botId=" + botId +
                ", botLanguage=" + botLanguage +
                ", countsOfftopEmoteCount=" + offtopEmoteCount +
                ", joinAndLeaveChannelId=" + joinAndLeaveChannelId +
                ", logsChannelId=" + logsChannelId +
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
