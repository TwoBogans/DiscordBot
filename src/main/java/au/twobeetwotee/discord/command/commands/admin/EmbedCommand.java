package au.twobeetwotee.discord.command.commands.admin;

import au.twobeetwotee.discord.command.Command;
import au.twobeetwotee.discord.util.Util;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.io.StringReader;

public class EmbedCommand extends Command {

    public EmbedCommand() {
        super("embed", "send json url as message/embed", Category.ADMIN);
        setGuildOnly(true);
        setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR));
        addOption(OptionType.STRING, "url", "raw json url (pastebin, etc)", true);
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        var option = event.getOption("url");
        if (option != null) {
            try {
                var url = option.getAsString();
                var json = Util.readUrl(url);
                var reader = new JsonReader(new StringReader(json));

                reader.setLenient(true);

                var data = Util.jsonToMessage(JsonParser.parseReader(reader).getAsJsonObject());
                var message = event.getChannel().sendMessage(data);

                message.queue(success -> event.reply("Embed Successfully Sent.").setEphemeral(true).queue());

                reader.close();

                return;
            } catch (Exception e) {
                event.reply(String.format("Failed with exception:%n%n%s", e.getMessage()))
                        .setEphemeral(true)
                        .queue();
                return;
            }
        }

        event.reply("Invalid JSON URL?")
                .setEphemeral(true)
                .queue();
    }
}
