package horse.boo.bot.setup;

import horse.boo.bot.setup.config.BotSystemChannelService;
import horse.boo.bot.setup.config.GuildConfigService;
import horse.boo.bot.setup.steps.Languages;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class InitialSetupEvent extends ListenerAdapter {
    //TODO: Вынести сюда все шаги по настройке.
    public InitialSetupEvent() {
    }

    BotSystemChannelService botSystemChannelService = new BotSystemChannelService();
    GuildConfigService gcs = new GuildConfigService();
    Languages language = Languages.ENGLISH;

    @SneakyThrows
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        var guild = event.getGuild();
        var msg = event.getMessage();
        var systemLyraChannel = botSystemChannelService.getSystemLyraChannel(guild);

        if (msg.getContentRaw().equals("/lyrastart")) {
            System.out.println("lyrastart");
            var cfg = gcs.getActualGuildConfig(guild);
            cfg.setBotId("cx235r2xc3x");
            gcs.updateGuildConfigMessage(guild, cfg);

            guild.getTextChannelsByName("test", true).get(0).sendMessage(language.getLanguage()).complete();
        }
    }

    public void start(SlashCommandInteractionEvent event, String content) {


        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor("Добро пожаловать в мастер настройки!");
        eb.addField("Для продолжения - пожалуйста выберите язык.", "- Русский\n" +
                "- Английский\n" +
                "- Китайский\n" +
                "- Украинский", true);
        eb.setColor(Color.magenta);

        event.reply(content).setEmbeds(eb.build())
                .addActionRow(
                        Button.danger("language.russian", "Русский"),
                        Button.danger("language.english", "Английский"))
                .queue();
    }

    public void badrequest(SlashCommandInteractionEvent event) {

        event.reply("kekw")
                .addActionRow(
                        Button.secondary("3", "11"),
                        Button.danger("4", "22"))
                .queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        var commands = event.getJDA().updateCommands();
        commands.addCommands(
                Commands.slash("start", "anyDescription")
                        .addOption(STRING, "content", "What the bot should say", true)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
        );
        commands.queue();

        // Only accept commands from guilds
        if (event.getGuild() == null)
            return;
        switch (event.getName()) {
            case "start": // 2 stage command with a button prompt
                start(event, event.getOption("content").getAsString());
                break;
            case "badrequest": // 2 stage command with a button prompt
                badrequest(event);
                break;
            default:
                event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        switch (event.getComponentId()) {
            case "language.russian":
                event.getMessage().delete().queue();
                EmbedBuilder eb1 = new EmbedBuilder()
                        .setTitle("Выбран русский язык настройщика.")
                        .setColor(Color.red);
                event.getChannel()
                        .sendMessageEmbeds(eb1.build())
                        .delay(7, TimeUnit.SECONDS)
                        .flatMap(Message::delete)
                        .queue();
                language = Languages.RUSSIAN;
                event.deferEdit().queue();
                break;
            case "language.english":
                event.getMessage().delete().queue();
                EmbedBuilder eb2 = new EmbedBuilder()
                        .setTitle("The English language of the customizer is selected.")
                        .setColor(Color.blue);
                event.getChannel()
                        .sendMessageEmbeds(eb2.build())
                        .delay(7, TimeUnit.SECONDS)
                        .flatMap(Message::delete)
                        .queue();
                language = Languages.ENGLISH;
                event.deferEdit().queue();
                break;
        }
    }
}

