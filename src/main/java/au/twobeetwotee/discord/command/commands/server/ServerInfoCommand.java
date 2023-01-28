package au.twobeetwotee.discord.command.commands.server;

import au.twobeetwotee.discord.Main;
import au.twobeetwotee.discord.command.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ServerInfoCommand extends Command {

    public ServerInfoCommand() {
        super("serverinfo", "Get Server Information for 2b2t.au", Category.SERVER);
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        try {
            final var api = Main.getApi();
            final var stats = api.getServerStats();
            final var perf = stats.getPerformance();

            event.reply(String.format("Players: %s, TPS: %s, Uptime: %s",
                            stats.getOnline(),
                            perf.getTps(),
                            stats.getUptime()))
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
