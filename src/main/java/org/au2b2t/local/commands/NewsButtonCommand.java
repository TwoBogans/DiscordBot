package org.au2b2t.local.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.au2b2t.DiscordBot;

import java.util.ArrayList;
import java.util.Objects;

public class NewsButtonCommand extends CommandDataImpl {

    public NewsButtonCommand() {
        super("newsbutton", "Add News Button to Message ID");
        setGuildOnly(true);
        setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
        addOption(OptionType.STRING, "messageid", "ID of Message to add News Button", true);
        DiscordBot.getJda().addEventListener(new Listener());
    }

    protected static class Listener extends ListenerAdapter {

        @Override
        public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
            if (event.getName().equalsIgnoreCase("newsbutton")) {
                if (Objects.requireNonNull(event.getGuild()).getIdLong() != DiscordBot.getConfig().getMainGuild()) {
                    event.reply("discord.gg/popbob only!")
                            .setEphemeral(true)
                            .queue();
                    return;
                }

                var option = event.getOption("messageid");
                if (option != null) {
                    try {
                        final var newsButton = Button.primary("news", "News").withEmoji(Emoji.fromFormatted("\uD83D\uDDDE️"));
                        event.getChannel()
                                .retrieveMessageById(option.getAsString())
                                .queue(message -> {
                                    var currentComponents = new ArrayList<>(message.getComponents());
                                    currentComponents.add(ActionRow.of(newsButton));
                                    message.editMessageComponents(currentComponents).queue(success -> {
                                        event.reply("News Button Sucessfully Added!")
                                                .setEphemeral(true)
                                                .queue();
                                    });
                                });
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
