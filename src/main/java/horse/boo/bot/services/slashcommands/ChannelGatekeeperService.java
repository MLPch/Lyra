package horse.boo.bot.services.slashcommands;

import horse.boo.bot.database.enums.FieldType;
import horse.boo.bot.database.enums.Languages;
import horse.boo.bot.database.table.LocalesTable;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static horse.boo.bot.database.enums.LocaleType.GATEKEEPER;

@Component
public class ChannelGatekeeperService extends ListenerAdapter {

    public void gatekeeper(@NotNull SlashCommandInteractionEvent event) {
        StringBuilder sb = new StringBuilder();

        sb.append("# ").append(event.getOption("gatekeeper_channel").getAsChannel().getName()).append("\n")
                .append(event.getOption("gatekeeper_channel").getAsChannel().getAsMention()).append("\n");

        if (event.getOption("gatekeeper_description") != null) {
            sb.append("### ").append(event.getOption("gatekeeper_description").getAsString());
        }

        event.reply(sb.toString()).addActionRow(
                        Button.primary("subscribe.channel", "Subscribe"),
                        Button.danger("unsubscribe.channel", "Unsubscribe"))
                .queue();
    }

    public List<LocalesTable> gatekeeperInitLocalesTable(Guild guild, String mode) {
        List<LocalesTable> localesTableList = new ArrayList<>();
        for (FieldType fieldT : FieldType.values()) {
            switch (fieldT) {
                case GATEKEEPER_SUBSCRIBE_EPHEMERAL -> {
                    Arrays.stream(Languages.values()).forEach(language -> {
                        LocalesTable initGatekeeperLocale = getLocalesNewTable(guild, mode, fieldT, language);
                        switch (language) {
                            case ENGLISH -> {
                                initGatekeeperLocale.setValue("You have been successfully subscribed to the channel!");
                                localesTableList.add(initGatekeeperLocale);
                            }
                            case RUSSIAN -> {
                                initGatekeeperLocale.setValue("Вы успешно подписались на канал!");
                                localesTableList.add(initGatekeeperLocale);
                            }
                            case UKRAINE -> {
                                initGatekeeperLocale.setValue("Ви успішно підписалися на канал!");
                                localesTableList.add(initGatekeeperLocale);
                            }
                            case CHINESE -> {
                                initGatekeeperLocale.setValue("您已成功订阅频道！");
                                localesTableList.add(initGatekeeperLocale);
                            }
                        }
                    });
                }
                case GATEKEEPER_UNSUBSCRIBE_EPHEMERAL -> {
                    Arrays.stream(Languages.values()).forEach(language -> {
                        LocalesTable initGatekeeperLocale = getLocalesNewTable(guild, mode, fieldT, language);
                        switch (language) {
                            case ENGLISH -> {
                                initGatekeeperLocale.setValue("You have been successfully unsubscribed from the channel!");
                                localesTableList.add(initGatekeeperLocale);
                            }
                            case RUSSIAN -> {
                                initGatekeeperLocale.setValue("Вы успешно отписались от канала!");
                                localesTableList.add(initGatekeeperLocale);
                            }
                            case UKRAINE -> {
                                initGatekeeperLocale.setValue("Ви успішно відписалися від каналу!");
                                localesTableList.add(initGatekeeperLocale);
                            }
                            case CHINESE -> {
                                initGatekeeperLocale.setValue("您已成功取消订阅该频道!");
                                localesTableList.add(initGatekeeperLocale);
                            }
                        }
                    });
                }
            }
        }
        return localesTableList;
    }

    @NotNull
    private static LocalesTable getLocalesNewTable(@NotNull Guild guild, String mode, @NotNull FieldType fieldT, @NotNull Languages language) {
        LocalesTable initGatekeeperLocale = new LocalesTable();
        initGatekeeperLocale.setGuildName(guild.getName());
        initGatekeeperLocale.setGuildId(guild.getIdLong());
        initGatekeeperLocale.setModeType(mode);
        initGatekeeperLocale.setLocaleType(GATEKEEPER.name());
        initGatekeeperLocale.setFieldType(fieldT.getFieldType());
        initGatekeeperLocale.setLanguage(language.getLanguage());
        return initGatekeeperLocale;
    }
}
