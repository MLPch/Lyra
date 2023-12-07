package horse.boo.bot.services.slashcommands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class ChannelGatekeeperService extends ListenerAdapter {

    public void gatekeeper(@NotNull SlashCommandInteractionEvent event) {
        StringBuilder sb = new StringBuilder();

        sb.append("# ").append(event.getOption("gatekeeper_channel").getAsChannel().getName()).append("\n")
                .append(event.getOption("gatekeeper_channel").getAsChannel().getAsMention()).append("\n");

        if (event.getOption("gatekeeper_description") != null) {
            sb.append("### ").append(event.getOption("gatekeeper_description").getAsString());
        }

        event.reply(sb.toString()).addActionRow(
                        Button.primary("subscribe.channel", "Subscribe"),
                        Button.danger("unsubscribe.channel", "Unsubscribe"))
                .queue();
    }

}
