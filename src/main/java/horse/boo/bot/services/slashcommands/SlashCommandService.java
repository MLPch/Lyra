package horse.boo.bot.services.slashcommands;

import horse.boo.bot.services.functionals.DiceRollerService;
import horse.boo.bot.services.slashcommands.handlers.EmbedConstructorService;
import horse.boo.bot.services.slashcommands.handlers.IgnoreChannelService;
import horse.boo.bot.services.slashcommands.handlers.SettingsService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static horse.boo.bot.services.functionals.DiceRollerService.getRollCommandData;
import static horse.boo.bot.services.slashcommands.handlers.EmbedConstructorService.constructorData;
import static horse.boo.bot.services.slashcommands.handlers.IgnoreChannelService.disableFunctionalsData;
import static horse.boo.bot.services.slashcommands.handlers.IgnoreChannelService.enableFunctionalsData;
import static horse.boo.bot.services.slashcommands.handlers.SettingsService.getLanguageSelectSlashCommandData;
import static horse.boo.bot.services.slashcommands.handlers.SettingsService.getSetupSlashCommandData;

@Component
public class SlashCommandService extends ListenerAdapter {

    @Autowired
    SettingsService settingsService;
    @Autowired
    IgnoreChannelService ignoreChannelService;
    @Autowired
    EmbedConstructorService embedConstructorService;
    @Autowired
    DiceRollerService diceRollerService;


    private final Logger logger = LoggerFactory.getLogger(SlashCommandService.class);


    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        var commands = event.getJDA().updateCommands()
                .addCommands(getRollCommandData())
                .addCommands(disableFunctionalsData())
                .addCommands(enableFunctionalsData())
                .addCommands(getSetupSlashCommandData())
                .addCommands(getLanguageSelectSlashCommandData())
                .addCommands(constructorData());

        commands.queue();

        // Only accept commands from guilds
        if (event.getGuild() == null) return;

        switch (event.getName()) {
            case "roll" -> {
                if (event.getOption("dice") == null) {
                    diceRollerService.roll(event, "custom");
                } else {
                    diceRollerService.roll(event, event.getOption("dice").getAsString());
                }
            }
            case "language_select" -> settingsService.languageSelect(event);
            case "setup" -> settingsService.setup(event);
            case "disable_functionals_from_channel" -> ignoreChannelService.disableFunctionals(event);
            case "enable_functionals_from_channel" -> ignoreChannelService.enableFunctionals(event);
            case "constructor" -> embedConstructorService.constructor(event);
            default -> event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
        }

    }
}
