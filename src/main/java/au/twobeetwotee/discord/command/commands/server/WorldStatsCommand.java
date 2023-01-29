package au.twobeetwotee.discord.command.commands.server;

import au.twobeetwotee.discord.Main;
import au.twobeetwotee.discord.command.Command;
import au.twobeetwotee.discord.util.Util;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class WorldStatsCommand extends Command {

    public WorldStatsCommand() {
        super("worldstats", "2b2t Australia World Statistics", Category.SERVER);
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        try {
            final var api = Main.getApi();
            final var stats = api.getWorldStats();
            final var embed = Util.defaultBuilder()
                    .setAuthor("World Statistics", "https://cdn.discordapp.com/attachments/1035500596012261396/1066685050076282900/2b2t_au_logo.png")
                    .addField("Joins", String.valueOf(stats.getPlayers()), true)
                    .addField("Size", String.format("%s GB", (int) stats.getSize()), true)
                    .addField("Age", String.format("%s Years, %s Months and %s Days.",
                            stats.getYears(),
                            stats.getMonths(),
                            stats.getDays()), false)
                    .setImage("https://media.discordapp.net/attachments/801552029625352294/1068917535103266876/image.png")
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
