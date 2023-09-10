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
    private final BotReadyService botReadyService;
    private final MemberJoinService memberJoinService;
    private final MemberLeaveService memberLeaveService;
    private final PingService pingService;
    private final UnrelatedDeleteService unrelatedDeleteService;
    private final SlashCommandService slashCommandService;
    private final EmbedConstructorService embedConstructorService;
    private final SettingsService settingsService;
    private final IgnoreChannelService ignoreChannelService;
    private final DiceRollerService diceRollerService;

    public static String type = "default";

    public DiscordClient(
            BotReadyService botReadyService,
            MemberJoinService memberJoinService,
            MemberLeaveService memberLeaveService,
            PingService pingService,
            UnrelatedDeleteService unrelatedDeleteService,
            SlashCommandService slashCommandService,
            EmbedConstructorService embedConstructorService,
            SettingsService settingsService,
            IgnoreChannelService ignoreChannelService,
            DiceRollerService diceRollerService) {
        this.botReadyService = botReadyService;
        this.memberJoinService = memberJoinService;
        this.memberLeaveService = memberLeaveService;
        this.pingService = pingService;
        this.unrelatedDeleteService = unrelatedDeleteService;
        this.slashCommandService = slashCommandService;
        this.embedConstructorService = embedConstructorService;
        this.settingsService = settingsService;
        this.ignoreChannelService = ignoreChannelService;
        this.diceRollerService = diceRollerService;
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
                botReadyService,                        // Оповещение о запуске бота
                memberJoinService,                      // Оповещение о новом участнике
                memberLeaveService,                     // Оповещение об уходе участника
                pingService,                            // Проверка работоспособности бота
                unrelatedDeleteService,                 // Удаление нерелейтед контента пользователями
                slashCommandService,                    // Слеш комманды
                embedConstructorService,                // Конструктор эмбедов
                settingsService,                        // Настройка конфига
                ignoreChannelService,                   // Отключение функционала удаления по слешу
                diceRollerService                       // Функционал дайсов по слешу
        );
    }
}