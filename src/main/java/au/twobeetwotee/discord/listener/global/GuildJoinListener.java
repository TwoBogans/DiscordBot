package au.twobeetwotee.discord.listener.global;

import au.twobeetwotee.discord.Main;
import au.twobeetwotee.discord.util.Util;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.stream.Collectors;

public class GuildJoinListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        final var guild = event.getGuild();
        final var embed = Util.defaultBuilder()
                .setTitle("Guild Joined")
                .setThumbnail(guild.getIconUrl())
                .addField("Guild Name", guild.getName(), true)
                .addField("Member Count", String.valueOf(guild.getMemberCount()), true)
                .build();

        Util.log(embed);

        System.out.printf("[%s] joined guild.", guild.getName());
        System.out.printf("[%s] total guilds.", Main.getJda().getGuilds().stream().map(Guild::getName).collect(Collectors.joining(", ")));

        // Update Nickname
        guild.retrieveMember(Main.getJda().getSelfUser()).queue(member -> {
            guild.modifyNickname(member, "Australian Hausemaster").queue();
        });
    }
}
