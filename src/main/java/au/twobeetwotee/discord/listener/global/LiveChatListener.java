package au.twobeetwotee.discord.listener.global;

import au.twobbeetwotee.api.responses.ChatResponse;
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
                var now = Main.getApi().getChat().getMap();

                for (Map.Entry<Integer, ChatResponse.ChatMessage> entry : now.entrySet()) {
                    if (entry.getKey() == latestHash) continue;
                    var message = entry.getValue();

                    guildChannel.sendMessage(message.getMessage()).queue();
                    latestHash = entry.getKey();
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
