package au.twobeetwotee.discord.listener.main;

import au.twobeetwotee.discord.Main;
import au.twobeetwotee.discord.util.Constants;
import au.twobeetwotee.discord.util.Util;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

import java.util.Objects;

public class VerifyPlayerListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getComponentId().equalsIgnoreCase("verify")) {
            if (Objects.requireNonNull(event.getGuild()).getIdLong() != Main.getConfig().getMainGuild()) {
                event.reply("discord.gg/popbob only feature!")
                        .setEphemeral(true)
                        .queue();
                return;
            }

            final var user = event.getUser();
            final var guild = event.getGuild();

            doVerify(event, user, guild);
        }
    }

    public static void doVerify(IReplyCallback callback, User user, Guild guild) {
        final var verified = Util.isUserVerified(user);

        // IDK why this would happen
        if (guild == null) throw new NullPointerException();

        // Not Verified
        if (!verified) {
            callback.reply("You must do `/verify` in-game first!")
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
                    callback.reply("You have already been verified.")
                            .setEphemeral(true)
                            .queue();
                    return;
                }

                guild.addRoleToMember(member, role).queue();
                callback.reply("You have successfully been verified.")
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
