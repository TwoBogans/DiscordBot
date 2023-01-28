package org.au2b2t.global.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.au2b2t.DiscordBot;

public class DiscordCommand extends CommandDataImpl {

    public DiscordCommand() {
        super("discord", "2b2t Australia Discord Invite");
        DiscordBot.getJda().addEventListener(new Listener());
    }

    protected static class Listener extends ListenerAdapter {

        @Override
        public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
            if (event.getName().equalsIgnoreCase("discord")) {
                event.reply("discord.gg/popbob")
                        .setEphemeral(true)
                        .queue();
            }
        }
    }
}
