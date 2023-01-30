package au.twobeetwotee.discord.listener.global;

import au.twobbeetwotee.api.responses.ChatMessage;
import au.twobeetwotee.discord.Main;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Map;

@RequiredArgsConstructor
public class LiveChatListener extends ListenerAdapter {
    @NonNull
    private TextChannel guildChannel;

    private int latestHash = -1;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getChannel() == guildChannel) {
            event.getMessage().addReaction(Emoji.fromUnicode("✅")).queue();
        }
    }

    public void startThread() {
        final var t = new Thread(this::onTick);
        t.setDaemon(true);
        t.start();
    }

    private void onTick() {
        while (true) {
            try {
                var response = Main.getApi().getChat();
                for (Map.Entry<Integer, ChatMessage> entry : response.entrySet()) {
                    if (entry.getValue() == null) continue;
                    if (entry.getKey() == latestHash) continue;

                    System.out.println(response);

                    guildChannel.sendMessage(entry.getValue().getMessage()).queue();
                    latestHash = entry.getKey();
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
