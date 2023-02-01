package au.twobeetwotee.discord.command.commands.server;

import au.twobeetwotee.discord.Main;
import au.twobeetwotee.discord.command.Command;
import au.twobeetwotee.discord.util.Constants;
import au.twobeetwotee.discord.util.Util;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.io.IOException;
import java.util.UUID;

public class SeenCommand extends Command {

    public SeenCommand() {
        super("seen", "check when player was last seen", Category.SERVER);
//        addOption(OptionType.STRING, "name", "minecraft name");
        addOption(OptionType.STRING, "uuid", "minecraft uuid");
        addOption(OptionType.USER, "user", "discord user (must be verified)");
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        try {
            final var uuid = getUUIDFromOption(event);
            final var api = Main.getApi().getOfflinePlayer(uuid);

            final var embed = Util.defaultBuilder("Seen Player")
                    .setThumbnail(Constants.MINOTAR_BUST.formatted(api.getUuid()))
                    .addField("Minecraft IGN", api.getName(), false)
                    .addField("Last Seen", api.getName(), false)
                    .setImage(Constants.SERVER_URL)
                    .build();

            event.replyEmbeds(embed)
                    .setEphemeral(true)
                    .queue();
        } catch (Exception e) {
            event.reply(e.getMessage())
                    .setEphemeral(true)
                    .queue();
        }
    }

    private String formatLastPlayed(long lastPlayed) {
        return "";
    }

    private UUID getUUIDFromOption(SlashCommandInteractionEvent event) throws IOException {
        var option1 = event.getOption("uuid");
//        var option2 = event.getOption("name");
        var option3 = event.getOption("user");

        if (option1 != null) {
            return UUID.fromString(option1.getAsString());
        }

//        if (option2 != null) {
//            // TODO MojangAPI - Name -> UUID
//            return UUID.randomUUID();
//        }

        if (option3 != null) {
            var user = option3.getAsUser();
            var api = Main.getApi().getDiscordUUID(user.getIdLong());
            return UUID.fromString(api.getUuid());
        }

        throw new IOException("You must provide at least one option: uuid/name/user");
    }
}
