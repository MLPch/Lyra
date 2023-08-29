//package horse.boo.bot.database.models;
//
//import horse.boo.bot.enums.Languages;
//
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//
//
//public class OldConfig {
//
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;
//    private long guild_id;
//    private long admin_channel_id;
//    private long bot_id;
//    private String bot_language;
//    private int offtop_emote_count;
//    private long offtop_emote_id;
//    private long welcome_channel_id;
//    private long goodbye_channel_id;
//    private long log_channel_id;
//    private long bot_readiness_channel_id;
//    private int offtop_delete_time_sec;
//    private boolean function_music_player;
//    private boolean function_role_saver;
//    private boolean function_dice_roller;
//    private boolean function_offlop_deleter;
//
//    public OldConfig() {
//
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public long getConfig_guild_id() {
//        return guild_id;
//    }
//
//    public long getConfig_admin_channel_id() {
//        return admin_channel_id;
//    }
//
//    public long getConfig_bot_id() {
//        return bot_id;
//    }
//
//    public String getConfig_bot_language() {
//        return bot_language;
//    }
//
//    public int getConfig_offtop_emote_count() {
//        return offtop_emote_count;
//    }
//
//    public long getConfig_offtop_emote_id() {
//        return offtop_emote_id;
//    }
//
//    public long getConfig_welcome_channel_id() {
//        return welcome_channel_id;
//    }
//
//    public long getConfig_goodbye_channel_id() {
//        return goodbye_channel_id;
//    }
//
//    public long getConfig_log_channel_id() {
//        return log_channel_id;
//    }
//
//    public long getConfig_bot_readiness_channel_id() {
//        return bot_readiness_channel_id;
//    }
//
//    public int getConfig_offtop_delete_time_sec() {
//        return offtop_delete_time_sec;
//    }
//
//    public boolean isConfig_function_music_player() {
//        return function_music_player;
//    }
//
//    public boolean isConfig_function_role_saver() {
//        return function_role_saver;
//    }
//
//    public boolean isConfig_function_dice_roller() {
//        return function_dice_roller;
//    }
//
//    public boolean isConfig_function_offlop_deleter() {
//        return function_offlop_deleter;
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
//        public OldConfig build() {
//            return new OldConfig(this);
//        }
//    }
//
//    private Config22(Builder builder) {
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
//    @Override
//    public String toString() {
//        return "OldConfig{" +
//                ", guild_id=" + guild_id +
//                ", admin_channel_id=" + admin_channel_id +
//                ", bot_id=" + bot_id +
//                ", bot_language=" + bot_language +
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
//}
//
//
