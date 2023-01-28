package org.au2b2t.global.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.au2b2t.DiscordBot;
import org.au2b2t.util.Util;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

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
                    final var api = DiscordBot.getAuApi();
                    final var stats = api.getServerStats();
                    final var perf = stats.getPerformance();

                    event.reply(String.format("Players: %s, TPS: %s, Uptime: %s",
                                    stats.getOnline(),
                                    perf.getTps(),
                                    stats.getUptime()))
                            .queue();
                } catch (Exception e) {
                    event.reply("Server is offline! :/")
                            .queue();
                }
            }
        }
    }
}
