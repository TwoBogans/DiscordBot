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

// TODO Save Active Guilds
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

                    var chat = entry.getValue();
                    var type = chat.getType();
                    var bold = type.equalsIgnoreCase("join")
                            || type.equalsIgnoreCase("quit")
                            || type.equalsIgnoreCase("death");

                    var msg = chat.getMessage();

                    // TODO FIX ESCAPE DISCORD FORMATTING
                    msg = msg.replaceAll("_", "\\_")
                            .replaceAll("`", "\\`")
                            .replaceAll("\\*", "\\*`");

                    var name = "";
                    var content = "";

                    if (!bold) {
                        name = msg.substring(msg.indexOf("<") + 1, msg.indexOf(">")).trim();
                        content = msg.split(Pattern.quote("> "), 2)[1];
                    }

                    msg = bold ? "**%s**".formatted(msg) : "**<%s>** %s".formatted(name, content);

                    var pattern = Pattern.compile("/(https?\\:\\/\\/)?(www\\.)?([a-z0-9]([a-z0-9]|(\\-[a-z0-9]))*\\.)+[a-z0-9]+(\\/[\\-a-z0-9_]+)*(\\/[a-z0-9]+\\.(gif|jpg|png|jpeg|JPG|PNG|JPEG|GIF){1})/g");
                    var imageUrl = "";
                    try {
                        imageUrl = pattern.matcher(msg).group();

                        if (!imageUrl.isEmpty())
                            msg = pattern.matcher(msg).replaceAll("![image](%s)".formatted(imageUrl));
                    } catch (IllegalStateException ignored) {

                    }

                    var embed = new EmbedBuilder()
                            .setColor(getColor(chat, content))
                            .setDescription(msg);

                    if (!imageUrl.isEmpty()) {
                        embed = embed.setImage(imageUrl);
                    }

                    guildChannel.sendMessageEmbeds(embed.build()).queue();
                    latestHash = entry.getKey();
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private int getColor(ChatMessage chatMessage, String messageContent) {
        var type = chatMessage.getType();
        var greenText = messageContent.startsWith(">");
        var blueText = messageContent.startsWith("`");
        return switch (type) {
            case "chat" -> greenText ? 44807 : blueText ? 24244 : 986895;
            case "death" -> 16724522;
            default -> 986895;
        };
    }
}
