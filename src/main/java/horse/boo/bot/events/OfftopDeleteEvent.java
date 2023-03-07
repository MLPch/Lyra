package horse.boo.bot.events;

import horse.boo.bot.database.dao.LocaleDAO;
import horse.boo.bot.setup.config.GuildConfig;
import horse.boo.bot.setup.config.GuildConfigService;
import horse.boo.bot.setup.steps.Languages;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.time.OffsetDateTime;

public class OfftopDeleteEvent extends ListenerAdapter {

    //    private boolean stopped = true;
    @Autowired
    LocaleDAO localeDAO;

    @Autowired
    GuildConfigService guildConfigService;

    @SneakyThrows
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        GuildConfig guildConfig = guildConfigService.getActualGuildConfig(event.getGuild());
        Languages language = guildConfig.getBotLanguage();
        var offtopEmoteId = guildConfig.getOfftopEmoteId();
        var botId = guildConfig.getBotId();
        var offtopEmoteCount = guildConfig.getOfftopEmoteCount();

        event.getChannel().retrieveMessageById(event.getMessageId()).queue();

        Message msg = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
        User author = msg.getAuthor();
        String stringAbove = localeDAO.getLocaleByName("offtop_default_stringAbove").getLocaleStringByLanguage(language);

        for (MessageReaction react : msg.getReactions()) {
            if (react.getEmoji().asCustom().getIdLong() == offtopEmoteId &&
                    (!(author.getIdLong() == botId)) &&
                    react.getCount() >= offtopEmoteCount) {
                msg.delete().complete();

                String ping = author.getAsMention();

                event.getChannel().sendMessage(stringAbove + ping + "!\n").setEmbeds(offtopEmbed(event)).complete();
                author.openPrivateChannel().queue(channel -> channel.sendMessageEmbeds(offtopEmbed(event)).complete());
            }
        }
    }

    @SneakyThrows
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        GuildConfig guildConfig = guildConfigService.getActualGuildConfig(event.getGuild());
        Languages language = guildConfig.getBotLanguage();
        var offtopDeleteTime = guildConfig.getOfftopDeleteTime();
        var botId = guildConfig.getBotId();
        String stringAbove = localeDAO.getLocaleByName("offtop_default_stringAbove").getLocaleStringByLanguage(language);

        if ((event.getAuthor().getIdLong() == botId) &&
                (event.getMessage().getContentRaw().contains(stringAbove)) &&
                (!event.getMessage().getEmbeds().isEmpty()) &&
                (event.getMessage().getAuthor().isBot())) {
            try {
                Thread.sleep(offtopDeleteTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            event.getMessage().delete().complete();
        }
    }

    @SneakyThrows
    private MessageEmbed offtopEmbed(MessageReactionAddEvent event) {
        GuildConfig guildConfig = guildConfigService.getActualGuildConfig(event.getGuild());
        Languages language = guildConfig.getBotLanguage();

        Message msg = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
        User author = msg.getAuthor();

        String fieldName = localeDAO.getLocaleByName("offtop_default_fieldName").getLocaleStringByLanguage(language);
        String title = localeDAO.getLocaleByName("offtop_default_title").getLocaleStringByLanguage(language);
        String fieldValue = localeDAO.getLocaleByName("offtop_default_fieldValue").getLocaleStringByLanguage(language);
        String footerText = localeDAO.getLocaleByName("offtop_default_footerText").getLocaleStringByLanguage(language);

        EmbedBuilder eb = new EmbedBuilder();
        if (!author.isBot()) {
            author.openPrivateChannel().complete();
        }
        String img = author.getEffectiveAvatarUrl();
        eb.setTitle(title);
        eb.addField(fieldName,
                fieldValue, true);
        eb.setColor(Color.red);
        eb.setThumbnail(img);
        eb.setTimestamp(OffsetDateTime.now());
        eb.setFooter(footerText, event.getGuild().getIconUrl());

        return eb.build();
    }
}