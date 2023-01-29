package au.twobeetwotee.discord.command.commands.info;

import au.twobeetwotee.discord.Main;
import au.twobeetwotee.discord.command.Command;
import au.twobeetwotee.discord.util.Util;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Objects;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "list of available commands.", Category.INFO);
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        var embed = Util.defaultBuilder("Information & Commands")
                .setDescription("""
                        \uD83D\uDCE8 admin@2b2t.au
                        \uD83D\uDD17 https://2b2t.au/
                        \uD83D\uDD17 [main discord invite](https://discord.gg/popbob)
                        \uD83D\uDD17 [invite discord bot](%s)
                        ❤️ [like us on namemc?](https://namemc.com/server/2b2t.au)
                        """.formatted(Main.getJda().getInviteUrl(Permission.ADMINISTRATOR)));

        var commands = Main.getCommandManager().getCommands();
        var skipMain = Objects.requireNonNull(event.getGuild()).getIdLong() != Main.getConfig().getMainGuild();

        for (Command.Category category : Command.Category.values()) {
            if (skipMain && category == Category.MAIN) continue;
            var categoryCommands = commands.stream().filter(command -> command.getCategory() == category).toList();
            if (categoryCommands.isEmpty()) continue;

            var commandLabels = categoryCommands.stream().map(Command::toString).toList();
            var categoryContent = String.join("\n", commandLabels);
            var newField = new MessageEmbed.Field(category.name(), "```\n%s```".formatted(categoryContent), true);

            embed = embed.addField(newField);
        }

        event.replyEmbeds(embed.build())
                .setEphemeral(true)
                .queue();
    }
}
