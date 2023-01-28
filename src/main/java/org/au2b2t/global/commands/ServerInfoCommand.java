package org.au2b2t.global.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.au2b2t.DiscordBot;

public class ServerInfoCommand extends CommandDataImpl {

    public ServerInfoCommand() {
        super("serverinfo", "Get Server Information for 2b2t.au");
        DiscordBot.getJda().addEventListener(new Listener());
    }

    protected static class Listener extends ListenerAdapter {

        @Override
        public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
            if (event.getName().equalsIgnoreCase("serverinfo")) {
                try {
                    final var api = DiscordBot.getApi();
                    final var stats = api.getServerStats();
                    final var perf = stats.getPerformance();

                    event.reply(String.format("Players: %s, TPS: %s, Uptime: %s",
                                    stats.getOnline(),
                                    perf.getTps(),
                                    stats.getUptime()))
                            .setEphemeral(true)
                            .queue();
                } catch (Exception e) {
                    e.printStackTrace();
                    event.reply("Server is offline! :/")
                            .setEphemeral(true)
                            .queue();
                }
            }
        }
    }
}
