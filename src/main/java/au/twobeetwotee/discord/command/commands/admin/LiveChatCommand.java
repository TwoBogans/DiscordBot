package au.twobeetwotee.discord.command.commands.admin;

import au.twobeetwotee.discord.Main;
import au.twobeetwotee.discord.command.Command;
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
        addOption(OptionType.BOOLEAN, "remove", "Remove Live Chat Integration", false);
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        var option = event.getOption("channel");
        if (option != null) {
            var channel = option.getAsChannel();
            var manager = Main.getLiveChatManager();
            var remove = event.getOption("remove");

            if (remove != null && remove.getAsBoolean()) {
                var text = channel.asTextChannel();

                manager.removeLiveChat(channel.getGuild(), text);

                event.reply("Successfully removed live chat channel.")
                        .setEphemeral(true)
                        .queue();
                return;
            }

            try {
                var text = channel.asTextChannel();

                manager.registerNewLiveChat(channel.getGuild(), text, true);

                event.reply("Successfully registered new live chat channel! %s".formatted(text.getAsMention()))
                        .setEphemeral(true)
                        .queue();
            } catch (IllegalStateException e) {
                event.reply("Must be a text channel!")
                        .setEphemeral(true)
                        .queue();
            }
        }
    }
}
