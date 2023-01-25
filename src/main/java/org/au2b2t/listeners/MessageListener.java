package org.au2b2t.listeners;

import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        /* Private channels, I'm pretty sure, are DM messages.
         * Public channels are in discord servers. */
        if (event.isFromType(ChannelType.PRIVATE)) {
            System.out.printf("[PM] %s: %s\n",
                    event.getAuthor().getName(),
                    event.getMessage().getContentDisplay()
            );
        } else {
            System.out.printf("[%s][%s] %s: %s\n",
                    event.getGuild().getName(),
                    event.getChannel().getName(),
                    event.getMember().getEffectiveName(),
                    event.getMessage().getContentDisplay()
            );
        }


        if (event.getChannel().getIdLong() == 0L) {
            MessageChannelUnion groupsChannel = event.getChannel();
            Message message = event.getMessage();

            if (message.getInvites().size() == 0) {
                event.getMessage().delete().queue();
            }

            List<String> invites = message.getInvites();

            invites.stream()
                   .findFirst()
                   .ifPresent(code -> message.delete()
                   .queue(m -> Invite.resolve(event.getJDA(), code)
                   .queue(i -> groupsChannel.sendMessage(i.getUrl())
                   .queue())));
        }
    }
}
