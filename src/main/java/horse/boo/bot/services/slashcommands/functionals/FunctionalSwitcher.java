package horse.boo.bot.services.slashcommands.functionals;

import horse.boo.bot.database.repository.IgnoreChannelRepository;
import horse.boo.bot.database.table.IgnoreChannelTable;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class FunctionalSwitcher extends ListenerAdapter {

    private final IgnoreChannelRepository ignoreChannel;

    public FunctionalSwitcher(IgnoreChannelRepository ignoreChannel) {
        this.ignoreChannel = ignoreChannel;
    }

    public void disableFunctionals(@NotNull SlashCommandInteractionEvent event) {
        var guild = event.getGuild();
        var channel = Objects.requireNonNull(event.getOption("disable_functionals_from_channel")).getAsChannel();
        IgnoreChannelTable ignoreChannelTable = new IgnoreChannelTable();
        assert guild != null;
        ignoreChannelTable.setGuildId(guild.getIdLong());
        ignoreChannelTable.setChannelId(channel.getIdLong());
        ignoreChannel.save(ignoreChannelTable);
        event.reply("Success disable in " + channel.getName()).complete();
    }

    public void enableFunctionals(@NotNull SlashCommandInteractionEvent event) {
        var channel = Objects.requireNonNull(event.getOption("enable_functionals_from_channel")).getAsChannel();
        ignoreChannel.deleteByChannelId(channel.getIdLong());
        event.reply("Success enable in " + channel.getName()).complete();
    }
}
