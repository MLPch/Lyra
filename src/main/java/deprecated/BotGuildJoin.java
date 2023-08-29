//package horse.boo.bot.setup.steps;
//
//
//import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
//import net.dv8tion.jda.api.hooks.ListenerAdapter;
//import org.jetbrains.annotations.NotNull;
//
//public class BotGuildJoin extends ListenerAdapter {
//
//    public void onGuildJoin(@NotNull GuildJoinEvent event) {
//
//        var guild = event.getGuild();
//
//        configService.setInitialGuildConfig(guild);
//        System.out.println("Подключилась к новой гильдии! Называется: " + guild.getName());
//    }
//
//}
