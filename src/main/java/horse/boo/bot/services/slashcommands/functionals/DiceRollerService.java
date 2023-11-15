package horse.boo.bot.services.slashcommands.functionals;

import horse.boo.bot.database.repository.ConfigRepository;
import horse.boo.bot.database.repository.LocaleRepository;
import horse.boo.bot.database.table.ConfigsTable;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.Objects;

@Component
public class DiceRollerService extends ListenerAdapter {

    private final Logger logger = LoggerFactory.getLogger(DiceRollerService.class);
    private final ConfigRepository configRepository;
    private final LocaleRepository localeRepository;

    public DiceRollerService(ConfigRepository configRepository, LocaleRepository localeRepository) {
        this.configRepository = configRepository;
        this.localeRepository = localeRepository;
    }

    public void roll(@NotNull SlashCommandInteractionEvent event, String diceRow) {
        Guild guild = event.getGuild();
        ConfigsTable config = configRepository.getConfigByGuildId(guild.getIdLong());
        if (config.isFunctionUnrelatedDeleter() && configRepository.getConfigByGuildId(event.getGuild().getIdLong()).isFunctionDiceRoller()) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.WHITE);
            if (diceRow.contains("custom")) {

                eb.setTitle("Select the desired dice or click \"My dice\" to calculate your variant");

                event.replyEmbeds(eb.build())
                        .addActionRow(
                                Button.primary("d20", "D20"),
                                Button.primary("d12", "D12"),
                                Button.primary("d10", "D10"),
                                Button.secondary("d2", "Coin"))
                        .addActionRow(
                                Button.primary("d8", "D8"),
                                Button.primary("d6", "D6"),
                                Button.primary("d4", "D4"),
                                Button.secondary("custom_dice", "My dice"))
                        .setEphemeral(true).queue();
            } else {
                if (validCheck(event, null, null, diceRow)) {
                    event.reply("One or more values are not valid").queue();
                    return;
                }
                String[] values = diceRow.split("[Dd]");
                if (validCheck(event, values[0], values[1], null)) {
                    event.reply("One or more values are not valid").queue();
                    return;
                }

                var diceAndEdges = getTrueDicesAndEdges(null, null, diceRow);
                int dices = diceAndEdges[0];
                int edges = diceAndEdges[1];

                eb.setTitle(dices + "d" + edges + ":");
                eb.addField(" ", multiDiceAsString(dices, edges), false);

                event.reply(Objects.requireNonNull(event.getMember()).getAsMention()).addEmbeds(eb.build()).queue();
            }


        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String componentId = event.getComponentId();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.WHITE);

        switch (componentId) {
            case "d20", "d12", "d10", "d8", "d6", "d4", "d2" -> {
                eb.setTitle(componentId + " = " + randomizer(Integer.parseInt(componentId.substring(1))));
                event.reply(Objects.requireNonNull(event.getMember()).getAsMention()).addEmbeds(eb.build()).queue();
            }
            case "custom_dice" -> event.replyModal(getDiceModal()).queue();
        }
    }

    @NotNull
    private Integer randomizer(Integer initialValue) {
        return (int) (Math.random() * initialValue) + 1;
    }

    @NotNull
    private String multiDiceAsString(Integer dices, Integer edges) {
        StringBuilder multiDiceString = new StringBuilder();
        String splitter = " | ";
        for (int i = 0; i < dices; i++) {
            if (dices == 1) {
                splitter = " ";
            }
            if (i % 10 == i / 10 && dices > 10) {
                multiDiceString.append("\n----------------------\n");
            }
            String value = randomizer(edges) + splitter;
            multiDiceString.append(value);
        }
        if (dices > 10) {
            multiDiceString.append("\n----------------------");
        }
        return multiDiceString.toString();
    }

    @NotNull
    private Modal getDiceModal() {
        TextInput dices = TextInput.create("dice_text_input", "DICE COUNT", TextInputStyle.SHORT)
                .setPlaceholder("From 1 to 100")
                .setRequiredRange(1, 3)
                .setRequired(true)
                .build();
        TextInput edges = TextInput.create("edge_text_input", "EDGES", TextInputStyle.SHORT)
                .setPlaceholder("From 1 to 100")
                .setRequiredRange(1, 3)
                .setRequired(true)
                .build();

        return Modal.create("dice_modmail", "CUSTOM DICE").addComponents(
                ActionRow.of(dices),
                ActionRow.of(edges)
        ).build();
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (event.getModalId().equals("dice_modmail")) {
            String fieldName = " ";
            if (validCheck(event, event.getInteraction().getValue("dice_text_input").getAsString(),
                    event.getInteraction().getValue("edge_text_input").getAsString(), null)) {
                event.reply("One or more values are not valid").queue();
                return;
            }
            var diceAndEdges = getTrueDicesAndEdges(event.getInteraction().getValue("dice_text_input")
                    .getAsString(), event.getInteraction().getValue("edge_text_input").getAsString(), null);
            var dices = diceAndEdges[0];
            var edges = diceAndEdges[1];
            EmbedBuilder eb = new EmbedBuilder();
            eb.setColor(Color.WHITE);
            eb.setTitle(dices + "d" + edges + ":");
            eb.addField(fieldName, multiDiceAsString(dices, edges), false);

            event.reply(Objects.requireNonNull(event.getMember()).getAsMention()).addEmbeds(eb.build()).queue();
        }
    }

    private boolean validCheck(@NotNull Interaction event, @Nullable String diceAsString,
                               @Nullable String edgeAsString, @Nullable String diceRow) {
        boolean check = true;
        int dices = 0;
        int edges = 0;
        try {
            if (diceAsString == null && edgeAsString == null) {
                int[] dicesAndEdges = getTrueDicesAndEdges(null, null, diceRow);
                dices = dicesAndEdges[0];
                edges = dicesAndEdges[1];
            } else {
                int[] dicesAndEdges = getTrueDicesAndEdges(diceAsString, edgeAsString, null);
                dices = dicesAndEdges[0];
                edges = dicesAndEdges[1];
            }


        } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
            logger.error(("""
                    \n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                                Exception: %s
                                Values: %sd%s
                                Initiator: %s
                                Guild: %s
                                Channel: %s
                    !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!""")
                    .formatted(
                            ex.getMessage(),
                            dices,
                            edges,
                            Objects.requireNonNull(event.getMember()).getUser().toString(),
                            Objects.requireNonNull(event.getGuild()).toString(),
                            Objects.requireNonNull(event.getChannel()).toString()
                    ));
            check = false;
        }
        return !check;
    }

    @NotNull
    private int[] getTrueDicesAndEdges(@Nullable String dicesAsString, @Nullable String edgesAsString, @Nullable String diceRow) {
        int dices = 0;
        int edges = 0;
        int[] result = new int[2];
        if ((dicesAsString == null || edgesAsString == null) && diceRow != null) {
            dices = Integer.parseInt(diceRow.split("[dD]")[0]);
            edges = Integer.parseInt(diceRow.split("[dD]")[1]);
        } else if (dicesAsString == null && edgesAsString != null) {
            dices = 2;
            edges = Integer.parseInt(edgesAsString);
        } else if (dicesAsString != null && edgesAsString == null) {
            dices = Integer.parseInt(dicesAsString);
            edges = 1;
        }
        if (dicesAsString != null && edgesAsString != null) {
            dices = Integer.parseInt(dicesAsString);
            edges = Integer.parseInt(edgesAsString);
        }
        if (dices < 1) dices = 1;
        if (dices > 100) dices = 100;
        if (edges < 2) edges = 2;
        if (edges > 100) edges = 100;

        result[0] = dices;
        result[1] = edges;
        return result;
    }


}
