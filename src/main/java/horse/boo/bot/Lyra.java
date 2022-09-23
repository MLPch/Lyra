package horse.boo.bot;

import horse.boo.bot.config.BotConfig;
import horse.boo.bot.config.CommandsConfig;
import horse.boo.bot.events.MemberJoinEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class Lyra {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BotConfig.class);
        JDA jda = context.getBean("jdaBuilder", JDA.class);

    }


}