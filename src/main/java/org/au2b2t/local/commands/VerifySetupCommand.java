package org.au2b2t.local.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.au2b2t.DiscordBot;
import org.au2b2t.local.listeners.VerifyPlayerListener;

public class VerifySetupCommand extends CommandDataImpl {

    public VerifySetupCommand() {
        super("verifysetup", "Setup Verify Button on Message ID");
        setGuildOnly(true);
        setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
        addOption(OptionType.STRING, "messageid", "ID of Message to add Verify Button", true);
        DiscordBot.getJda().addEventListener(new Listener());
        DiscordBot.getJda().addEventListener(new VerifyPlayerListener());
    }

    protected static class Listener extends ListenerAdapter {

        @Override
        public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
            if (event.getGuild().getIdLong() != DiscordBot.getConfig().getMainGuild()) {
                event.reply("discord.gg/popbob only!")
                        .setEphemeral(true)
                        .queue();
                return;
            }

            if (event.getName().equalsIgnoreCase("verifysetup")) {
                var option = event.getOption("messageid");
                if (option != null) {
                    try {
                        final var verifyButton = Button.primary("verify", "Verify Player").withEmoji(Emoji.fromFormatted("<:hausecool:838708181273804851>"));
                        event.getChannel()
                                .retrieveMessageById(option.getAsString())
                                .queue(message -> message.editMessageComponents(ActionRow.of(verifyButton))
                                .queue(success -> {
                                    event.reply("Verify Button Sucessfully Added!")
                                            .queue();
                                }));
                        return;
                    } catch (Exception e) {
                        event.reply(String.format("Failed with exception:%n%n%s", e.getMessage()))
                                .setEphemeral(true)
                                .queue();
                        return;
                    }
                }

                event.reply("Invalid Message ID?")
                        .setEphemeral(true)
                        .queue();
            }
        }
    }
}
