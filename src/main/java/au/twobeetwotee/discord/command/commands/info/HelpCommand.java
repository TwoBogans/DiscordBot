package au.twobeetwotee.discord.command.commands.info;

import au.twobeetwotee.discord.Main;
import au.twobeetwotee.discord.command.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.time.Instant;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "Get a list of commands.", Category.INFO);
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        var embed = new EmbedBuilder()
                .setColor(2263842)
                .setTitle("Information & Commands")
                .setDescription("\uD83D\uDCE8 admin@2b2t.au\n" + "\uD83D\uDD17 https://2b2t.au/")
                .setFooter("Australian Hausemaster", "https://imgur.com/v05uTf1.gif")
                .setTimestamp(Instant.now());

        var commands = Main.getCommandManager().getCommands();

        for (Command.Category category : Command.Category.values()) {
            var categoryCommands = commands.stream().filter(command -> command.getCategory() == category).toList();
            if (categoryCommands.isEmpty()) continue;

            var commandLabels = categoryCommands.stream().map(command -> "/%s".formatted(command.getName())).toList();
            var categoryContent = String.join("\n", commandLabels);
            var newField = new MessageEmbed.Field(category.name(), "```\n%s```".formatted(categoryContent), true);

            embed = embed.addField(newField);
        }

        event.replyEmbeds(embed.build())
                .setEphemeral(true)
                .queue();
    }
}
