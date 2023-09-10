//package deprecated.events;
//
//
//import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
//import net.dv8tion.jda.api.hooks.ListenerAdapter;
//import net.dv8tion.jda.api.interactions.commands.OptionType;
//import net.dv8tion.jda.api.interactions.commands.build.Commands;
//import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
//
//@Deprecated
//public class SlashCommandEvent extends ListenerAdapter {
//
//@Override
//public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
//        // Simple reply commands
//        CommandListUpdateAction commands = event.getJDA().updateCommands();
//        commands.addCommands(
//                Commands.slash("say", "Makes the bot say what you tell it to")
//                        .addOption(OptionType.STRING, "content", "What the bot should say", true) // you can add required options like this too
//        );
//        commands.queue();
//
//        // Only accept commands from guilds
//        if (event.getGuild() == null)
//            return;
//        switch (event.getName()) {
//            case "say":
//                say(event, event.getOption("content").getAsString()); // content is required so no null-check here
//                break;
//            default:
//                event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
//        }
//
//    }
//
//    public void say(SlashCommandInteractionEvent event, String content) {
//        event.reply(content).queue(); // This requires no permissions!
//    }
//
//}
//
