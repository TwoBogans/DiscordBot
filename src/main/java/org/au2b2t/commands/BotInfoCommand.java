package org.au2b2t.commands;

import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
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
                var embed = new EmbedBuilder()
                        .setTitle("Bot Information")
                        .addField("Uptime", Util.getUptime(), false)
                        .setAuthor("Australian Hausemaster", "https://minotar.net/avatar/306eae875a8d4e148ec0375b54475d15/100.png")
                        .setTimestamp(Instant.now())
                        .build();

                event.replyEmbeds(embed)
                        .queue();
            }
        }
    }
}
