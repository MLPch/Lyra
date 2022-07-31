package horse.boo.bot.setup.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.List;

public class SetupStepOne extends ListenerAdapter {
    private String systemChannel = "system-channel-lyra";
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        var guild = event.getGuild();
        var msgHistory = getMessagesFromSystemLyraChannel(guild);
        var channel = guild.getTextChannelById(getSystemLyraChannel(guild).getId());
        System.out.println("Config history in guild " + event.getGuild() + ":\n" + msgHistory);
        if (getMessagesFromSystemLyraChannel(guild).isEmpty()) {

//            for (int i = 0; i < 30; i++) {
//                channel.sendMessage("Param" + i + " = []").queue();
//            }
            for (Member mem : guild.getDefaultChannel().getMembers()){
                channel.sendMessage(mem.getUser().toString()).queue();
            }
        }

//        EmbedBuilder eb = new EmbedBuilder();
//        eb.setTitle("Шаг 1 - Выбор языка настройщика");
//        eb.addField("Выберите язык:",
//                "", true);
//        eb.setColor(Color.lightGray);
//        channel.sendMessageEmbeds(eb.build()).setActionRow(
//                Button.primary("1", Languages.RUSSIAN.getLanguage()),
//                Button.primary("2", Languages.ENGLISH.getLanguage())
//        ).queue();
    }

//    @Override
//    public void onButtonClick(@NotNull ButtonClickEvent event) {
//
//        if (event.getComponentId().equals("1")) {
//            event.getMessage().delete().queue();
//            EmbedBuilder eb = new EmbedBuilder()
//                    .setTitle("Выбран русский язык настройщика.")
//                    .setColor(Color.red);
//            event.getChannel()
//                    .sendMessageEmbeds(eb.build())
//                    .delay(4, TimeUnit.SECONDS)
//                    .flatMap(Message::delete)
//                    .queue();
//            language = Languages.RUSSIAN;
//        } else if (event.getComponentId().equals("2")) {
//            event.getMessage().delete().queue();
//            EmbedBuilder eb = new EmbedBuilder()
//                    .setTitle("The English language of the customizer is selected.")
//                    .setColor(Color.blue);
//            event.getChannel()
//                    .sendMessageEmbeds(eb.build())
//                    .delay(4, TimeUnit.SECONDS)
//                    .flatMap(Message::delete)
//                    .queue();
//            language = Languages.ENGLISH;
//        }
//    }

    public GuildChannel createSystemLyraChannel(Guild guild) {
        final GuildChannel[] channel = new GuildChannel[1];
        guild.createTextChannel(systemChannel)
                .addPermissionOverride(guild.getMemberById("320332718921482241"), EnumSet.of(Permission.ADMINISTRATOR), null)
                .addPermissionOverride(guild.getBotRole(), EnumSet.of(Permission.ADMINISTRATOR), null)
                .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                .queue(c -> channel[0] = c);
        return channel[0];
    }

    private GuildChannel getSystemLyraChannel(Guild guild) {
        return guild.getChannels()
                .stream()
                .filter(guildChannel -> guildChannel.getName().equals(systemChannel))
                .findFirst().orElseGet(() -> createSystemLyraChannel(guild));
    }

    @NotNull
    private List<Message> getMessagesFromSystemLyraChannel(Guild guild) {
        var channel = getSystemLyraChannel(guild);
        return MessageHistory.getHistoryFromBeginning((MessageChannel) channel).complete()
                .getRetrievedHistory();
    }


}
