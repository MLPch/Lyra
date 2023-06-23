package horse.boo.bot.config;

import horse.boo.bot.events.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.security.auth.login.LoginException;
import java.util.List;

import static net.dv8tion.jda.api.requests.GatewayIntent.*;

@Configuration
@PropertySource("classpath:princess.properties")
public class BotConfig {

    @Value("${bot.token}")
    private String token;

    public static JDABuilder jda;

    @Bean
    public JDA jdaBuilder() throws LoginException {
        List<GatewayIntent> intents = List.of(GUILD_MESSAGES,
                GUILD_MESSAGE_REACTIONS,
                GUILD_EMOJIS,
                GUILD_INVITES,
                GUILD_VOICE_STATES,
                GUILD_MESSAGE_REACTIONS,
                DIRECT_MESSAGE_REACTIONS,
                GUILD_MESSAGE_TYPING,
                GUILD_MEMBERS,
                GUILD_PRESENCES);
        jda = JDABuilder.createDefault(token);
        jda.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        jda.setBulkDeleteSplittingEnabled(false);
        jda.setCompression(Compression.NONE);
        jda.setActivity(Activity.playing("with stars."));        // Активность бота
        jda.setEnabledIntents(intents);
        jda.addEventListeners(botReadyEvent());                             // Оповещение о запуске бота
        jda.addEventListeners(pingEvent());                                 // Проверка работоспособности бота
        jda.addEventListeners(memberLeaveEvent());                          // Оповещение об уходе участника
        jda.addEventListeners(offtopDeleteEvent());                         // Удаление нежелательного контента
        jda.addEventListeners(addReactionOnPostEvent());                    // Подписка на ивент (триггер)
        jda.addEventListeners(removeReactionOnPostEvent());                 // Подписка на ивент (исполнитель)
        jda.addEventListeners(helpEvent());                                 // Правила использования подписки на ивент
        jda.addEventListeners(slashCommand());


        return jda.build();
    }

    @Bean
    public BotReadyEvent botReadyEvent() {
        return new BotReadyEvent();
    }

    @Bean
    public PingEvent pingEvent() {
        return new PingEvent();
    }

    @Bean
    public MemberLeaveEvent memberLeaveEvent() {
        return new MemberLeaveEvent();
    }

    @Bean
    public OfftopDeleteEvent offtopDeleteEvent() {
        return new OfftopDeleteEvent();
    }

    @Bean
    public AddReactionOnPostEvent addReactionOnPostEvent() {
        return new AddReactionOnPostEvent();
    }

    @Bean
    public RemoveReactionOnPostEvent removeReactionOnPostEvent() {
        return new RemoveReactionOnPostEvent();
    }

    @Bean
    public HelpEvent helpEvent() {
        return new HelpEvent();
    }

    @Bean
    public SlashCommand slashCommand() {
        return new SlashCommand();
    }


    @Configuration
    @PropertySource("classpath:princess.properties")
    public class BotConfig2 {

        @Value("${bot.token2}")
        private String token2;

        public JDABuilder jda2;

        @Bean
        public JDA jdaBuilder2() throws LoginException {
            List<GatewayIntent> intents = List.of(GUILD_MESSAGES,
                    GUILD_MESSAGE_REACTIONS,
                    GUILD_EMOJIS,
                    GUILD_INVITES,
                    GUILD_VOICE_STATES,
                    GUILD_MESSAGE_REACTIONS,
                    DIRECT_MESSAGE_REACTIONS,
                    GUILD_MESSAGE_TYPING,
                    GUILD_MEMBERS,
                    GUILD_PRESENCES);
            jda2 = JDABuilder.createDefault(token2);
            jda2.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
            jda2.setBulkDeleteSplittingEnabled(false);
            jda2.setCompression(Compression.NONE);
            jda2.setActivity(Activity.listening("to your troubles."));        // Активность бота
            jda2.setEnabledIntents(intents);
            jda2.addEventListeners(memberJoinEvent());                           // Оповещение о новом участнике


            return jda2.build();
        }

        @Bean
        public MemberJoinEvent memberJoinEvent() {
            return new MemberJoinEvent();
        }

    }
}