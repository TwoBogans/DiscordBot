package au.twobeetwotee.discord.command.commands.main;

import au.twobeetwotee.discord.command.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;

public class NewsButtonCommand extends Command {

    public NewsButtonCommand() {
        super("newsbutton", "Add News Button to Message ID", Category.MAIN);
        setGuildOnly(true);
        setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
        addOption(OptionType.STRING, "messageid", "ID of Message to add News Button", true);
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        var option = event.getOption("messageid");
        if (option != null) {
            try {
                final var newsButton = Button.primary("news", "News").withEmoji(Emoji.fromFormatted("\uD83D\uDDDE️"));
                event.getChannel()
                        .retrieveMessageById(option.getAsString())
                        .queue(message -> {
                            var currentComponents = new ArrayList<>(message.getComponents());
                            currentComponents.stream().filter(itemComponents -> itemComponents instanceof ActionRow)
                                    .findFirst().ifPresent(item -> item.getButtons().add(newsButton));
//                                    currentComponents.add(ActionRow.of(newsButton));
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
