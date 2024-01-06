package horse.boo.bot;

import horse.boo.bot.services.BotReadyService;
import horse.boo.bot.services.MemberJoinService;
import horse.boo.bot.services.MemberLeaveService;
import horse.boo.bot.services.WednesdayPostingService;
import horse.boo.bot.services.commands.CommandManager;
import horse.boo.bot.services.slashcommands.functionals.UnrelatedDeleteService;
import horse.boo.bot.services.slashcommands.functionals.player.*;
import horse.boo.bot.services.slashcommands.interractions.ButtonInteraction;
import horse.boo.bot.services.slashcommands.interractions.SlashCommandInteraction;
import horse.boo.bot.services.utils.MessageAboutUpdateService;
import horse.boo.bot.services.utils.MessageFromBotService;
import horse.boo.bot.services.utils.PingService;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

@Component
public class DiscordClient implements CommandLineRunner {

    @Value("${discord.token}")
    private String token;
    private final MessageAboutUpdateService messageAboutUpdateService;
    private final SlashCommandInteraction slashCommandInteraction;
    private final WednesdayPostingService wednesdayPostingService;
    private final UnrelatedDeleteService unrelatedDeleteService;
    private final MessageFromBotService messageFromBotService;
    private final MemberLeaveService memberLeaveService;
    private final MemberJoinService memberJoinService;
    private final BotReadyService botReadyService;
    private final PingService pingService;
    private final ButtonInteraction buttonInteraction;

    public static String TYPE = "default";

    public DiscordClient(
            MessageAboutUpdateService messageAboutUpdateService,
            SlashCommandInteraction slashCommandInteraction,
            WednesdayPostingService wednesdayPostingService,
            UnrelatedDeleteService unrelatedDeleteService,
            MessageFromBotService messageFromBotService,
            MemberLeaveService memberLeaveService,
            ButtonInteraction buttonInteraction,
            MemberJoinService memberJoinService,
            BotReadyService botReadyService,
            PingService pingService) {
        this.messageAboutUpdateService = messageAboutUpdateService;
        this.slashCommandInteraction = slashCommandInteraction;
        this.wednesdayPostingService = wednesdayPostingService;
        this.unrelatedDeleteService = unrelatedDeleteService;
        this.messageFromBotService = messageFromBotService;
        this.memberLeaveService = memberLeaveService;
        this.buttonInteraction = buttonInteraction;
        this.memberJoinService = memberJoinService;
        this.botReadyService = botReadyService;
        this.pingService = pingService;
    }

    @Override
    public void run(String... args) {
        var jda = JDABuilder
                .createDefault(token)
                .setActivity(Activity.listening("Anthropology"))        // Bot activity
                .enableIntents(EnumSet.allOf(GatewayIntent.class))
                .setBulkDeleteSplittingEnabled(false)
                .setCompression(Compression.NONE)
                .setStatus(OnlineStatus.ONLINE)
                .build();

        CommandManager playerManager = new CommandManager();
        playerManager.add(new Play());
        playerManager.add(new Skip());
        playerManager.add(new Stop());
        playerManager.add(new NowPlaying());
        playerManager.add(new Queue());

        // TODO: Добавить радио


        // TODO: подумать над переработкой в менеджеры
        jda.addEventListener(
                messageAboutUpdateService,              // Sending a message about a new update to all connected guilds
                slashCommandInteraction,                // Slash commands
                wednesdayPostingService,                // It is Wednesday, my dudes
                unrelatedDeleteService,                 // Deleting unreleased content by users
                messageFromBotService,                  //
                memberLeaveService,                     // Notification of a participant's departure
                buttonInteraction,                      //
                memberJoinService,                      // Notification of a new participant
                botReadyService,                        // Notification of bot launch
                pingService,                            // Checking the bot's operability
                playerManager
        );
    }
}