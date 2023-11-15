package horse.boo.bot.services.slashcommands;

import horse.boo.bot.services.slashcommands.functionals.DiceRollerService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;


@Component
public class SlashCommandInteraction extends ListenerAdapter {
    final SettingsService settingsService;
    final FunctionalSwitcher functionalSwitcher;
    final MessageAboutUpdateService messageAboutUpdateService;
    final EmbedConstructorService embedConstructorService;
    final DiceRollerService diceRollerService;

    public SlashCommandInteraction(SettingsService settingsService,
                                   FunctionalSwitcher functionalSwitcher,
                                   MessageAboutUpdateService messageAboutUpdateService,
                                   EmbedConstructorService embedConstructorService,
                                   DiceRollerService diceRollerService) {
        this.settingsService = settingsService;
        this.functionalSwitcher = functionalSwitcher;
        this.messageAboutUpdateService = messageAboutUpdateService;
        this.embedConstructorService = embedConstructorService;
        this.diceRollerService = diceRollerService;
    }


    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {


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
            case "disable_functionals_in_channel" -> functionalSwitcher.disableFunctionals(event);
            case "enable_functionals_in_channel" -> functionalSwitcher.enableFunctionals(event);
            case "send_update" -> messageAboutUpdateService.sendUpdate(event);
            case "constructor" -> embedConstructorService.constructor(event);
            case "select_language" -> settingsService.languageSelect(event);
            case "setup" -> settingsService.setup(event);
            default -> event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
        }

    }
}
