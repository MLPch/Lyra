package horse.boo.bot.setup;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class InitialSetupEvent extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        //TODO: Вынести сюда все шаги по настройке.

        Message msg = event.getMessage();
        if (msg.getContentRaw().equals("/lyrastart")) {
            System.out.println("lyrastart");
        }
    }


}
