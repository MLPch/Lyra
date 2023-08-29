package horse.boo.bot.config;

import horse.boo.bot.events.*;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

@Component
public class DiscordClient implements CommandLineRunner {

    @Value("${discord.token}")
    private String token;
    private final MemberJoinService memberJoinService;
    private final MemberLeaveService memberLeaveService;
    private final BotReadyService botReadyService;
    private final PingService pingService;
    private final UnrelatedDeleteService unrelatedDeleteService;
    private final SlashCommandService slashCommandService;

    public static String type = "default";

    public DiscordClient(
            MemberJoinService memberJoinService,
            MemberLeaveService memberLeaveService,
            BotReadyService botReadyService,
            PingService pingService,
            UnrelatedDeleteService unrelatedDeleteService,
            SlashCommandService slashCommandService
    ) {
        this.memberJoinService = memberJoinService;
        this.memberLeaveService = memberLeaveService;
        this.botReadyService = botReadyService;
        this.pingService = pingService;
        this.unrelatedDeleteService = unrelatedDeleteService;
        this.slashCommandService = slashCommandService;
    }

    @Override
    public void run(String... args) {
        var jda = JDABuilder
                .createDefault(token)
                .enableIntents(EnumSet.allOf(GatewayIntent.class))
                .disableCache(CacheFlag.SCHEDULED_EVENTS, CacheFlag.VOICE_STATE)
                .setBulkDeleteSplittingEnabled(false)
                .setCompression(Compression.NONE)
                .setActivity(Activity.playing("pi-pu-pa-pi-pa"))        // Активность бота
                .build();

        jda.addEventListener(
                memberJoinService,                      // Оповещение о новом участнике
                memberLeaveService,                     // Оповещение об уходе участника
                botReadyService,                        // Оповещение о запуске бота
                pingService,                            // Проверка работоспособности бота
                unrelatedDeleteService,                 // Удаление нерелейтед контента пользователями
                slashCommandService                     // Слеш команды
        );
    }
}