package horse.boo.bot.config;

import horse.boo.bot.Lyra;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class CommandsConfig extends BotConfig {

    public void ss(JDA jda) {

        // These commands might take a few minutes to be active after creation/update/delete
        CommandListUpdateAction commands = jda.updateCommands();

        // Simple reply commands
        commands.addCommands(
                Commands.slash("say", "Makes the bot say what you tell it to")
                        .addOption(OptionType.STRING, "content", "What the bot should say", true) // you can add required options like this too
        );
        // Send the new set of commands to discord, this will override any existing global commands with the new set provided here
        commands.queue();

    }

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        // Only accept commands from guilds
        if (event.getGuild() == null)
            return;
        switch (event.getName()) {
            case "say":
                say(event, event.getOption("content").getAsString()); // content is required so no null-check here
                break;
            default:
                event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
        }
    }

    public void say(SlashCommandInteractionEvent event, String content) {
        event.reply(content).queue(); // This requires no permissions!
    }

}
