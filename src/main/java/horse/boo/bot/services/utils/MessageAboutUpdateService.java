package horse.boo.bot.services.utils;

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
        eb.setTitle("Last Update: 6.12.2023");
        String name = "What's new:";
        String value = getValueForUsers();
        if (embedType.equals("log")) {
            value = getValueForLogs();
        }
        eb.addField(name, value, false);
        eb.setFooter("Lyra_Heartstrings   Ver: 5.6.8");
        eb.setImage(guild.getSelfMember().getAvatarUrl());
        eb.setColor(Color.GREEN).build();
        return eb.build();
    }

    @NotNull
    @Contract(pure = true)
    private static String getValueForLogs() {
        return """
                * The "/gatekeeper" functionality has been added, it allows the server administrator to create a message for users with two Subscribe-Unsubscribe buttons that grant or revoke access to the selected channel
                * It is Wednesday, my dudes
                * Added new bugs
                """;
    }

    @NotNull
    @Contract(pure = true)
    private static String getValueForUsers() {
        return """
                * The "/gatekeeper" functionality has been added, it allows the server administrator to create a message for users with two Subscribe-Unsubscribe buttons that grant or revoke access to the selected channel
                * It is Wednesday, my dudes
                * Added new bugs
                """;
    }
}
