package au.twobeetwotee.discord.command.commands.admin;

import au.twobeetwotee.discord.command.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class LockdownCommand extends Command {

    public LockdownCommand() {
        super("lockdown", "Set all the channels to 18+", Category.ADMIN);
        setGuildOnly(true);
        setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
        addOption(OptionType.BOOLEAN, "remove", "Remove NSFW from all channels", true);
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        var guild = event.getGuild();
        if (guild == null) return;
        var option = event.getOption("remove");
        var nsfwBool = option != null && option.getAsBoolean();
        guild.getTextChannels().forEach(textChannel -> {
            textChannel.getManager().setNSFW(!nsfwBool).queue();
        });
        event.reply(!nsfwBool ? "Locked down Discord Successfully!" : "Removed Lockdown Successfully")
                .setEphemeral(true)
                .queue();
    }
}
