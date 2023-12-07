package horse.boo.bot.database.table;

import horse.boo.bot.database.enums.Languages;
import jakarta.persistence.*;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;


@Entity
@Table(name = "config")
public class ConfigsTable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "guild_id")
    private Long guildId = 0L;

    @Column(name = "guild_name")
    private String guildName;

    @Column(name = "admin_channel_id")
    private Long adminChannelId;

    @Column(name = "bot_id")
    private Long botId;

    @Column(name = "bot_language")
    private String botLanguage;

    @Column(name = "unrelated_emote_count")
    private Integer unrelatedEmoteCount;

    @Column(name = "unrelated_emote_id")
    private Long unrelatedEmoteId;

    @Column(name = "welcome_channel_id")
    private Long welcomeChannelId;

    @Column(name = "goodbye_channel_id")
    private Long goodbyeChannelId;

    @Column(name = "log_channel_id")
    private Long logChannelId;

    @Column(name = "bot_readiness_channel_id")
    private Long botInfoChannelId;

    @Column(name = "unrelated_delete_time_sec")
    private Integer unrelatedDeleteTimeSec;

    @Column(name = "function_music_player")
    private boolean functionMusicPlayer;

    @Column(name = "function_role_saver")
    private boolean functionRememberingRoles;


    @Column(name = "function_dice_roller")
    private boolean functionDiceRoller;

    @Column(name = "function_unrelated_deleter")
    private boolean functionUnrelatedDeleter;


    public ConfigsTable() {
    }

    public ConfigsTable(@NotNull Guild guild) {
        this.setGuildId(guild.getIdLong());                                           //ID гильдии
        this.setGuildName(guild.getName());                                           //ID гильдии
        this.setAdminChannelId(guild.getDefaultChannel().getIdLong());                //ID админского канала
        this.setBotId(guild.getSelfMember().getIdLong());                             //ID бота
        this.setBotLanguage(Languages.ENGLISH.getLanguage());                         //язык по дефолту
        this.setUnrelatedEmoteCount(1000);                                            //кол-во эмодзей для удаления сообщения
        this.setUnrelatedEmoteId(0L);                                                 //ID эмодзи для удаления сообщения
        this.setWelcomeChannelId(guild.getDefaultChannel().getIdLong());              //ID канала для уведомлений о приходе нового юзера
        this.setGoodbyeChannelId(guild.getDefaultChannel().getIdLong());              //ID канала для уведомлений об уходе юзера
        this.setLogChannelId(0L);                                                     //ID канала для логов
        this.setBotInfoChannelId(guild.getDefaultChannel().getIdLong());              //ID канала для сообщений о готовности бота или сообщений с обновлениях
        this.setUnrelatedDeleteTimeSec(0);                                            //кол-во секунд в течении которых отображается уведомление об удалении сообщения
        this.setFunctionMusicPlayer(false);                                           //активация функционала музыкального бота
        this.setFunctionRememberingRoles(false);                                      //активация функционала сохранения ролей при выходе и повторном входе пользователя
        this.setFunctionDiceRoller(false);                                            //активация функционала броска кубов
        this.setFunctionUnrelatedDeleter(false);                                      //активация функционала удаления сообщений по набору реакций
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getGuildId() {
        return guildId;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    public Long getAdminChannelId() {
        return adminChannelId;
    }

    public void setAdminChannelId(Long adminChannelId) {
        this.adminChannelId = adminChannelId;
    }

    public Long getBotId() {
        return botId;
    }

    public void setBotId(Long botId) {
        this.botId = botId;
    }

    public String getBotLanguage() {
        return botLanguage;
    }

    public void setBotLanguage(String botLanguage) {
        this.botLanguage = botLanguage;
    }

    public Integer getUnrelatedEmoteCount() {
        return unrelatedEmoteCount;
    }

    public void setUnrelatedEmoteCount(Integer unrelatedEmoteCount) {
        this.unrelatedEmoteCount = unrelatedEmoteCount;
    }

    public Long getUnrelatedEmoteId() {
        return unrelatedEmoteId;
    }

    public void setUnrelatedEmoteId(Long unrelatedEmoteId) {
        this.unrelatedEmoteId = unrelatedEmoteId;
    }

    public Long getWelcomeChannelId() {
        return welcomeChannelId;
    }

    public void setWelcomeChannelId(Long welcomeChannelId) {
        this.welcomeChannelId = welcomeChannelId;
    }

    public Long getGoodbyeChannelId() {
        return goodbyeChannelId;
    }

    public void setGoodbyeChannelId(Long goodbyeChannelId) {
        this.goodbyeChannelId = goodbyeChannelId;
    }

    public Long getLogChannelId() {
        return logChannelId;
    }

    public void setLogChannelId(Long logChannelId) {
        this.logChannelId = logChannelId;
    }

    public Long getBotInfoChannelId() {
        return botInfoChannelId;
    }

    public void setBotInfoChannelId(Long botInfoChannelId) {
        this.botInfoChannelId = botInfoChannelId;
    }

    public Integer getUnrelatedDeleteTimeSec() {
        return unrelatedDeleteTimeSec;
    }

    public void setUnrelatedDeleteTimeSec(Integer unrelatedDeleteTimeSec) {
        this.unrelatedDeleteTimeSec = unrelatedDeleteTimeSec;
    }

    public boolean isFunctionMusicPlayer() {
        return functionMusicPlayer;
    }

    public void setFunctionMusicPlayer(boolean functionMusicPlayer) {
        this.functionMusicPlayer = functionMusicPlayer;
    }

    public boolean isFunctionRememberingRoles() {
        return functionRememberingRoles;
    }

    public void setFunctionRememberingRoles(boolean functionRememberingRoles) {
        this.functionRememberingRoles = functionRememberingRoles;
    }

    public boolean isFunctionDiceRoller() {
        return functionDiceRoller;
    }

    public void setFunctionDiceRoller(boolean functionDiceRoller) {
        this.functionDiceRoller = functionDiceRoller;
    }

    public boolean isFunctionUnrelatedDeleter() {
        return functionUnrelatedDeleter;
    }

    public void setFunctionUnrelatedDeleter(boolean functionUnrelatedDeleter) {
        this.functionUnrelatedDeleter = functionUnrelatedDeleter;
    }
    @Override
    public String toString() {
        return "ConfigsTable{" +
                "id=" + id +
                ", guildId=" + guildId +
                ", guildName=" + guildName +
                ", adminChannelId=" + adminChannelId +
                ", botId=" + botId +
                ", botLanguage='" + botLanguage + '\'' +
                ", unrelatedEmoteCount=" + unrelatedEmoteCount +
                ", unrelatedEmoteId=" + unrelatedEmoteId +
                ", welcomeChannelId=" + welcomeChannelId +
                ", goodbyeChannelId=" + goodbyeChannelId +
                ", logChannelId=" + logChannelId +
                ", botInfoChannelId=" + botInfoChannelId +
                ", unrelatedDeleteTimeSec=" + unrelatedDeleteTimeSec +
                ", functionMusicPlayer=" + functionMusicPlayer +
                ", functionRememberingRoles=" + functionRememberingRoles +
                ", functionDiceRoller=" + functionDiceRoller +
                ", functionUnrelatedDeleter=" + functionUnrelatedDeleter +
                '}';
    }
    public void sendInLogChannel(@NotNull Guild guild, String logMessage){
        guild.getTextChannelById(this.getLogChannelId()).sendMessage(logMessage).queue();
    }
}
