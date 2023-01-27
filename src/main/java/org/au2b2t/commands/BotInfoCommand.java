package org.au2b2t.commands;

import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.au2b2t.DiscordBot;
import org.au2b2t.util.Util;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.time.Instant;
import java.util.stream.Collectors;

public class BotInfoCommand extends CommandDataImpl {

    public BotInfoCommand() {
        super("botinfo", "Get Bot Information");
        setGuildOnly(true);
        setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
        DiscordBot.getJda().addEventListener(new Listener());
    }

    protected static class Listener extends ListenerAdapter {

        @Override
        public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
            if (event.getName().equalsIgnoreCase("botinfo")) {
                final var avatarUrl = "https://minotar.net/avatar/306eae875a8d4e148ec0375b54475d15/100.png";
                final var embed = new EmbedBuilder()
                        .setColor(2263842)
                        .setThumbnail(avatarUrl)
                        .setTitle("Bot Information")
                        .addField("Uptime", Util.getUptime(), false)
                        .addField("Total Guilds", String.valueOf(DiscordBot.getJda().getGuilds().size()), false)
                        .addField("Total Members", String.valueOf(DiscordBot.getJda().getGuilds().stream().map(Guild::getMemberCount).collect(Collectors.toSet())), false)
                        .setFooter("Australian Hausemaster", avatarUrl)
                        .setTimestamp(Instant.now())
                        .build();

                event.replyEmbeds(embed)
                        .queue();
            }
        }
    }
}
