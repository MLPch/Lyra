package horse.boo.bot.services.slashcommands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class LanguageSelectService extends ListenerAdapter {
    /**
     * @param event - Срабатывает при вызове команды "languageSelect".
     *              Возвращает эмбед с кнопками для выбора языка видимый для всех.
     */
    public void languageSelect(@NotNull SlashCommandInteractionEvent event) {


        MessageEmbed eb = new EmbedBuilder()
                .setAuthor("Welcome to the Setup wizard!")
                .addField("To continue - please select a language.", "There are currently 4 languages available.", true)
                .setColor(Color.magenta)
                .build();

        event.reply("").setEmbeds(eb).addActionRow(
                        Button.danger("language.english", "English"),
                        Button.danger("language.russian", "Russian"),
                        Button.danger("language.ukrainian", "Ukrainian"),
                        Button.danger("language.chinese", "Chinese"))
                .queue();
    }
}
