package horse.boo.bot.services.slashcommands.interractions;

import horse.boo.bot.services.slashcommands.*;
import horse.boo.bot.services.slashcommands.functionals.DiceRollerService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;


@Component
public class SlashCommandInteraction extends ListenerAdapter {
    final MessageAboutUpdateService messageAboutUpdateService;
    final ChannelGatekeeperService channelGatekeeperService;
    final EmbedConstructorService embedConstructorService;
    final LanguageSelectService languageSelectService;
    final FunctionalSwitcher functionalSwitcher;
    final DiceRollerService diceRollerService;
    final SettingsService settingsService;

    public SlashCommandInteraction(
            MessageAboutUpdateService messageAboutUpdateService,
            ChannelGatekeeperService channelGatekeeperService,
            EmbedConstructorService embedConstructorService,
            LanguageSelectService languageSelectService,
            FunctionalSwitcher functionalSwitcher,
            DiceRollerService diceRollerService,
            SettingsService settingsService
    ) {
        this.messageAboutUpdateService = messageAboutUpdateService;
        this.channelGatekeeperService = channelGatekeeperService;
        this.embedConstructorService = embedConstructorService;
        this.languageSelectService = languageSelectService;
        this.functionalSwitcher = functionalSwitcher;
        this.diceRollerService = diceRollerService;
        this.settingsService = settingsService;
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
            case "gatekeeper" -> channelGatekeeperService.gatekeeper(event);
            case "constructor" -> embedConstructorService.constructor(event);
            case "select_language" -> languageSelectService.languageSelect(event);
            case "setup" -> settingsService.setup(event);
            default -> event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
        }

    }
}
