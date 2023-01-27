package org.au2b2t.global.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.au2b2t.DiscordBot;
import org.au2b2t.util.Util;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class GuildJoinListener extends ListenerAdapter {

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        final var guild = event.getGuild();
        final var embed = new EmbedBuilder()
                .setColor(2263842)
                .setTitle("Guild Joined")
                .setThumbnail(guild.getIconUrl())
//                .setDescription(getFirstInvite(guild))
                .addField("Guild Name", guild.getName(), true)
                .addField("Member Count", String.valueOf(guild.getMemberCount()), true)
//                .addField("Boost Count", String.valueOf(guild.getBoostCount()), true)
//                .setFooter(getGuildOwner(guild).getAsTag(), getGuildOwner(guild).getAvatarUrl())
                .setTimestamp(Instant.now())
                .build();

        Util.log(embed);

        System.out.printf("[%s] joined guild.", guild.getName());
        System.out.printf("[%s] total guilds.", DiscordBot.getJda().getGuilds().stream().map(Guild::getName).collect(Collectors.joining(", ")));

        // Update Nickname
        guild.retrieveMember(DiscordBot.getJda().getSelfUser()).queue(member -> {
            guild.modifyNickname(member, "Australian Hausemaster").queue();
        });
    }

    private String getFirstInvite(Guild guild) {
        final var str = new AtomicReference<>("");
        guild.retrieveInvites()
                .queue(invites -> invites
                .stream()
                .findFirst()
                .ifPresent(invite -> str.set(invite.getUrl())));
        return str.get();
    }

    private User getGuildOwner(Guild guild) {
        final var user = new AtomicReference<User>();
        guild.retrieveOwner().queue(member -> {
            user.set(member.getUser());
        });
        return user.get();
    }
}
