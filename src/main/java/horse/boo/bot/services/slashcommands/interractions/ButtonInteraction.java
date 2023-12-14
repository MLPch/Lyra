package horse.boo.bot.services.slashcommands.interractions;

import horse.boo.bot.database.enums.Languages;
import horse.boo.bot.database.repository.ConfigRepository;
import horse.boo.bot.database.table.ConfigsTable;
import horse.boo.bot.services.slashcommands.ChannelGatekeeperService;
import horse.boo.bot.services.slashcommands.LanguageSelectService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.EnumSet;
import java.util.concurrent.TimeUnit;

import static net.dv8tion.jda.api.Permission.*;

@Component
public class ButtonInteraction extends ListenerAdapter {

    private final Logger languageSelectLogger = LoggerFactory.getLogger(LanguageSelectService.class);
    private final Logger gatekeeperLogger = LoggerFactory.getLogger(ChannelGatekeeperService.class);
    private final ConfigRepository configRepository;
    public static EnumSet<Permission> textChannelUserPermission = initTextUserPermission();

    @NotNull
    private static EnumSet<Permission> initTextUserPermission() {
        var perm = Permission.getRaw(MESSAGE_ADD_REACTION, MESSAGE_SEND,
                MESSAGE_EMBED_LINKS, MESSAGE_ATTACH_FILES, MESSAGE_EXT_EMOJI, MESSAGE_EXT_STICKER,
                MESSAGE_HISTORY, USE_APPLICATION_COMMANDS,
                CREATE_PUBLIC_THREADS, MESSAGE_SEND_IN_THREADS, VIEW_CHANNEL);
        return Permission.getPermissions(perm);
    }

    public ButtonInteraction(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        Guild guild = event.getGuild();
        ConfigsTable config = configRepository.findByGuildId(guild.getIdLong()).orElseGet(() -> new ConfigsTable(guild));
        String componentId = event.getComponentId();
        Languages lang;
        EmbedBuilder eb = new EmbedBuilder();
        long mentionSubscribeChannelId = event.getMessage().getMentions().getChannels().get(0).getIdLong();


        switch (componentId) {
            case "language.english" -> {
                eb.setTitle("The English language of the customizer is selected.").setColor(Color.blue);
                lang = Languages.ENGLISH;
                langUpdateAndSendMessage(event, config, lang, eb, guild);
            }
            case "language.russian" -> {
                eb.setTitle("Выбран русский язык наёстройщика.").setColor(Color.green);
                lang = Languages.RUSSIAN;
                langUpdateAndSendMessage(event, config, lang, eb, guild);
            }
            case "language.ukrainian" -> {
                eb.setTitle("Обрано українську мову настроювача.").setColor(Color.yellow);
                lang = Languages.UKRAINE;
                langUpdateAndSendMessage(event, config, lang, eb, guild);
            }
            case "language.chinese" -> {
                eb.setTitle("选择定制器的中文语言。").setColor(Color.red);
                lang = Languages.CHINESE;
                langUpdateAndSendMessage(event, config, lang, eb, guild);
            }
            case "subscribe.channel" -> {
                subscribeMember(event, guild, config, mentionSubscribeChannelId);
            }
            case "unsubscribe.channel" -> {
                unsubscribeMember(event, guild, config, mentionSubscribeChannelId);
            }
        }
    }

    private void unsubscribeMember(@NotNull ButtonInteractionEvent event, @NotNull Guild guild,
                                   @NotNull ConfigsTable config, long mentionSubscribeChannelId) {
        guild.getTextChannelById(mentionSubscribeChannelId)
                .getPermissionContainer()
                .upsertPermissionOverride(event.getMember())
                .setPermissions(null, textChannelUserPermission).queue();

        event.reply("You have been successfully unsubscribed from the channel!" + "\n"
                + guild.getTextChannelById(mentionSubscribeChannelId).getAsMention()).setEphemeral(true).queue();

        config.sendInLogChannel(guild, "The %s has been unsubscribed from %s"
                .formatted(event.getMember().getAsMention(),
                        guild.getTextChannelById(mentionSubscribeChannelId).getAsMention()));

        gatekeeperLogger.info("The %s has been unsubscribed from %s in guild %s"
                .formatted(event.getMember().getUser().toString(),
                        guild.getTextChannelById(mentionSubscribeChannelId).toString(), guild.toString()));
    }

    private void subscribeMember(@NotNull ButtonInteractionEvent event, @NotNull Guild guild,
                                 @NotNull ConfigsTable config, long mentionSubscribeChannelId) {
        guild.getTextChannelById(mentionSubscribeChannelId)
                .getPermissionContainer()
                .upsertPermissionOverride(event.getMember())
                .setPermissions(textChannelUserPermission, null).queue();

        event.reply("You have been successfully subscribed to the channel!" + "\n"
                + guild.getTextChannelById(mentionSubscribeChannelId).getAsMention()).setEphemeral(true).queue();

        config.sendInLogChannel(guild, "The %s has been subscribed to %s"
                .formatted(event.getMember().getAsMention(),
                        guild.getTextChannelById(mentionSubscribeChannelId).getAsMention()));

        gatekeeperLogger.info("The %s has been subscribed to %s in guild %s"
                .formatted(event.getMember().getUser().toString(),
                        guild.getTextChannelById(mentionSubscribeChannelId).toString(), guild.toString()));
    }

    private void langUpdateAndSendMessage(@NotNull ButtonInteractionEvent event, @NotNull ConfigsTable config,
                                          @NotNull Languages lang, @NotNull EmbedBuilder eb, Guild guild) {
        event.getMessage().delete().queue();
        event.getChannel().sendMessageEmbeds(eb.build()).delay(7, TimeUnit.SECONDS).flatMap(Message::delete).queue();
        config.setBotLanguage(lang.getLanguage());
        event.deferEdit().queue();
        configRepository.save(config);
        languageSelectLogger.info("The bot language is set in the guild (%s(%d)): %s"
                .formatted(guild.getName(), guild.getIdLong(), lang));
    }
}
