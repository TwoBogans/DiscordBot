package org.au2b2t.global.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.au2b2t.DiscordBot;

import java.time.Instant;

public class HelpCommand extends CommandDataImpl {
    public HelpCommand() {
        super("help", "Get a list of commands.");
        DiscordBot.getJda().addEventListener(new Listener());
    }

    protected static class Listener extends ListenerAdapter {

        @Override
        public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
            if (event.getName().equalsIgnoreCase("help")) {
                final var avatarUrl = "https://imgur.com/v05uTf1.gif";
                final var embed = new EmbedBuilder()
                        .setColor(2263842)
                        .setTitle("Information & Commands")
                        .setDescription("\uD83D\uDCE8 admin@2b2t.au\n" + "\uD83D\uDD17 https://2b2t.au/")
                        .addField("Player", "```%s```", true)
                        .addField("Admin", "```%s```", true)
                        .addField("Secret", "```%s```", true)
                        .setFooter("Australian Hausemaster", avatarUrl)
                        .setTimestamp(Instant.now())
                        .build();

                event.reply("You need help!")
                        .setEphemeral(true)
                        .queue();
            }
        }
    }
}
