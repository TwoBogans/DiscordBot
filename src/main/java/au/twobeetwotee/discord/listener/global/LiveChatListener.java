package au.twobeetwotee.discord.listener.global;

import au.twobbeetwotee.api.responses.ChatMessage;
import au.twobeetwotee.discord.Main;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Map;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class LiveChatListener extends ListenerAdapter {
    @NonNull
    private TextChannel guildChannel;

    private int latestHash = -1;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getChannel() != guildChannel) return;
        if (event.getAuthor().isBot()) return;

        event.getMessage().addReaction(Emoji.fromUnicode("✅")).queue();
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

                    var type = entry.getValue().getType();
                    var bold = type.equalsIgnoreCase("join")
                            || type.equalsIgnoreCase("quit")
                            || type.equalsIgnoreCase("death");

                    var msg = entry.getValue().getMessage();

                    msg = msg.replaceAll("_", "\\_")
                            .replaceAll("`", "\\`")
                            .replaceAll("\\*", "\\*`");

                    var name = msg.substring(msg.indexOf("<") + 1, msg.indexOf(">")).trim();
                    var content = msg.split(Pattern.quote("> "), 2)[1];

                    msg = bold ? "**%s**".formatted(msg) : "**<%s>** %s".formatted(name, content);

                    var embed = new EmbedBuilder()
                            .setDescription(msg)
                            .build();

                    guildChannel.sendMessageEmbeds(embed).queue();
                    latestHash = entry.getKey();
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
