package horse.boo.bot.setup;


import horse.boo.bot.database.dao.LocaleDAO;
import horse.boo.bot.setup.config.GuildConfig;
import horse.boo.bot.setup.config.GuildConfigService;
import horse.boo.bot.setup.steps.Languages;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static net.dv8tion.jda.api.interactions.commands.OptionType.CHANNEL;
import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class InitialSetupEvent extends ListenerAdapter {
    //TODO: Вынести сюда все шаги по настройке.

    @Autowired
    LocaleDAO localeDAO;
    GuildConfigService gcs = new GuildConfigService();
    Languages language;
    GuildConfig cfg;


    @SneakyThrows
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        var guild = event.getGuild();
        var msg = event.getMessage();
        cfg = gcs.getActualGuildConfig(guild);
        language = cfg.getBotLanguage();
        if (msg.getContentRaw().equals("1")) { //for tests

            //TODO: 3 строчки ниже пригодятся при заделке интерфейса смены текстов
//            EntitySelectMenu menu = EntitySelectMenu.create("menu:class", EntitySelectMenu.SelectTarget.CHANNEL)
//            .setPlaceholder("Выбурите канал для логов").setRequiredRange(1,1) // must select exactly one
//                    .build();

            System.out.println("lyrastart");
            cfg.setBotId(event.getGuild().getSelfMember().getIdLong());
            cfg.setBotLanguage(getActualLanguage());
//            guild.getTextChannelsByName("test", true).get(0).sendMessage(language.getLanguage()).addActionRow().complete();
//            guild.getTextChannelsByName("test", true).get(0).sendMessage(getActualLanguage().toString()).addActionRow(menu).complete();
            guild.getTextChannelsByName("test", true).get(0)
                    .sendMessage(localeDAO.getLocaleByName("greetings_default_fieldName")
                            .getLocaleStringByLanguage(getActualLanguage())).complete();

        }

    }

    //TODO: Убрать контент у старта при релизе
    public void start(@NotNull SlashCommandInteractionEvent event, String content) {


        EmbedBuilder eb = new EmbedBuilder();
        eb.setAuthor("Добро пожаловать в мастер настройки!");
        eb.addField("Для продолжения - пожалуйста выберите язык.", "- Английский\n" + "- Русский\n" + "- Украинский\n" + "- Китайский", true);
        eb.setColor(Color.magenta);

        event.reply(content).setEmbeds(eb.build()).addActionRow(
                        Button.danger("language.english", "Английский"),
                        Button.danger("language.russian", "Русский"),
                        Button.danger("language.ukrainian", "Украинский"),
                        Button.danger("language.china", "Китайский"))
                .queue();
    }

    @SneakyThrows
    public void setup(@NotNull SlashCommandInteractionEvent event, String content) {
        var guild = event.getGuild();
        cfg = gcs.getActualGuildConfig(guild);
        cfg.setLogsChannelId(event.getOption("logs").getAsChannel().getIdLong());
        cfg.setJoinAndLeaveChannelId(event.getOption("join-leave").getAsChannel().getIdLong());
        cfg.setBotReadyChannelId(event.getOption("bot-ready").getAsChannel().getIdLong());
        cfg.setOfftopEmoteCount(event.getOption("offtop-count").getAsInt());
        gcs.updateGuildConfigMessage(guild, cfg);

        String setupLog =
                "\n✎ Канал __" + event.getOption("logs").getAsChannel().getName()
                        + "__ (" + event.getOption("logs").getAsChannel().getIdLong()
                        + ") установлен как канал для логов" +
                "\n✎ Канал __" + event.getOption("join-leave").getAsChannel().getName()
                        + "__ (" + event.getOption("join-leave").getAsChannel().getIdLong()
                        + ") установлен как канал для встречи пользователей входящих на сервер и для оповещений об их уходе" +
                "\n✎ Канал __" + event.getOption("bot-ready").getAsChannel().getName()
                        + "__ (" + event.getOption("bot-ready").getAsChannel().getIdLong()
                        + ") установлен как канал для постинга сообщений об обновлениях бота" +
                "\n✎ Для удаления сообщения через реакции теперь нужно __" + event.getOption("offtop-count").getAsString()
                        + "__ реакций";


        event.reply(content).setEphemeral(true).addContent(setupLog).queue();
        System.out.println(setupLog);
    }

    @SneakyThrows
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        SlashCommandData start = new CommandDataImpl("start", "anyDescriptionStart")
                .addOption(STRING, "content", "ТЕСТОВАЯ КОМАНДА", true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));


        SlashCommandData setup = new CommandDataImpl("setup", "Меню настройки бота")
                .addOption(CHANNEL, "logs", "Канал в который будут лететь логи:", true)
                .addOption(CHANNEL, "join-leave", "Канал в котором бот будет приветствовать и прощаться с пользователями", true)
                .addOption(CHANNEL, "bot-ready", "Канал в который будут падать сообщения об обновлениях бота:", true)
                .addOption(STRING, "offtop-count", "Кол-во реакций необходимых для удаления поста", true)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));

        var commands = event.getJDA().updateCommands();
        commands.addCommands(setup);
        commands.addCommands(start);
        commands.queue();

        // Only accept commands from guilds
        if (event.getGuild() == null) return;
        switch (event.getName()) {
            case "start": // 2 stage command with a button prompt
                start(event, event.getOption("content").getAsString());
                break;
            case "setup":
                setup(event, "");
                break;
            default:
                event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
                break;
        }

    }

    @SneakyThrows
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        var guild = event.getGuild();
        String componentId = event.getComponentId();
        switch (componentId) {
            case "language.english":
                event.getMessage().delete().queue();
                EmbedBuilder eb2 = new EmbedBuilder().setTitle("The English language of the customizer is selected.").setColor(Color.blue);
                event.getChannel().sendMessageEmbeds(eb2.build()).delay(7, TimeUnit.SECONDS).flatMap(Message::delete).queue();
                language = Languages.ENGLISH;
                updateBotLanguage(guild, getActualLanguage());
                event.deferEdit().queue();
                break;
            case "language.russian":
                event.getMessage().delete().queue();
                EmbedBuilder eb1 = new EmbedBuilder().setTitle("Выбран русский язык настройщика.").setColor(Color.red);
                event.getChannel().sendMessageEmbeds(eb1.build()).delay(7, TimeUnit.SECONDS).flatMap(Message::delete).queue();
                language = Languages.RUSSIAN;
                updateBotLanguage(guild, getActualLanguage());
                event.deferEdit().queue();
                break;
            case "language.ukrainian":
                event.getMessage().delete().queue();
                EmbedBuilder eb3 = new EmbedBuilder().setTitle("Обрано українську мову настроювача.").setColor(Color.blue);
                event.getChannel().sendMessageEmbeds(eb3.build()).delay(7, TimeUnit.SECONDS).flatMap(Message::delete).queue();
                language = Languages.UKRAINE;
                updateBotLanguage(guild, getActualLanguage());
                event.deferEdit().queue();
                break;
            case "language.china":
                event.getMessage().delete().queue();
                EmbedBuilder eb4 = new EmbedBuilder().setTitle("选择定制器的中文语言。").setColor(Color.blue);
                event.getChannel().sendMessageEmbeds(eb4.build()).delay(7, TimeUnit.SECONDS).flatMap(Message::delete).queue();
                language = Languages.CHINA;
                updateBotLanguage(guild, getActualLanguage());
                event.deferEdit().queue();
                break;
        }
    }

    public Languages getActualLanguage() {
        return language;
    }

    private void updateBotLanguage(Guild guild, Languages language) throws IOException {
        var cfg = gcs.getActualGuildConfig(guild);
        cfg.setBotLanguage(language);
        gcs.updateGuildConfigMessage(guild, cfg);
    }
}

