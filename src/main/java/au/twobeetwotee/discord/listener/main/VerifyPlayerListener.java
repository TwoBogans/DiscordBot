package au.twobeetwotee.discord.listener.main;

import au.twobeetwotee.discord.Main;
import au.twobeetwotee.discord.util.Constants;
import au.twobeetwotee.discord.util.Util;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class VerifyPlayerListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equalsIgnoreCase("verify")) {
            if (Objects.requireNonNull(event.getGuild()).getIdLong() != Main.getConfig().getMainGuild()) {
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
            final var role = guild.getRoleById(Main.getConfig().getUserRole());

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
                    var namemc = Constants.NAMEMC_URL.formatted(uuid);
                    var embed = Util.defaultBuilder("Player Verified")
                            .setThumbnail(Constants.MINOTAR_BODY.formatted(uuid))
                            .addField("Discord User", user.getAsTag(), false)
                            .addField("Discord ID", user.getId(), false)
                            .addField("Minecraft UUID", String.format("[%s](%s)", uuid, namemc), false)
                            .build();
                    Util.log(embed);
                });
            }
        }
    }
}
