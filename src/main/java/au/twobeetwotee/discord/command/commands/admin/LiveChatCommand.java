package au.twobeetwotee.discord.command.commands.admin;

import au.twobeetwotee.discord.Main;
import au.twobeetwotee.discord.command.Command;
import au.twobeetwotee.discord.listener.global.LiveChatListener;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class LiveChatCommand extends Command {

    public LiveChatCommand() {
        super("livechat", "setup live chat channel", Category.ADMIN);
        setGuildOnly(true);
        setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
        addOption(OptionType.CHANNEL, "channel", "Channel For Live Chat", true);
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        var option = event.getOption("channel");
        if (option != null) {
            var channel = option.getAsChannel();
            var listener = new LiveChatListener(channel.getGuild(), channel.asTextChannel());
            listener.startThread();
            Main.getJda().addEventListener(listener);
            event.reply("Success!")
                    .setEphemeral(true)
                    .queue();
        }
    }
}
