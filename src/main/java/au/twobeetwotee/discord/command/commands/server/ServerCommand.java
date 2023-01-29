package au.twobeetwotee.discord.command.commands.server;

import au.twobeetwotee.discord.Main;
import au.twobeetwotee.discord.command.Command;
import au.twobeetwotee.discord.util.Util;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ServerCommand extends Command {

    public ServerCommand() {
        super("server", "2b2t Australia Server Statistics", Category.SERVER);
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        try {
            final var api = Main.getApi();
            final var queue = api.getQueueStats();
            final var stats = api.getServerStats();
            final var perf = stats.getPerformance();
            final var embed = Util.defaultBuilder()
                    .setAuthor("Server Statistics", null, "https://imgur.com/OkfrCr2.png")
                    .addField("Online", String.valueOf(stats.getOnline()), true)
                    .addField("Queue", String.valueOf(queue.getRegular() + queue.getPriority() + queue.getVeteran()), true)
                    .addField("TPS", String.valueOf(perf.getTps()), true)
                    .addField("Uptime", stats.getUptime(), true)
                    .setImage("https://api.loohpjames.com/serverbanner.png?ip=2b2t.au")
                    .build();

            event.replyEmbeds(embed)
                    .setEphemeral(true)
                    .queue();
        } catch (Exception e) {
            e.printStackTrace();
            event.reply("Server is offline! :/")
                    .setEphemeral(true)
                    .queue();
        }
    }
}
