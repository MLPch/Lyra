package horse.boo.bot;

import horse.boo.bot.services.BotReadyService;
import horse.boo.bot.services.MemberLeaveService;
import horse.boo.bot.services.slashcommands.*;
import horse.boo.bot.services.slashcommands.functionals.DiceRollerService;
import horse.boo.bot.services.slashcommands.functionals.UnrelatedDeleteService;
import horse.boo.bot.services.utils.PingService;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
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
    private final MessageAboutUpdateService messageAboutUpdateService;
    private final EmbedConstructorService embedConstructorService;
    private final SlashCommandInteraction slashCommandInteraction;
    private final UnrelatedDeleteService unrelatedDeleteService;
    private final FunctionalSwitcher functionalSwitcher;
    private final MemberLeaveService memberLeaveService;
    private final DiceRollerService diceRollerService;
    private final BotReadyService botReadyService;
    private final SettingsService settingsService;
    private final PingService pingService;

    public static String TYPE = "default";

    public DiscordClient(
            MessageAboutUpdateService messageAboutUpdateService,
            EmbedConstructorService embedConstructorService,
            SlashCommandInteraction slashCommandInteraction,
            UnrelatedDeleteService unrelatedDeleteService,
            FunctionalSwitcher functionalSwitcher,
            MemberLeaveService memberLeaveService,
            DiceRollerService diceRollerService,
            BotReadyService botReadyService,
            SettingsService settingsService,
            PingService pingService) {
        this.messageAboutUpdateService = messageAboutUpdateService;
        this.embedConstructorService = embedConstructorService;
        this.slashCommandInteraction = slashCommandInteraction;
        this.unrelatedDeleteService = unrelatedDeleteService;
        this.functionalSwitcher = functionalSwitcher;
        this.memberLeaveService = memberLeaveService;
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
                .setActivity(Activity.playing("dream streaming"))        // Bot activity
                .setStatus(OnlineStatus.IDLE)
                .build();

        jda.addEventListener(
                messageAboutUpdateService,              // Sending a message about a new update to all connected guilds
                embedConstructorService,                // Constructor for changing the contents of embeds
                slashCommandInteraction,                // Slash commands
                unrelatedDeleteService,                 // Deleting unreleased content by users
                functionalSwitcher,                     // Disabling the slash deletion functionality
                memberLeaveService,                     // Notification of a participant's departure
                diceRollerService,                      // Slash dice functionality
                botReadyService,                        // Notification of bot launch
                settingsService,                        // Setup config
                pingService                             // Checking the bot's operability
        );
    }
}