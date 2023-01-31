package au.twobeetwotee.discord.livechat;

import au.twobbeetwotee.api.responses.ChatMessage;
import au.twobeetwotee.discord.Main;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Map;
import java.util.regex.Pattern;

@Getter
@ToString
public class LiveChatListener {
    private final Guild guild;
    private final TextChannel guildChannel;
    private final Thread thread;
    private int latestHash = -1;

    public LiveChatListener(@NonNull Guild guild, @NonNull TextChannel guildChannel) {
        this.guild = guild;
        this.guildChannel = guildChannel;

        Main.getJda().addEventListener(new Listener(this));

        thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    @RequiredArgsConstructor
    protected static class Listener extends ListenerAdapter {
        @NonNull
        private LiveChatListener listener;
        @Override
        public void onMessageReceived(MessageReceivedEvent event) {
            if (event.getChannel() != listener.guildChannel) return;
            if (event.getAuthor().isBot()) return;
            event.getMessage().delete().queue();
        }
    }
    @RequiredArgsConstructor
    protected static class Thread extends java.lang.Thread {
        @NonNull
        private LiveChatListener listener;

        @Override
        public void run() {
            while (true) {
                try {
                    var response = Main.getApi().getChat();
                    for (Map.Entry<Integer, ChatMessage> entry : response.entrySet()) {
                        if (entry.getValue() == null) continue;
                        if (entry.getKey() == listener.latestHash) continue;

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
                                .setColor(listener.getColor(chat, content))
                                .setDescription(msg);

                        if (!imageUrl.isEmpty()) {
                            embed = embed.setImage(imageUrl);
                        }

                        listener.guildChannel.sendMessageEmbeds(embed.build()).queue();
                        listener.latestHash = entry.getKey();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    private int getColor(ChatMessage chatMessage, String messageContent) {
        var type = chatMessage.getType();
        var greenText = messageContent.startsWith(">");
        var blueText = messageContent.startsWith("`");
        return switch (type) {
            case "chat" -> greenText ? 44807 : blueText ? 24244 : 14211288;
            case "death" -> 16724522;
            case "join", "quit" -> 7039851;
            default -> 986895;
        };
    }
}
