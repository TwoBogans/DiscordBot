package org.au2b2t.global.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.au2b2t.DiscordBot;
import org.au2b2t.global.listeners.LiveChatListener;

public class SetupLiveChatCommand extends CommandDataImpl {

    public SetupLiveChatCommand() {
        super("setuplivechat", "Setup Live Chat");
        addOption(OptionType.CHANNEL, "channel", "Channel For Live Chat", true);
        DiscordBot.getJda().addEventListener(new Listener());
    }

    protected static class Listener extends ListenerAdapter {

        @Override
        public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
            if (event.getName().equalsIgnoreCase("setuplivechat")) {
                var option = event.getOption("channel");
                if (option != null) {
                    var channel = option.getAsChannel();
                    var listener = new LiveChatListener(channel.getGuild(), channel.asTextChannel());
                    listener.startThread();
                    DiscordBot.getJda().addEventListener(listener);
                    event.reply("Success!")
                            .setEphemeral(true)
                            .queue();
                }
            }
        }
    }
}
