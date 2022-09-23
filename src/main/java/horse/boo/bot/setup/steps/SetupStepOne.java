package horse.boo.bot.setup.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SetupStepOne extends ListenerAdapter {
    private String systemChannel = "system-channel-lyra";
    ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        var guild = event.getGuild();
        var msgHistory = getMessagesFromSystemLyraChannel(guild);
        var channel = guild.getTextChannelById(getSystemLyraChannel(guild).getId());
        System.out.println("Config history in guild " + event.getGuild() + ":\n" + msgHistory);
        if (msgHistory.isEmpty()) {
            channel.sendMessage("{\n" +
                    "  \"variables\": {\n" +
                    "    \"bot\": {\n" +
                    "      \"name\": \"Spike\",\n" +
                    "      \"id\": \"746130954355081408\"\n" +
                    "    },\n" +
                    "    \"emotes\": {\n" +
                    "      \"notificationUsers\": {\n" +
                    "        \"name\": \"dovolen\",\n" +
                    "        \"id\": \"909091212890361966\"\n" +
                    "      },\n" +
                    "      \"notificationOwner\": {\n" +
                    "        \"name\": \"zeleniy\",\n" +
                    "        \"id\": \"909091915675353128\"\n" +
                    "      },\n" +
                    "      \"offtopEmote\": {\n" +
                    "        \"name\": \"xyi\",\n" +
                    "        \"id\": \"822373612564512788\"\n" +
                    "      }\n" +
                    "    },\n" +
                    "    \"counts\": {\n" +
                    "      \"offtopEmote\": \"1\"\n" +
                    "    },\n" +
                    "    \"channels\": {\n" +
                    "      \"notificationChannel\": \"734167565756137492\",\n" +
                    "      \"joinAndLeaveChannel\": \"734167565756137492\",\n" +
                    "      \"botReadyChannel\": \"942816365331513445\",\n" +
                    "      \"vyborKomnatChannel\": \"734167565756137492\"\n" +
                    "    },\n" +
                    "    \"times\": {\n" +
                    "      \"offtopDelete\": \"3000\"\n" +
                    "    }\n" +
                    "  }\n" +
                    "}").queue();
            channel.sendMessage("--------\n--------\n--------\n").queue();
        }

        System.out.println(msgHistory.get(msgHistory.size() - 1).getContentRaw());
//        msgHistory.get(msgHistory.size() - 1).editMessage("{\n" +
//                "  \"variables\": {\n" +
//                "    \"bot\": {\n" +
//                "      \"name\": \"sasdasda\",\n" +
//                "      \"id\": \"asdasdasa\"\n" +
//                "    },\n" +
//                "    \"emotes\": {\n" +
//                "      \"notificationUsers\": {\n" +
//                "        \"name\": \"bruuasdasdauh\",\n" +
//                "        \"id\": \"134576asdasda612354412\"\n" +
//                "      },\n" +
//                "      \"notificationOwner\": {\n" +
//                "        \"name\": \"bruasdasdauuh\",\n" +
//                "        \"id\": \"1345766asdasda12354412\"\n" +
//                "      },\n" +
//                "      \"offtopEmote\": {\n" +
//                "        \"name\": \"bruasdasdauuh\",\n" +
//                "        \"id\": \"13457661asdasda2354412\"\n" +
//                "      }\n" +
//                "    },\n" +
//                "    \"counts\": {\n" +
//                "      \"offtopEmote\": \"bruasdasdauuuh\"\n" +
//                "    },\n" +
//                "    \"channels\": {\n" +
//                "      \"notificationChannel\": \"sdasdaasdasdaasdasdasdasd\",\n" +
//                "      \"joinAndLeaveChannel\": \"sdasasdasdadasdasd\",\n" +
//                "      \"botReadyChannel\": \"sdasdasasdasdadasd\",\n" +
//                "      \"vyborKomnatChannel\": \"sdasdasdasdaasdasd\"\n" +
//                "    },\n" +
//                "    \"times\": {\n" +
//                "      \"offtopDelete\": \"3000\"\n" +
//                "    }\n" +
//                "  }\n" +
//                "}").queue();
//        System.out.println(msgHistory.get(msgHistory.size() - 1).getContentRaw());

    }


    public Languages onButtonClick(@NotNull ButtonInteractionEvent event) {
        Languages language = Languages.ENGLISH;
        if (event.getComponentId().equals("1")) {
            event.getMessage().delete().queue();
            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle("Выбран русский язык настройщика.")
                    .setColor(Color.red);
            event.getChannel()
                    .sendMessageEmbeds(eb.build())
                    .delay(4, TimeUnit.SECONDS)
                    .flatMap(Message::delete)
                    .queue();

            language = Languages.RUSSIAN;
        } else if (event.getComponentId().equals("2")) {
            event.getMessage().delete().queue();
            EmbedBuilder eb = new EmbedBuilder()
                    .setTitle("The English language of the customizer is selected.")
                    .setColor(Color.blue);
            event.getChannel()
                    .sendMessageEmbeds(eb.build())
                    .delay(4, TimeUnit.SECONDS)
                    .flatMap(Message::delete)
                    .queue();
            language = Languages.ENGLISH;
        }
        return language;
    }

    public GuildChannel createSystemLyraChannel(Guild guild) {
        final GuildChannel[] channel = new GuildChannel[1];
        System.out.println("createSystemLyraChannel");
        guild.createTextChannel(systemChannel)
                .addPermissionOverride(Objects.requireNonNull(guild.getMemberById("320332718921482241")), EnumSet.of(Permission.ADMINISTRATOR), null)
                .addPermissionOverride(Objects.requireNonNull(guild.getBotRole()), EnumSet.of(Permission.ADMINISTRATOR), null)
                .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                .queue(c -> channel[0] = c);

        return channel[0];
    }

    private GuildChannel getSystemLyraChannel(Guild guild) {
        System.out.println("getSystemLyraChannel");
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
