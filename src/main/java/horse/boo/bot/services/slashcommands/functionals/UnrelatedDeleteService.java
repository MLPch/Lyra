package horse.boo.bot.services.slashcommands.functionals;

import horse.boo.bot.database.repository.ConfigRepository;
import horse.boo.bot.database.repository.IgnoreChannelRepository;
import horse.boo.bot.database.repository.LocaleRepository;
import horse.boo.bot.database.table.ConfigsTable;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.OffsetDateTime;

import static horse.boo.bot.DiscordClient.TYPE;
import static java.util.concurrent.TimeUnit.SECONDS;

@Component
public class UnrelatedDeleteService extends ListenerAdapter {

    private final Logger logger = LoggerFactory.getLogger(UnrelatedDeleteService.class);
    private final ConfigRepository configRepository;
    private final LocaleRepository localeRepository;
    private final IgnoreChannelRepository ignoreChannelRepository;


    public UnrelatedDeleteService(ConfigRepository configRepository, LocaleRepository localeRepository, IgnoreChannelRepository ignoreChannelRepository) {
        this.configRepository = configRepository;
        this.localeRepository = localeRepository;
        this.ignoreChannelRepository = ignoreChannelRepository;
    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        Guild guild = event.getGuild();
        ConfigsTable config = configRepository.getConfigByGuildId(guild.getIdLong());
        String language = config.getBotLanguage();

        Integer delaySeconds = configRepository.getConfigByGuildId(guild.getIdLong()).getUnrelatedDeleteTimeSec();
        boolean permissionToDelete = ignoreChannelRepository.existsByChannelId(event.getChannel().getIdLong());

        if (config.isFunctionUnrelatedDeleter() &&
                !permissionToDelete) {
            long unrelatedEmoteId = config.getUnrelatedEmoteId();
            long unrelatedEmoteCount = config.getUnrelatedEmoteCount();
            Message msg = event.getChannel().retrieveMessageById(event.getMessageId()).complete();
            User author = msg.getAuthor();
            msg.getReactions().forEach(react -> {

                var ume = unrelatedMessageEmbed("forUsers", guild, language, author, msg);
                var lme = unrelatedMessageEmbed("forLogs", guild, language, author, msg);

                if (react.getEmoji().getType() != Emoji.Type.UNICODE &&
                        react.getEmoji().asCustom().getIdLong() == unrelatedEmoteId &&
                        (!(author.isBot())) &&
                        react.getCount() >= unrelatedEmoteCount) {

                    msg.delete().queue();


                    event.getChannel().sendMessage(localeRepository.getValueByLanguageAndLocaleNameAndGuild(
                                    language, "unrelated_" + TYPE + "_stringAbove", guild) +
                                    author.getAsMention() + "!\n" + "*удаление через __" + delaySeconds + "__ секунд*")
                            .setEmbeds(ume).delay(delaySeconds, SECONDS).flatMap(Message::delete).queue();

                    if (author.hasPrivateChannel()) {
                        author.openPrivateChannel().queue(channel -> channel.sendMessageEmbeds(ume).complete());
                    }
                    guild.getTextChannelById(config.getLogChannelId()).sendMessageEmbeds(lme).complete();
                    logger.info("The message [" + msg + "] was responded to by the user [" + event.getUser() + "]");
                }
            });
        }
    }

    @NotNull
    private MessageEmbed unrelatedMessageEmbed(String titleType, Guild guild, String language, @NotNull User author, Message msg) {
        String img = author.getEffectiveAvatarUrl();

        String title = localeRepository.getValueByLanguageAndLocaleNameAndGuild(language, "unrelated_" + TYPE + "_title", guild);
        String fieldName = localeRepository.getValueByLanguageAndLocaleNameAndGuild(language, "unrelated_" + TYPE + "_fieldName", guild);
        String fieldValue = localeRepository.getValueByLanguageAndLocaleNameAndGuild(language, "unrelated_" + TYPE + "_fieldValue", guild);
        String footerText = localeRepository.getValueByLanguageAndLocaleNameAndGuild(language, "unrelated_" + TYPE + "_footerText", guild);

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
        //TODO: Добавить тех кто удалил в логи
        EmbedBuilder eb = new EmbedBuilder();
        if (title != null) {
            if (titleType.equals("forUsers")) {
                eb.setTitle(title);
            } else if (titleType.equals("forLogs")) {
                eb.setTitle("The message from the { " + author.getName() + "(" + author.getId() + ") } has been deleted.");
            }
        }

        if (fieldValue != null || fieldName != null) {
            if (fieldName == null) {
                fieldName = " ";
            }
            if (fieldValue == null) {
                fieldValue = " ";
            }
            eb.addField(fieldName, fieldValue, true);
        }

        // Content Field
        if (titleType.equals("forUsers")) {
            eb.addField("Содержание:", content, false);
        } else if (titleType.equals("forLogs")) {
            eb.addField("Content:", author.getAsMention() + " say: " + content, false);
        }
        // Place Field
        eb.addField("Place:", msg.getChannel().getAsMention(), false);

        eb.setColor(Color.red);
        eb.setThumbnail(img);
        eb.setTimestamp(OffsetDateTime.now());
        if (footerText != null) {
            eb.setFooter(footerText, guild.getIconUrl());
        }
        return eb.build();
    }

}