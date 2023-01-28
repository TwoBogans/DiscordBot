package org.au2b2t.global.listeners;

import com.google.common.collect.Sets;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.au2b2t.DiscordBot;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class LiveChatListener extends ListenerAdapter {

    @NonNull
    private Guild guild;
    @NonNull
    private TextChannel channel;

    private Set<String> messages;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getChannel() == channel) {
            // Send Message
        }
    }

    public void startThread() {
        final var i = new AtomicInteger();
        final var t = new Thread(() -> {
            while (true) {
                onTick(i);
            }
        });
        t.setDaemon(true);
        t.start();
    }

    private void onTick(AtomicInteger i) {
        try {
            i.incrementAndGet();

            var now = DiscordBot.getApi().getChat();

            if (messages == null) {
                messages = Set.copyOf(now.getMessages());
                return;
            }

            Set<String> lM, nM, dM;

            { // logic level 1: current state
                lM = messages;
                nM = Set.copyOf(now.getMessages());
            }

            { // logic level 2: get difference
                dM = Sets.difference(lM, nM);
            }

            if (i.get() >= 20) {
                if (dM.isEmpty()) nM.forEach(message -> channel.sendMessage(message).queue());
                else dM.forEach(message -> channel.sendMessage(message).queue());
                System.out.printf("List: %s %s %s %s", lM.size(), nM.size(), dM.size(), dM);
            }
        } catch (Exception e) {

        }
    }
}
