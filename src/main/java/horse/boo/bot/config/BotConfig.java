package horse.boo.bot.config;

import horse.boo.bot.events.*;
import horse.boo.bot.setup.InitialSetupEvent;
import horse.boo.bot.setup.steps.BotGuildJoin;
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

import java.util.List;

import static net.dv8tion.jda.api.requests.GatewayIntent.*;

@Configuration
@PropertySource("classpath:lyra.properties")

public class BotConfig {

    @Value("${bot.token}")
    private String token;

    public static JDABuilder jda;

    public BotConfig() {

    }

    @Bean
    public JDA jdaBuilder() {

        List<GatewayIntent> intents = List.of(GUILD_MESSAGES,
                MESSAGE_CONTENT,
                GUILD_MESSAGE_REACTIONS,
                GUILD_INVITES,
                GUILD_WEBHOOKS,
                GUILD_EMOJIS_AND_STICKERS,
                GUILD_VOICE_STATES,
                DIRECT_MESSAGE_REACTIONS,
                GUILD_MESSAGE_TYPING,
                GUILD_MEMBERS,
                GUILD_PRESENCES,
                SCHEDULED_EVENTS);

        jda = JDABuilder.createDefault(token);
        jda.disableCache(CacheFlag.SCHEDULED_EVENTS, CacheFlag.VOICE_STATE);
        jda.setBulkDeleteSplittingEnabled(false);
        jda.setCompression(Compression.NONE);
        jda.setActivity(Activity.playing("pi-pu-pa-pi-pa"));        // Активность бота
        jda.setEnabledIntents(intents);
        jda.addEventListeners(botReadyEvent());                             // Оповещение о запуске бота
        jda.addEventListeners(pingEvent());                                 // Проверка работоспособности бота
        jda.addEventListeners(memberJoinEvent());                           // Оповещение о новом участнике
        jda.addEventListeners(memberLeaveEvent());                          // Оповещение об уходе участника
        jda.addEventListeners(offtopDeleteEvent());                         // Удаление нежелательного контента
        jda.addEventListeners(initialSetupEvent());
        jda.addEventListeners(botGuildJoin());
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
    public InitialSetupEvent initialSetupEvent() {
        return new InitialSetupEvent();
    }

    @Bean
    public BotGuildJoin botGuildJoin() {
        return new BotGuildJoin();
    }
}