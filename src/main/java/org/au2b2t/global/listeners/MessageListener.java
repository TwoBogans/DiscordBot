package org.au2b2t.global.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.au2b2t.DiscordBot;
import org.au2b2t.util.Util;

import java.time.Instant;
import java.util.Objects;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        /**
         * 2b2t Au Booster Channel
         */
        if (event.getChannel().getIdLong() == DiscordBot.getConfig().getBoosterChannel() &&
                Objects.requireNonNull(event.getGuild()).getIdLong() == DiscordBot.getConfig().getMainGuild()) {
            event.getMessage().addReaction(Util.randomEmojiFromGuild(event.getGuild())).queue(); // Add Random Emojiy
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

            final var embed = new EmbedBuilder()
                    .setColor(2263842)
                    .setTitle("PM Received")
                    .setDescription(event.getMessage().getContentDisplay())
                    .setFooter(event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl())
                    .setTimestamp(Instant.now())
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
