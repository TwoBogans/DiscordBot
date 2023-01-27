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

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

public class EmbedCommand extends CommandDataImpl {

    public EmbedCommand() {
        super("embed", "Send JSON as message/embed");
        setGuildOnly(true);
        setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
        addOption(OptionType.STRING, "url", "Raw JSON URL", true);
        addOption(OptionType.BOOLEAN, "verify", "Add The Verify Button?");
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
                        var verify = event.getOption("verify");

                        var message = event.getChannel().sendMessage(data);

                        if (verify != null && verify.getAsBoolean()) {
                            message.addActionRow(Button.primary("verify", "Verify Player").withEmoji(Emoji.fromFormatted("<:hausecool:838708181273804851>")));
                        }

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

        @Override
        public void onButtonInteraction(ButtonInteractionEvent event) {
            if (event.getComponentId().equalsIgnoreCase("verify")) {
                final var user = event.getUser();
                final var verified = DiscordBot.isUserVerified(user);
                final var guild = event.getGuild();

                // IDK why this would happen
                if (guild == null) throw new NullPointerException();

                // Not Verified
                if (!verified) {
                    try {
                        event.reply("Do `/verify` in-game")
                                .addFiles(FileUpload.fromData(new URL("https://i.imgur.com/muYQcm4.png").openStream(), "verify"))
                                .setEphemeral(true)
                                .queue();
                    } catch (IOException e) {
                        event.reply("Do `/verify` in-game")
//                                .addFiles(FileUpload.fromData(new URL("https://i.imgur.com/muYQcm4.png").openStream()))
                                .setEphemeral(true)
                                .queue();
                    }
                    return;
                }

                // User Role
                final var role = guild.getRoleById(DiscordBot.getConfig().getUserRole());

                // Check Existence
                if (role != null) {
                    guild.retrieveMember(user).queue(member -> {
                        if (member.getRoles().contains(role)) {
                            event.reply("You have already been verified.")
                                    .setEphemeral(true)
                                    .queue();
                            return;
                        }

                        guild.addRoleToMember(member, role).queue();
                        event.reply("You have successfully been verified.")
                                .setEphemeral(true)
                                .queue();

                        // Send Log
                        var logsChannel = guild.getChannelById(TextChannel.class, DiscordBot.getConfig().getLogsChannel());
                        if (logsChannel != null) {
                            var uuid = DiscordBot.getUserMinecraftUUID(user);
                            var embed = new EmbedBuilder()
                                    .setColor(2263842)
                                    .setThumbnail("https://minotar.net/avatar/%s/100.png".formatted(uuid))
                                    .setTitle("Verified Player", "https://namemc.com/search?q=%s".formatted(uuid))
                                    .addField("Discord User", user.getAsTag(), false)
                                    .addField("Minecraft UUID", uuid.toString(), false)
                                    .setFooter("Discord ID: %s".formatted(user.getId()))
                                    .build();
                            logsChannel.sendMessageEmbeds(embed).queue();
                        }
                    });
                }
            }
        }
    }
}
