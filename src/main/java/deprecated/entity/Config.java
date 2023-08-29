//package horse.boo.bot.database.entity;
//
//import horse.boo.bot.enums.Languages;
//
//import javax.persistence.*;
//import java.io.Serializable;
//
//@Entity
//@Table(name = "config")
//public class Config implements Serializable {
//    @Id
//    @GeneratedValue(generator = "identity")
//    @Column(name = "id", length = 10, nullable = false)
//    private long id;
//    @Column(name = "guild_id")
//    private long guild_id;
//    @Column
//    private long admin_channel_id;
//    @Column
//    private long bot_id;
//    @Column
//    private String bot_language;
//    @Column
//    private int offtop_emote_count;
//    @Column
//    private long offtop_emote_id;
//    @Column
//    private long welcome_channel_id;
//    @Column
//    private long goodbye_channel_id;
//    @Column
//    private long log_channel_id;
//    @Column
//    private long bot_readiness_channel_id;
//    @Column
//    private int offtop_delete_time_sec;
//    @Column
//    private boolean function_music_player;
//    @Column
//    private boolean function_role_saver;
//    @Column
//    private boolean function_dice_roller;
//    @Column
//    private boolean function_offlop_deleter;
//
//
//    protected Config() {
//
//    }
//
//    public Config(long id, long guild_id, long admin_channel_id, long bot_id, String bot_language, int offtop_emote_count, long offtop_emote_id, long welcome_channel_id, long goodbye_channel_id, long log_channel_id, long bot_readiness_channel_id, int offtop_delete_time_sec, boolean function_music_player, boolean function_role_saver, boolean function_dice_roller, boolean function_offlop_deleter) {
//        this.id = id;
//        this.guild_id = guild_id;
//        this.admin_channel_id = admin_channel_id;
//        this.bot_id = bot_id;
//        this.bot_language = bot_language;
//        this.offtop_emote_count = offtop_emote_count;
//        this.offtop_emote_id = offtop_emote_id;
//        this.welcome_channel_id = welcome_channel_id;
//        this.goodbye_channel_id = goodbye_channel_id;
//        this.log_channel_id = log_channel_id;
//        this.bot_readiness_channel_id = bot_readiness_channel_id;
//        this.offtop_delete_time_sec = offtop_delete_time_sec;
//        this.function_music_player = function_music_player;
//        this.function_role_saver = function_role_saver;
//        this.function_dice_roller = function_dice_roller;
//        this.function_offlop_deleter = function_offlop_deleter;
//    }
//
//
//    @Override
//    public String toString() {
//        return "Config{" +
//                "id=" + id +
//                ", guild_id=" + guild_id +
//                ", admin_channel_id=" + admin_channel_id +
//                ", bot_id=" + bot_id +
//                ", bot_language='" + bot_language + '\'' +
//                ", offtop_emote_count=" + offtop_emote_count +
//                ", offtop_emote_id=" + offtop_emote_id +
//                ", welcome_channel_id=" + welcome_channel_id +
//                ", goodbye_channel_id=" + goodbye_channel_id +
//                ", log_channel_id=" + log_channel_id +
//                ", bot_readiness_channel_id=" + bot_readiness_channel_id +
//                ", offtop_delete_time_sec=" + offtop_delete_time_sec +
//                ", function_music_player=" + function_music_player +
//                ", function_role_saver=" + function_role_saver +
//                ", function_dice_roller=" + function_dice_roller +
//                ", function_offlop_deleter=" + function_offlop_deleter +
//                '}';
//    }
//
//    public static class Builder {
//        private long id;
//
//        // Required parameters
//        private final long guild_id;
//
//        // Optional parameters - initialized to default values
//        private long admin_channel_id = 0;
//        private long bot_id = 0;
//        private String bot_language = Languages.ENGLISH.getLanguage();
//        private int offtop_emote_count = 0;
//        private long offtop_emote_id = 0;
//        private long welcome_channel_id = 0;
//        private long goodbye_channel_id = 0;
//        private long log_channel_id = 0;
//        private long bot_readiness_channel_id = 0;
//        private int offtop_delete_time_sec = 0;
//        private boolean function_music_player = false;
//        private boolean function_role_saver = false;
//        private boolean function_dice_roller = false;
//        private boolean function_offlop_deleter = false;
//
//
//        public Builder(long guild_id) {
//            this.guild_id = guild_id;
//        }
//
//        public Builder id(long val) {
//            id = val;
//            return this;
//        }
//
//        public Builder admin_channel_id(long val) {
//            admin_channel_id = val;
//            return this;
//        }
//
//        public Builder bot_id(long val) {
//            bot_id = val;
//            return this;
//        }
//
//        public Builder bot_language(String val) {
//            bot_language = val;
//            return this;
//        }
//
//        public Builder offtop_emote_count(int val) {
//            offtop_emote_count = val;
//            return this;
//        }
//
//        public Builder offtop_emote_id(long val) {
//            offtop_emote_id = val;
//            return this;
//        }
//
//        public Builder welcome_channel_id(long val) {
//            welcome_channel_id = val;
//            return this;
//        }
//
//        public Builder goodbye_channel_id(long val) {
//            goodbye_channel_id = val;
//            return this;
//        }
//
//        public Builder log_channel_id(long val) {
//            log_channel_id = val;
//            return this;
//        }
//
//        public Builder bot_readiness_channel_id(long val) {
//            bot_readiness_channel_id = val;
//            return this;
//        }
//
//        public Builder offtop_delete_time_sec(int val) {
//            offtop_delete_time_sec = val;
//            return this;
//        }
//
//        public Builder function_music_player(boolean val) {
//            function_music_player = val;
//            return this;
//        }
//
//        public Builder function_role_saver(boolean val) {
//            function_role_saver = val;
//            return this;
//        }
//
//        public Builder function_dice_roller(boolean val) {
//            function_dice_roller = val;
//            return this;
//        }
//
//        public Builder function_offlop_deleter(boolean val) {
//            function_offlop_deleter = val;
//            return this;
//        }
//
//        public Config build() {
//            return new Config(this);
//        }
//    }
//
//
//    private Config(Builder builder) {
//        id = builder.id;
//        guild_id = builder.guild_id;
//        admin_channel_id = builder.admin_channel_id;
//        bot_id = builder.bot_id;
//        bot_language = builder.bot_language;
//        offtop_emote_count = builder.offtop_emote_count;
//        offtop_emote_id = builder.offtop_emote_id;
//        welcome_channel_id = builder.welcome_channel_id;
//        goodbye_channel_id = builder.goodbye_channel_id;
//        log_channel_id = builder.log_channel_id;
//        bot_readiness_channel_id = builder.bot_readiness_channel_id;
//        offtop_delete_time_sec = builder.offtop_delete_time_sec;
//        function_music_player = builder.function_music_player;
//        function_role_saver = builder.function_role_saver;
//        function_dice_roller = builder.function_dice_roller;
//        function_offlop_deleter = builder.function_offlop_deleter;
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public long getGuild_id() {
//        return guild_id;
//    }
//
//    public void setGuild_id(long guild_id) {
//        this.guild_id = guild_id;
//    }
//
//    public long getAdmin_channel_id() {
//        return admin_channel_id;
//    }
//
//    public void setAdmin_channel_id(long admin_channel_id) {
//        this.admin_channel_id = admin_channel_id;
//    }
//
//    public long getBot_id() {
//        return bot_id;
//    }
//
//    public void setBot_id(long bot_id) {
//        this.bot_id = bot_id;
//    }
//
//    public String getBot_language() {
//        return bot_language;
//    }
//
//    public void setBot_language(String bot_language) {
//        this.bot_language = bot_language;
//    }
//
//    public int getOfftop_emote_count() {
//        return offtop_emote_count;
//    }
//
//    public void setOfftop_emote_count(int offtop_emote_count) {
//        this.offtop_emote_count = offtop_emote_count;
//    }
//
//    public long getOfftop_emote_id() {
//        return offtop_emote_id;
//    }
//
//    public void setOfftop_emote_id(long offtop_emote_id) {
//        this.offtop_emote_id = offtop_emote_id;
//    }
//
//    public long getWelcome_channel_id() {
//        return welcome_channel_id;
//    }
//
//    public void setWelcome_channel_id(long welcome_channel_id) {
//        this.welcome_channel_id = welcome_channel_id;
//    }
//
//    public long getGoodbye_channel_id() {
//        return goodbye_channel_id;
//    }
//
//    public void setGoodbye_channel_id(long goodbye_channel_id) {
//        this.goodbye_channel_id = goodbye_channel_id;
//    }
//
//    public long getLog_channel_id() {
//        return log_channel_id;
//    }
//
//    public void setLog_channel_id(long log_channel_id) {
//        this.log_channel_id = log_channel_id;
//    }
//
//    public long getBot_readiness_channel_id() {
//        return bot_readiness_channel_id;
//    }
//
//    public void setBot_readiness_channel_id(long bot_readiness_channel_id) {
//        this.bot_readiness_channel_id = bot_readiness_channel_id;
//    }
//
//    public int getOfftop_delete_time_sec() {
//        return offtop_delete_time_sec;
//    }
//
//    public void setOfftop_delete_time_sec(int offtop_delete_time_sec) {
//        this.offtop_delete_time_sec = offtop_delete_time_sec;
//    }
//
//    public boolean isFunction_music_player() {
//        return function_music_player;
//    }
//
//    public void setFunction_music_player(boolean function_music_player) {
//        this.function_music_player = function_music_player;
//    }
//
//    public boolean isFunction_role_saver() {
//        return function_role_saver;
//    }
//
//    public void setFunction_role_saver(boolean function_role_saver) {
//        this.function_role_saver = function_role_saver;
//    }
//
//    public boolean isFunction_dice_roller() {
//        return function_dice_roller;
//    }
//
//    public void setFunction_dice_roller(boolean function_dice_roller) {
//        this.function_dice_roller = function_dice_roller;
//    }
//
//    public boolean isFunction_offlop_deleter() {
//        return function_offlop_deleter;
//    }
//
//    public void setFunction_offlop_deleter(boolean function_offlop_deleter) {
//        this.function_offlop_deleter = function_offlop_deleter;
//    }
//
//}