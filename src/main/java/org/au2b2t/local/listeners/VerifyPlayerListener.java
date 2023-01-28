package org.au2b2t.local.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.au2b2t.DiscordBot;
import org.au2b2t.util.Util;

import java.util.Objects;

public class VerifyPlayerListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equalsIgnoreCase("verify")) {
            if (Objects.requireNonNull(event.getGuild()).getIdLong() != DiscordBot.getConfig().getMainGuild()) {
                event.reply("discord.gg/popbob only!")
                        .setEphemeral(true)
                        .queue();
                return;
            }

            final var user = event.getUser();
            final var verified = Util.isUserVerified(user);
            final var guild = event.getGuild();

            // IDK why this would happen
            if (guild == null) throw new NullPointerException();

            // Not Verified
            if (!verified) {
                event.reply("You must do `/verify` in-game first!")
                        .setEphemeral(true)
                        .queue();
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
                    var uuid = Util.getUserMinecraftUUID(user);
                    var embed = new EmbedBuilder()
                            .setColor(2263842)
                            .setThumbnail("https://minotar.net/avatar/%s/100.png".formatted(uuid))
                            .setTitle("Verified Player", "https://namemc.com/search?q=%s".formatted(uuid))
                            .addField("Discord User", user.getAsTag(), false)
                            .addField("Minecraft UUID", uuid.toString(), false)
                            .setFooter("Discord ID: %s".formatted(user.getId()))
                            .build();
                    Util.log(embed);
                });
            }
        }
    }
}
