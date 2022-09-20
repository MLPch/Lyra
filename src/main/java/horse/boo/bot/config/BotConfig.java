package horse.boo.bot.config;

import horse.boo.bot.events.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
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
@PropertySource("classpath:test.properties")
public class BotConfig {

    @Value("${bot.token}")
    private String token;

    public static JDABuilder jda;

    @Bean
    public JDA jdaBuilder() {
        List<GatewayIntent> intents = List.of(GUILD_MESSAGES,
                GUILD_MESSAGE_REACTIONS,
                GUILD_INVITES,
                GUILD_WEBHOOKS,
                GUILD_BANS,
                GUILD_EMOJIS_AND_STICKERS,
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
        jda.setActivity(Activity.watching("ponies, dreams of people."));        // Активность бота
        jda.setEnabledIntents(intents);
        jda.addEventListeners(botReadyEvent());                             // Оповещение о запуске бота
        jda.addEventListeners(pingEvent());                                 // Проверка работоспособности бота
        jda.addEventListeners(memberJoinEvent());                           // Оповещение о новом участнике
        jda.addEventListeners(memberLeaveEvent());                          // Оповещение об уходе участника
        jda.addEventListeners(offtopDeleteEvent());                         // Удаление нежелательного контента
        jda.addEventListeners(addReactionOnPostEvent());                    // Подписка на ивент (триггер)
        jda.addEventListeners(removeReactionOnPostEvent());                 // Подписка на ивент (исполнитель)
        jda.addEventListeners(helpEvent());                                 // Правила использования подписки на ивент

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
    public MemberJoinEvent memberJoinEvent() {
        return new MemberJoinEvent();
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

}