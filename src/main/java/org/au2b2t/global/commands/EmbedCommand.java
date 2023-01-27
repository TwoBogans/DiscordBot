package org.au2b2t.global.commands;

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
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.au2b2t.DiscordBot;
import org.au2b2t.util.Util;

import java.io.StringReader;

public class EmbedCommand extends CommandDataImpl {

    public EmbedCommand() {
        super("embed", "Send JSON as message/embed");
        setGuildOnly(true);
        setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
        addOption(OptionType.STRING, "url", "Raw JSON URL", true);
        DiscordBot.getJda().addEventListener(new Listener());
    }

    protected static class Listener extends ListenerAdapter {

        @Override
        public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
            if (event.getName().equalsIgnoreCase("embed")) {
                var option = event.getOption("url");
                if (option != null) {
                    try {
                        var url = option.getAsString();
                        var json = Util.readUrl(url);
                        var reader = new JsonReader(new StringReader(json));

                        reader.setLenient(true);

                        var data = Util.jsonToMessage(JsonParser.parseReader(reader).getAsJsonObject());
                        var message = event.getChannel().sendMessage(data);

                        message.queue(success -> event.reply("Embed Successfully Sent.").setEphemeral(true).queue());

                        reader.close();

                        return;
                    } catch (Exception e) {
                        event.reply(String.format("Failed with exception:%n%n%s", e.getMessage()))
                                .setEphemeral(true)
                                .queue();
                        return;
                    }
                }

                event.reply("Invalid JSON URL?")
                        .setEphemeral(true)
                        .queue();
            }
        }
    }
}
