package horse.boo.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.util.EnumSet;
import java.util.concurrent.TimeUnit;
import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

public class SlashBotExample extends ListenerAdapter {
    public static void main(String[] args) {
        JDA jda = JDABuilder.createLight("MTAxOTIxODIxOTM2MzQ3OTY1Mg.GjjkZV.p8R4ap4ZpdHXLulNuY_9kMDOozvL-mhEXDAGDQ", EnumSet.noneOf(GatewayIntent.class)) // slash commands don't need any intents
                .addEventListeners(new SlashBotExample())
                .build();

        // These commands might take a few minutes to be active after creation/update/delete
        CommandListUpdateAction commands = jda.updateCommands();

        // Simple reply commands
        commands.addCommands(
                Commands.slash("say", "Makes the bot say what you tell it to")
                        .addOption(STRING, "content", "What the bot should say", true) // you can add required options like this too
        );
        // Send the new set of commands to discord, this will override any existing global commands with the new set provided here
        commands.queue();
    }


    @Override
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

//    @Override
//    public void onButtonInteraction(ButtonInteractionEvent event) {
//        String[] id = event.getComponentId().split(":"); // this is the custom id we specified in our button
//        String authorId = id[0];
//        String type = id[1];
//        // Check that the button is for the user that clicked it, otherwise just ignore the event (let interaction fail)
//        if (!authorId.equals(event.getUser().getId()))
//            return;
//        event.deferEdit().queue(); // acknowledge the button was clicked, otherwise the interaction will fail
//
//        MessageChannel channel = event.getChannel();
//        switch (type) {
//            case "prune":
//                int amount = Integer.parseInt(id[2]);
//                event.getChannel().getIterableHistory()
//                        .skipTo(event.getMessageIdLong())
//                        .takeAsync(amount)
//                        .thenAccept(channel::purgeMessages);
//                // fallthrough delete the prompt message with our buttons
//            case "delete":
//                event.getHook().deleteOriginal().queue();
//        }
//    }

    public void say(SlashCommandInteractionEvent event, String content) {
        event.reply(content).queue(); // This requires no permissions!
    }

}
