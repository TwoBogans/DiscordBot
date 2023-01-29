package au.twobeetwotee.discord.command.commands.info;

import au.twobbeetwotee.api.responses.BalanceResponse;
import au.twobeetwotee.discord.Main;
import au.twobeetwotee.discord.command.Command;
import au.twobeetwotee.discord.util.Util;
import lombok.NonNull;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.io.IOException;

public class DonateCommand extends Command {
    public DonateCommand() {
        super("donate", "keep 2b2t australia running!", Category.INFO);
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        var bal = getBalance();
        var embed = Util.defaultBuilder("Donate Information")
                .setDescription("""
                        Server Bill: **%s/%s %S**
                        > Due: 1st %s
                        """.formatted(
                        bal.getWallet(),
                        bal.getCosts(),
                        bal.getCurrency(),
                        bal.getMonth()
                )).addField(
                        "Donate",
                        "https://donate.2b2t.au\nhttps://shop.2b2t.au",
                        false
                );

        event.replyEmbeds(embed.build())
                .setEphemeral(true)
                .queue();
    }

    @NonNull
    private BalanceResponse getBalance() {
        try {
            return Main.getApi().getBalance();
        } catch (IOException e) {
            return new BalanceResponse();
        }
    }
}
