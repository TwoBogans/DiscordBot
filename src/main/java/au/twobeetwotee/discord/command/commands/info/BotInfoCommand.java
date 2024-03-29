package au.twobeetwotee.discord.command.commands.info;

import au.twobeetwotee.discord.Main;
import au.twobeetwotee.discord.command.Command;
import au.twobeetwotee.discord.util.Util;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.concurrent.atomic.AtomicInteger;

public class BotInfoCommand extends Command {

    public BotInfoCommand() {
        super("botinfo", "bot information (uptime, guilds, etc)", Category.INFO);
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        final var guildsCount = Main.getJda().getGuilds().size();
        final var totalMembers = new AtomicInteger(); Main.getJda().getGuilds().stream().map(Guild::getMemberCount).forEach(totalMembers::getAndAdd);
        final var embed = Util.defaultBuilder("Bot Information")
                .addField("Uptime", Util.getUptime(), true)
                .addField("Total Guilds", String.valueOf(guildsCount), true)
                .addField("Total Members", String.valueOf(totalMembers), true)
                .build();

        event.replyEmbeds(embed)
                .setEphemeral(true)
                .queue();
    }
}
