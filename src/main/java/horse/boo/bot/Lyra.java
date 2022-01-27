package horse.boo.bot;

import horse.boo.bot.config.BotConfig;
import net.dv8tion.jda.api.JDA;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Lyra {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(BotConfig.class);
        JDA jda = context.getBean("jdaBuilder", JDA.class);

    }

}