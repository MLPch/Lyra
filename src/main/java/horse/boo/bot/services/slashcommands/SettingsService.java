package horse.boo.bot.services.slashcommands;


import horse.boo.bot.database.enums.Languages;
import horse.boo.bot.database.repository.ConfigRepository;
import horse.boo.bot.database.repository.LocaleRepository;
import horse.boo.bot.database.table.ConfigsTable;
import horse.boo.bot.services.enums.Options;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static horse.boo.bot.services.enums.Options.*;

@Component
public class SettingsService extends ListenerAdapter {
    //TODO: Вынести сюда все шаги по настройке.

    private final Logger logger = LoggerFactory.getLogger(SettingsService.class);
    private final ConfigRepository configRepository;
    private final LocaleRepository localeRepository;

    public SettingsService(ConfigRepository configRepository,
                           LocaleRepository localeRepository) {
        this.configRepository = configRepository;
        this.localeRepository = localeRepository;
    }




    private <T> void exec(Consumer<T> configSetup, Options adminChannel, SlashCommandInteractionEvent event, EmbedBuilder eb) {
        if (event.getOption(adminChannel.optionName) != null) {
            T value = (T) adminChannel.mapping.apply(event.getOption(adminChannel.optionName));
            configSetup.accept(value);
            eb.addField("", adminChannel.text.replace("%%", value + ""), false);
        }
    }

    /**
     * @param event - Срабатывает при вызове команды "setup" и вводе одного или нескольких параметров для изменения.
     *              Возвращает эмбед со списком изменённых параметров конфига.
     *              Выводит информацию в канал логов в виде отдельных эмбедов для каждого параметра и в логи бота.
     */
    public void setup(@NotNull SlashCommandInteractionEvent event) {
        var guild = event.getGuild();
        if (guild == null) {
            throw new IllegalStateException("Processing slash commands outside guild is not possible" + event);
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("BOT SETTINGS UPDATED");

        ConfigsTable config = configRepository.findByGuildId(guild.getIdLong()).orElseGet(() -> new ConfigsTable(guild));

        config.setGuildId(guild.getIdLong());
        config.setGuildName(guild.getName());
        //todo get list of added options and iterate on it will be simpler (?)
        exec(config::setAdminChannelId, ADMIN_CHANNEL, event, eb);
        config.setBotId(guild.getSelfMember().getIdLong());
        exec(config::setUnrelatedEmoteCount, UNRELATED_COUNT, event, eb);
        exec(config::setUnrelatedEmoteId, UNRELATED_EMOTE, event, eb);
        exec(config::setWelcomeChannelId, JOIN_CHANNEL, event, eb);
        exec(config::setGoodbyeChannelId, LEAVE_CHANNEL, event, eb);
        exec(config::setBotInfoChannelId, BOT_INFO_CHANNEL, event, eb);
        exec(config::setUnrelatedDeleteTimeSec, UNRELATED_DELETE, event, eb);

        exec(config::setFunctionMusicPlayer, MUSIC_PLAYER, event, eb);
        exec(config::setFunctionRememberingRoles, REMEMBERING_ROLES, event, eb);
        exec(config::setFunctionDiceRoller, ROLL_THE_DICE, event, eb);
        exec(config::setFunctionUnrelatedDeleter, UNRELATED_REMOVER, event, eb);

        Stream.of(MUSIC_PLAYER, REMEMBERING_ROLES)
                .forEach(e -> unimplementedFunctional(e, event, eb));

        eb.setColor(Color.YELLOW);
        eb.setTimestamp(OffsetDateTime.now());

        configRepository.save(config);
        event.replyEmbeds(eb.build()).setEphemeral(true).queue();
        Objects.requireNonNull(guild.getTextChannelById(config.getLogChannelId())).sendMessageEmbeds(eb.build()).queue();

        setupLog(event, eb);
    }


    /**
     * @param option
     * @param event
     * @param eb     (EmbedBuilder) - билдер в который будет добавлена строка.
     */
    private void unimplementedFunctional(Options option, @NotNull SlashCommandInteractionEvent event, EmbedBuilder eb) {
        //TODO: Убрать заглушку реализовав функционалы
        if (event.getOption(option.optionName) != null) {
            if (Objects.requireNonNull(event.getOption(option.optionName)).getAsBoolean())
                eb.setAuthor("Attention! At the moment, the functionality " + option.optionName +
                        " does not work. Wait in the next versions.");

        }
    }


    private void setupLog(@NotNull SlashCommandInteractionEvent event, @NotNull EmbedBuilder eb) {
        StringBuilder log = new StringBuilder();
        log.append("CONFIG UPDATED: \n");
        log.append("#################################### \n");
        log.append("######## \n");
        log.append("Guild name: ").append(Objects.requireNonNull(event.getGuild()).getName()).append("\n");
        log.append("Guild id: ").append(event.getGuild().getIdLong()).append("\n");
        log.append("Guild id: ").append(event.getGuild().getIdLong()).append("\n");
        log.append("Update time: ").append(OffsetDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").withZone(ZoneOffset.UTC))).append("\n");
        log.append("######## \n");
        log.append("Has been updated: \n");
        eb.build().getFields().forEach(field -> log.append("*  ").append(field.getValue()).append("\n"));
        log.append("#################################### \n");
        logger.info(log.toString());
    }


}

