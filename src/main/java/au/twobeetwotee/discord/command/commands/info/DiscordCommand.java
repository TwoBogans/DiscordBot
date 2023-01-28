package au.twobeetwotee.discord.command.commands.info;

import au.twobeetwotee.discord.command.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class DiscordCommand extends Command {

    public DiscordCommand() {
        super("discord", "2b2t Australia Discord Invite", Category.INFO);
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        event.reply("discord.gg/popbob")
                .setEphemeral(true)
                .queue();
    }
}
