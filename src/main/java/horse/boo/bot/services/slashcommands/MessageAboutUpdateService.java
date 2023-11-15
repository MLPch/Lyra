package horse.boo.bot.services.slashcommands;

import horse.boo.bot.database.repository.ConfigRepository;
import horse.boo.bot.database.table.ConfigsTable;
import horse.boo.bot.services.slashcommands.functionals.DiceRollerService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Objects;

@Component
public class MessageAboutUpdateService extends ListenerAdapter {

    private final Logger logger = LoggerFactory.getLogger(DiceRollerService.class);
    private final ConfigRepository configRepository;

    public MessageAboutUpdateService(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    public void sendUpdate(@NotNull SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        var allConfigs = configRepository.findAllConfigs();

        for (ConfigsTable configsTable : allConfigs) {
            if (event.getJDA().getGuildById(configsTable.getGuildId()) != null) {

                Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(configsTable.getGuildId()))
                                .getTextChannelById(configsTable.getLogChannelId()))
                        .sendMessageEmbeds(getMessageEmbed(guild, "log")).queue();

                Objects.requireNonNull(Objects.requireNonNull(event.getJDA().getGuildById(configsTable.getGuildId()))
                                .getTextChannelById(configsTable.getBotInfoChannelId()))
                        .sendMessageEmbeds(getMessageEmbed(guild, "bot")).queue();

            }
        }

        event.reply("☑").setEphemeral(true).queue();
        logger.info("The update message has been sent successfully!");
    }


    @NotNull
    private static MessageEmbed getMessageEmbed(Guild guild, @NotNull String embedType) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Last Update: 24.11.2023");
        String name = "What's new:";
        String value = getValueForUsers();
        if (embedType.equals("log")) {
            value = getValueForLogs();
        }
        eb.addField(name, value, false);
        eb.setFooter("Lyra_Heartstrings   Ver: 5.5.7");
        eb.setImage(guild.getSelfMember().getAvatarUrl());
        eb.setColor(Color.GREEN).build();
        return eb.build();
    }

    @NotNull
    @Contract(pure = true)
    private static String getValueForLogs() {
        return """
                * Fix old bugs
                * Add new bugs
                * Added new update command for admins (The "send_update" command sends update messages to the information channel from the bot and to the logs channel)
                """;
    }

    @NotNull
    @Contract(pure = true)
    private static String getValueForUsers() {
        return """
                * Fix old bugs
                * Add new bugs
                * Added functionality to capture the government of Africa! =)
                """;
    }
}
