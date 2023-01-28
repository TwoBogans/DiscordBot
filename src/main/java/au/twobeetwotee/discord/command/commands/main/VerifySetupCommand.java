package au.twobeetwotee.discord.command.commands.main;

import au.twobeetwotee.discord.Main;
import au.twobeetwotee.discord.command.Command;
import au.twobeetwotee.discord.listener.main.VerifyPlayerListener;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class VerifySetupCommand extends Command {

    public VerifySetupCommand() {
        super("verifysetup", "Setup Verify Button on Message ID", Category.MAIN);
        setGuildOnly(true);
        setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
        addOption(OptionType.STRING, "messageid", "ID of Message to add Verify Button", true);
        Main.getJda().addEventListener(new VerifyPlayerListener());
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        var option = event.getOption("messageid");
        if (option != null) {
            try {
                final var verifyButton = Button.primary("verify", "Verify Player").withEmoji(Emoji.fromFormatted("<:hausecool:838708181273804851>"));
                event.getChannel()
                        .retrieveMessageById(option.getAsString())
                        .queue(message -> message.editMessageComponents(ActionRow.of(verifyButton))
                                .queue(success -> {
                                    event.reply("Verify Button Sucessfully Added!")
                                            .setEphemeral(true)
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
