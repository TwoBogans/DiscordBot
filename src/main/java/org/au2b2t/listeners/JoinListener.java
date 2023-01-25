package org.au2b2t.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.au2b2t.DiscordBot;
import org.au2b2t.api.API;
import org.au2b2t.api.responses.DiscordRegisteredResponse;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class JoinListener extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        try {
            final Guild guild = event.getGuild();
            long userId = event.getUser().getIdLong();

            DiscordRegisteredResponse response = DiscordBot.getApi().getDiscordRegistered(userId);

            if (response.isRegistered()) {
                Role role = guild.getRoleById("");

                if (role != null) {
                    guild.addRoleToMember(event.getUser(), role).queue();
                }
            }
        } catch (IOException ignored) {
            ignored.printStackTrace();
        }
    }
}
