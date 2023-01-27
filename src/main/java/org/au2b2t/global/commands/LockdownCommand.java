package org.au2b2t.global.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.au2b2t.DiscordBot;

public class LockdownCommand extends CommandDataImpl {

    public LockdownCommand() {
        super("lockdown", "Set all the channels to 18+");
        setGuildOnly(true);
        addOption(OptionType.BOOLEAN, "remove", "Remove NSFW from all channels", true);
        setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
        DiscordBot.getJda().addEventListener(new Listener());
    }

    protected static class Listener extends ListenerAdapter {

        @Override
        public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
            if (event.getName().equalsIgnoreCase("lockdown")) {
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
    }
}
