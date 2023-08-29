package horse.boo.bot.events;

import horse.boo.bot.database.repository.ConfigRepository;
import horse.boo.bot.database.repository.LocaleRepository;
import horse.boo.bot.database.table.ConfigsTable;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.OffsetDateTime;

@Component
public class UnrelatedDeleteService extends ListenerAdapter {

    private final Logger logger = LoggerFactory.getLogger(BotReadyService.class);
    private final ConfigRepository configRepository;
    private final LocaleRepository localeRepository;

    public String type = "default";

    public UnrelatedDeleteService(ConfigRepository configRepository, LocaleRepository localeRepository) {
        this.configRepository = configRepository;
        this.localeRepository = localeRepository;
    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        Guild guild = event.getGuild();
        ConfigsTable config = configRepository.getConfigByGuildId(guild.getIdLong());
        String language = config.getBotLanguage();

        if (config.isFunctionUnrelatedDeleter()) {
            long unrelatedEmoteId = config.getUnrelatedEmoteId();
            long botId = config.getBotId();
            long unrelatedEmoteCount = config.getUnrelatedEmoteCount();
            Message msg = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
            User author = msg.getAuthor();

            for (MessageReaction react : msg.getReactions()) {
                if (react.getEmoji().getType() != Emoji.Type.UNICODE &&
                        react.getEmoji().asCustom().getIdLong() == unrelatedEmoteId &&
                        (!(author.getIdLong() == botId)) &&
                        (!(author.isBot())) &&
                        react.getCount() >= unrelatedEmoteCount) {
                    msg.delete().complete();

                    String ping = author.getAsMention();
                    event.getChannel().sendMessage(localeRepository.getValueByLanguageAndLocaleNameAndGuild(language, "unrelated_" + type + "_stringAbove", guild) + ping + "!\n")
                            .setEmbeds(unrelatedMessageEmbed("forUsers", guild, language, author, msg))
                            .complete();
                    if (author.hasPrivateChannel()) {
                        author.openPrivateChannel().queue(channel -> channel.sendMessageEmbeds(unrelatedMessageEmbed("forUsers", guild, language, author, msg)).complete());
                    }
                    guild.getTextChannelById(config.getLogChannelId()).sendMessageEmbeds(unrelatedMessageEmbed("forLogs", guild, language, author, msg)).complete();
                    logger.info("The message [" + msg + "] was responded to by the user [" + event.getUser() + "]");
                }
            }
        }
    }


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.isFromGuild()) {
            Guild guild = event.getGuild();
            ConfigsTable config = configRepository.getConfigByGuildId(guild.getIdLong());
            String language = config.getBotLanguage();
            if (event.getMessage().getAuthor().getIdLong() == config.getBotId() &&
                    (event.getMessage().getContentRaw().contains(localeRepository.getValueByLanguageAndLocaleNameAndGuild(language, "unrelated_" + type + "_stringAbove", guild))) &&
                    (event.getMessage().getAuthor().isBot())) {
                try {
                    Thread.sleep(config.getUnrelatedDeleteTimeSec() * 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                event.getMessage().delete().complete();
            }
        }
    }


    private MessageEmbed unrelatedMessageEmbed(String titleType, Guild guild, String language, User author, Message msg) {
        String img = author.getEffectiveAvatarUrl();

        String fieldName = localeRepository.getValueByLanguageAndLocaleNameAndGuild(language, "unrelated_" + type + "_fieldName", guild);
        String title = localeRepository.getValueByLanguageAndLocaleNameAndGuild(language, "unrelated_" + type + "_title", guild);
        String fieldValue = localeRepository.getValueByLanguageAndLocaleNameAndGuild(language, "unrelated_" + type + "_fieldValue", guild);
        String footerText = localeRepository.getValueByLanguageAndLocaleNameAndGuild(language, "unrelated_" + type + "_footerText", guild);

        if (!author.isBot()) {
            try {
                author.openPrivateChannel().complete();
            } catch (ErrorResponseException e) {
                logger.error("I couldn't send a message because the user is dead or some other unknown shit happened");
            }
        }

        String content;
        if (msg.getContentRaw().isEmpty()) {
            if (language.equals("russian")) {
                content = "*здесь были какие то файлы*";
            } else {
                content = "*there were some files here*";
            }
        } else {
            if (msg.getContentRaw().length() >= 1024) {
                content = msg.getContentRaw().substring(0, 800);
            } else {
                content = msg.getContentRaw();
            }
        }

        EmbedBuilder eb = new EmbedBuilder();
        if (titleType.equals("forUsers")) {
            eb.setTitle(title);
        } else if (titleType.equals("forLogs")) {
            eb.setTitle("The message from the { " + author.getName() + "(" + author.getId() + ") } has been deleted.");
        }
//        eb.addField(fieldName, fieldValue, false);
        if (titleType.equals("forUsers")) {
            eb.addField("Содержание:", content, false);
        } else if (titleType.equals("forLogs")) {
            eb.addField("Content:", author.getAsMention() + " say: " + content, false);
        }
        eb.addField("Place:", msg.getChannel().getAsMention(), false);
        eb.setColor(Color.red);
        eb.setThumbnail(img);
        eb.setTimestamp(OffsetDateTime.now());
        eb.setFooter(footerText, guild.getIconUrl());
        return eb.build();
    }

}