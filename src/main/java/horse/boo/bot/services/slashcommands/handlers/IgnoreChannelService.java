package horse.boo.bot.services.slashcommands.handlers;

import horse.boo.bot.database.repository.IgnoreChannelRepository;
import horse.boo.bot.database.table.IgnoreChannelTable;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static net.dv8tion.jda.api.interactions.commands.OptionType.CHANNEL;

@Component
public class IgnoreChannelService extends ListenerAdapter {

    private final IgnoreChannelRepository ignoreChannel;

    public IgnoreChannelService(IgnoreChannelRepository ignoreChannel) {
        this.ignoreChannel = ignoreChannel;
    }

    @NotNull
    public static SlashCommandData disableFunctionalsData() {
        return new CommandDataImpl("disable_functionals_from_channel", "disable_functionals_from_channel")
                .addOption(CHANNEL, "disable_from_channel", "The channel for which you need to disable the functionality:", true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
    }

    @NotNull
    public static SlashCommandData enableFunctionalsData() {
        return new CommandDataImpl("enable_functionals_from_channel", "enable_functionals_from_channel")
                .addOption(CHANNEL, "enable_for_channel", "The channel for which you need to enable the functionality:", true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
    }

    public void disableFunctionals(@NotNull SlashCommandInteractionEvent event) {
        var guild = event.getGuild();
        var channel = Objects.requireNonNull(event.getOption("disable_from_channel")).getAsChannel();
        IgnoreChannelTable ignoreChannelTable = new IgnoreChannelTable();
        ignoreChannelTable.setGuildId(guild.getIdLong());
        ignoreChannelTable.setChannelId(channel.getIdLong());
        ignoreChannel.save(ignoreChannelTable);
        event.reply("Success disable in " + channel.getName()).complete();
    }

    public void enableFunctionals(@NotNull SlashCommandInteractionEvent event) {
        var channel = Objects.requireNonNull(event.getOption("enable_for_channel")).getAsChannel();
        ignoreChannel.deleteByChannelId(channel.getIdLong());
        event.reply("Success enable in " + channel.getName()).complete();
    }
}
