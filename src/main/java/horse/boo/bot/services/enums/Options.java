package horse.boo.bot.services.enums;

import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.function.Function;

public enum Options {

    ADMIN_CHANNEL("<#%%> is designated as the administrators channel.", "admin_channel",
            (OptionMapping o) -> o.getAsChannel().getIdLong()),//todo take a look maybe we can use getAsLong without this stupid getAsChannel
    UNRELATED_COUNT("__**%%**__ this is how many seconds the message about deleting a post in the guild is now displayed", "unrelated-count",
            OptionMapping::getAsInt),//ah i wish we have default in java
    UNRELATED_EMOTE("%% is installed as an emoji to remove unrelated", "unrelated-emote",
            OptionMapping::getAsLong),
    JOIN_CHANNEL("<#%%> is designated as a channel for messages about users entering the server", "join-channel",
            (OptionMapping o) -> o.getAsChannel().getIdLong()),
    LEAVE_CHANNEL("<#%%> is designated as a channel for messages about users who have left the server", "leave-channel",
            (OptionMapping o) -> o.getAsChannel().getIdLong()),
    LOGS("<#%%> is designated as a channel for logs", "logs",
            (OptionMapping o) -> o.getAsChannel().getIdLong()),
    BOT_INFO_CHANNEL("<#%%> is assigned as a channel for bot updates", "bot_info_channel",
            (OptionMapping o) -> o.getAsChannel().getIdLong()),
    UNRELATED_DELETE("The following time is set to __%%__ to display the message about the removal of the unrelated",
            "unrelated_delete_time", OptionMapping::getAsInt),
    MUSIC_PLAYER("The functionality of the music player is set to __**%%**__", "music_player", OptionMapping::getAsBoolean),
    REMEMBERING_ROLES("The functionality of saving roles is set to __**%%**__", "remembering_roles", OptionMapping::getAsBoolean),
    ROLL_THE_DICE("The dice-throwing functionality is set to __**%%**__", "dice_roller", OptionMapping::getAsBoolean),
    UNRELATED_REMOVER("The unrelated removal functionality is set to __**%%**__", "unrelated_deleter", OptionMapping::getAsBoolean);

    //todo replace "set to" with something meaningful

    public final String optionName;
    public final String text;

    public final Function<OptionMapping, Object> mapping;

    Options(String msgText, String optionName, Function<OptionMapping, Object> functionMapping) {
        this.text = msgText;
        this.optionName = optionName;
        this.mapping = functionMapping;
    }

}
