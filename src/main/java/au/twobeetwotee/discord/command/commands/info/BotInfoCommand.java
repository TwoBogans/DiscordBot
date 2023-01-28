package au.twobeetwotee.discord.command.commands.info;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import au.twobeetwotee.discord.Main;
import au.twobeetwotee.discord.command.Command;
import au.twobeetwotee.discord.util.Util;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

public class BotInfoCommand extends Command {

    public BotInfoCommand() {
        super("botinfo", "Get Bot Information", Category.INFO);
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        final var guildsCount = Main.getJda().getGuilds().size();
        final var totalMembers = new AtomicInteger(); Main.getJda().getGuilds().stream().map(Guild::getMemberCount).forEach(totalMembers::getAndAdd);
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
