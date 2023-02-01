package au.twobeetwotee.discord.command.commands.server;

import au.twobeetwotee.discord.command.Command;
import au.twobeetwotee.discord.listener.main.VerifyPlayerListener;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class VerifyCommand extends Command {

    public VerifyCommand() {
        super("verify", "link your minecraft account with discord", Category.SERVER);
        setGuildOnly(true);
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        VerifyPlayerListener.doVerify(event, event.getUser(), event.getGuild());
    }

}
