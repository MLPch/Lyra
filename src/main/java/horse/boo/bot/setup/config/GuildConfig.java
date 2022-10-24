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

    @JsonProperty("bot.name")
    private String botName;

    @JsonProperty("bot.id")
    private String botId;

    @JsonProperty("emotes.notificationUsersEmote.name")
    private String notificationUsersEmoteName;

    @JsonProperty("emotes.notificationUsersEmote.id")
    private String notificationUsersEmoteId;

    @JsonProperty("emotes.notificationOwnerEmote.name")
    private String notificationOwnerEmoteName;

    @JsonProperty("emotes.notificationOwnerEmote.id")
    private String notificationOwnerEmoteId;

    @JsonProperty("counts.offtopEmoteCount")
    private String countsOfftopEmoteCount;

    @JsonProperty("channels.notificationChannel.name")
    private String notificationChannelName;

    @JsonProperty("channels.notificationChannel.id")
    private String notificationChannelId;

    @JsonProperty("channels.joinAndLeaveChannel.name")
    private String joinAndLeaveChannelName;

    @JsonProperty("channels.joinAndLeaveChannel.id")
    private String joinAndLeaveChannelId;

    @JsonProperty("channels.botReadyChannel.id")
    private String botReadyChannelId;

    @JsonProperty("channels.botReadyChannel.name")
    private String botReadyChannelName;

    @JsonProperty("channels.choiceRoomsChannel.name")
    private String choiceRoomsChannelName;

    @JsonProperty("channels.choiceRoomsChannel.id")
    private String choiceRoomsChannelId;

    @JsonProperty("times.offtopDeleteTime")
    private String offtopDeleteTime;

    @JsonProperty("functions.musicFunctional.enable")
    private boolean musicFunctionalEnable;

    @JsonProperty("functions.saveRoleFunctional.enable")
    private boolean saveRoleFunctionalEnable;

    @JsonProperty("functions.rollFunctional.enable")
    private boolean rollFunctionalEnable;


    @Override
    public String toString() {
        return "GuildConfig{" +
                "botName='" + botName + '\'' +
                ", botId='" + botId + '\'' +
                ", notificationUsersEmoteName='" + notificationUsersEmoteName + '\'' +
                ", notificationUsersEmoteId='" + notificationUsersEmoteId + '\'' +
                ", notificationOwnerEmoteName='" + notificationOwnerEmoteName + '\'' +
                ", notificationOwnerEmoteId='" + notificationOwnerEmoteId + '\'' +
                ", countsOfftopEmoteCount='" + countsOfftopEmoteCount + '\'' +
                ", notificationChannelName='" + notificationChannelName + '\'' +
                ", notificationChannelId='" + notificationChannelId + '\'' +
                ", joinAndLeaveChannelName='" + joinAndLeaveChannelName + '\'' +
                ", joinAndLeaveChannelId='" + joinAndLeaveChannelId + '\'' +
                ", botReadyChannelId='" + botReadyChannelId + '\'' +
                ", botReadyChannelName='" + botReadyChannelName + '\'' +
                ", choiceRoomsChannelName='" + choiceRoomsChannelName + '\'' +
                ", choiceRoomsChannelId='" + choiceRoomsChannelId + '\'' +
                ", offtopDeleteTime='" + offtopDeleteTime + '\'' +
                ", musicFunctionalEnable=" + musicFunctionalEnable + '\'' +
                ", saveRoleFunctionalEnable=" + saveRoleFunctionalEnable + '\'' +
                ", rollFunctionalEnable=" + rollFunctionalEnable +
                '}';
    }
}
