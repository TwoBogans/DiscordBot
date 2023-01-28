package org.au2b2t.global.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.au2b2t.DiscordBot;
import org.au2b2t.util.Util;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

public class BotInfoCommand extends CommandDataImpl {

    public BotInfoCommand() {
        super("botinfo", "Get Bot Information");
        DiscordBot.getJda().addEventListener(new Listener());
    }

    protected static class Listener extends ListenerAdapter {

        @Override
        public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
            if (event.getName().equalsIgnoreCase("botinfo")) {
                final var guildsCount = DiscordBot.getJda().getGuilds().size();
                final var totalMembers = new AtomicInteger(); DiscordBot.getJda().getGuilds().stream().map(Guild::getMemberCount).forEach(totalMembers::getAndAdd);
                final var embed = new EmbedBuilder()
                        .setColor(2263842)
                        .setTitle("Bot Information")
                        .addField("Uptime", Util.getUptime(), true)
                        .addField("Total Guilds", String.valueOf(guildsCount), true)
                        .addField("Total Members", String.valueOf(totalMembers), true)
                        .setFooter("Australian Hausemaster", "https://imgur.com/v05uTf1.gif")
                        .setTimestamp(Instant.now())
                        .build();

                event.replyEmbeds(embed)
                        .setEphemeral(true)
                        .queue();
            }
        }
    }
}
