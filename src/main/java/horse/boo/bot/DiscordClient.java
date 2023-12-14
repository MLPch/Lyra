package horse.boo.bot;

import horse.boo.bot.services.BotReadyService;
import horse.boo.bot.services.MemberJoinService;
import horse.boo.bot.services.MemberLeaveService;
import horse.boo.bot.services.WednesdayPostingService;
import horse.boo.bot.services.slashcommands.*;
import horse.boo.bot.services.slashcommands.functionals.DiceRollerService;
import horse.boo.bot.services.slashcommands.functionals.UnrelatedDeleteService;
import horse.boo.bot.services.slashcommands.interractions.ButtonInteraction;
import horse.boo.bot.services.slashcommands.interractions.SlashCommandInteraction;
import horse.boo.bot.services.utils.MessageFromBotService;
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
    private final WednesdayPostingService wednesdayPostingService;
    private final UnrelatedDeleteService unrelatedDeleteService;
    private final LanguageSelectService languageSelectService;
    private final MessageFromBotService messageFromBotService;
    private final FunctionalSwitcher functionalSwitcher;
    private final MemberLeaveService memberLeaveService;
    private final MemberJoinService memberJoinService;
    private final DiceRollerService diceRollerService;
    private final BotReadyService botReadyService;
    private final SettingsService settingsService;
    private final PingService pingService;
    private final SlashCommandInteraction slashCommandInteraction;
    private final ButtonInteraction buttonInteraction;

    public static String TYPE = "default";

    public DiscordClient(
            MessageAboutUpdateService messageAboutUpdateService,
            EmbedConstructorService embedConstructorService,
            SlashCommandInteraction slashCommandInteraction,
            WednesdayPostingService wednesdayPostingService,
            UnrelatedDeleteService unrelatedDeleteService,
            LanguageSelectService languageSelectService,
            MessageFromBotService messageFromBotService,
            FunctionalSwitcher functionalSwitcher,
            MemberLeaveService memberLeaveService,
            ButtonInteraction buttonInteraction,
            MemberJoinService memberJoinService,
            DiceRollerService diceRollerService,
            BotReadyService botReadyService,
            SettingsService settingsService,
            PingService pingService) {
        this.messageAboutUpdateService = messageAboutUpdateService;
        this.embedConstructorService = embedConstructorService;
        this.slashCommandInteraction = slashCommandInteraction;
        this.wednesdayPostingService = wednesdayPostingService;
        this.unrelatedDeleteService = unrelatedDeleteService;
        this.languageSelectService = languageSelectService;
        this.messageFromBotService = messageFromBotService;
        this.functionalSwitcher = functionalSwitcher;
        this.memberLeaveService = memberLeaveService;
        this.buttonInteraction = buttonInteraction;
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
                .setActivity(Activity.listening("Anthropology"))        // Bot activity
                .setStatus(OnlineStatus.ONLINE)
                .build();

        jda.addEventListener(
                messageAboutUpdateService,              // Sending a message about a new update to all connected guilds
                embedConstructorService,                // Constructor for changing the contents of embeds
                slashCommandInteraction,                // Slash commands
                wednesdayPostingService,                // It is Wednesday, my dudes
                unrelatedDeleteService,                 // Deleting unreleased content by users
                languageSelectService,                  //
                messageFromBotService,                  //
                functionalSwitcher,                     // Disabling the slash deletion functionality
                memberLeaveService,                     // Notification of a participant's departure
                buttonInteraction,                      //
                memberJoinService,                      // Notification of a new participant
                diceRollerService,                      // Slash dice functionality
                botReadyService,                        // Notification of bot launch
                settingsService,                        // Setup config
                pingService                             // Checking the bot's operability
        );
    }
}