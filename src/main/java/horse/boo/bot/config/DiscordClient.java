package horse.boo.bot.config;

import horse.boo.bot.services.BotReadyService;
import horse.boo.bot.services.MemberJoinService;
import horse.boo.bot.services.MemberLeaveService;
import horse.boo.bot.services.functionals.DiceRollerService;
import horse.boo.bot.services.functionals.UnrelatedDeleteService;
import horse.boo.bot.services.slashcommands.SlashCommandService;
import horse.boo.bot.services.slashcommands.handlers.EmbedConstructorService;
import horse.boo.bot.services.slashcommands.handlers.IgnoreChannelService;
import horse.boo.bot.services.slashcommands.handlers.SettingsService;
import horse.boo.bot.services.utils.PingService;
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
    private final EmbedConstructorService embedConstructorService;
    private final UnrelatedDeleteService unrelatedDeleteService;
    private final IgnoreChannelService ignoreChannelService;
    private final SlashCommandService slashCommandService;
    private final MemberLeaveService memberLeaveService;
    private final MemberJoinService memberJoinService;
    private final DiceRollerService diceRollerService;
    private final BotReadyService botReadyService;
    private final SettingsService settingsService;
    private final PingService pingService;

    public static String type = "default";

    public DiscordClient(
            EmbedConstructorService embedConstructorService,
            UnrelatedDeleteService unrelatedDeleteService,
            IgnoreChannelService ignoreChannelService,
            SlashCommandService slashCommandService,
            MemberLeaveService memberLeaveService,
            MemberJoinService memberJoinService,
            DiceRollerService diceRollerService,
            BotReadyService botReadyService,
            SettingsService settingsService,
            PingService pingService) {
        this.embedConstructorService = embedConstructorService;
        this.unrelatedDeleteService = unrelatedDeleteService;
        this.ignoreChannelService = ignoreChannelService;
        this.slashCommandService = slashCommandService;
        this.memberLeaveService = memberLeaveService;
        this.memberJoinService = memberJoinService;
        this.diceRollerService = diceRollerService;
        this.botReadyService = botReadyService;
        this.settingsService = settingsService;
        this.pingService = pingService;
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
                embedConstructorService,                // Конструктор эмбедов
                unrelatedDeleteService,                 // Удаление нерелейтед контента пользователями
                ignoreChannelService,                   // Отключение функционала удаления по слешу
                slashCommandService,                    // Слеш комманды
                memberLeaveService,                     // Оповещение об уходе участника
                memberJoinService,                      // Оповещение о новом участнике
                diceRollerService,                       // Функционал дайсов по слешу
                botReadyService,                        // Оповещение о запуске бота
                settingsService,                        // Настройка конфига
                pingService                            // Проверка работоспособности бота
        );
    }
}