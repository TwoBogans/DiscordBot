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
                    .setAuthor("Server Statistics", "https://cdn.discordapp.com/attachments/1035500596012261396/1066685050076282900/2b2t_au_logo.png")
                    .addField("Online", String.valueOf(stats.getOnline()), true)
                    .addField("Queue", String.valueOf(queue.getRegular() + queue.getPriority() + queue.getVeteran()), true)
                    .addField("TPS", String.valueOf(perf.getTps()), true)
                    .addField("Uptime", stats.getUptime(), true)
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
