package au.twobeetwotee.discord.listener.global;

import au.twobeetwotee.discord.Main;
import au.twobeetwotee.discord.util.Util;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        /* Boosters Channel - 2b2t Australia Only */
        if (event.getChannel().getIdLong() == Main.getConfig().getBoosterChannel() &&
                Objects.requireNonNull(event.getGuild()).getIdLong() == Main.getConfig().getMainGuild()) {
            event.getMessage().addReaction(Util.randomEmojiFromGuild(event.getGuild(), true)).queue(); // Add Random Emoji
            return;
        }

        /* Private channels, I'm pretty sure, are DM messages.
         * Public channels are in discord servers. */
        if (event.getAuthor().isBot()) return;
        if (event.isFromType(ChannelType.PRIVATE)) {
            System.out.printf("[PM] %s: %s\n",
                    event.getAuthor().getName(),
                    event.getMessage().getContentDisplay()
            );

            final var embed = Util.defaultBuilder("Private Message Received")
                    .setThumbnail(event.getAuthor().getAvatarUrl())
                    .setDescription(String.format("%s: %s",
                            event.getAuthor().getAsTag(),
                            event.getMessage().getContentDisplay()))
                    .build();

            Util.log(embed);

        } else {
            System.out.printf("[%s][%s] %s: %s\n",
                    event.getGuild().getName(),
                    event.getChannel().getName(),
                    event.getMember().getEffectiveName(),
                    event.getMessage().getContentDisplay()
            );
        }
    }
}
