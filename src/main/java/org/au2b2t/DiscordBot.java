package org.au2b2t;

import au.twobbeetwotee.api.API;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.au2b2t.global.commands.*;
import org.au2b2t.global.listeners.GuildJoinListener;
import org.au2b2t.global.listeners.MessageListener;
import org.au2b2t.local.commands.NewsButtonCommand;
import org.au2b2t.local.commands.VerifySetupCommand;
import org.au2b2t.util.Config;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

public class DiscordBot {

    @Getter
    private static JDA jda;
    @Getter
    private static API api;
    @Getter
    private static Gson gson;
    @Getter
    private static Config config;
    @Getter
    private static long startTime;

    public static void main(String[] args) {
        startTime = System.currentTimeMillis();

        gson = new GsonBuilder().setPrettyPrinting().create();

        config = loadConfig();

        api = new API();

        jda = JDABuilder
                .createDefault(config.getToken())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .setActivity(Activity.playing("2b2t.au"))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .build();

        jda.updateCommands()
                .addCommands(new VerifySetupCommand(), new BotInfoCommand(), new EmbedCommand(),
                        new HelpCommand(), new DiscordCommand(), new LockdownCommand(),
                        new ServerInfoCommand(), new SetupLiveChatCommand(), new NewsButtonCommand())
                .queue();

        jda.addEventListener(new MessageListener(), new GuildJoinListener());

        jda.getGuilds().forEach(guild -> {
            // Update Nickname
            var selfUser = DiscordBot.getJda().getSelfUser();
            guild.retrieveMember(selfUser).queue(member -> {
                guild.modifyNickname(member, "Australian Hausemaster").queue();
            });
        });
    }

    @SneakyThrows
    private static Config loadConfig() {
        var path = Paths.get("config.json");
        try {
            Reader reader = Files.newBufferedReader(path);

            Config config = gson.fromJson(reader, Config.class);

            reader.close();

            return config;
        } catch (IOException e) {
            var json = gson.toJson(new Config());
            Files.write(path, Collections.singleton(json));
            return loadConfig();
        }
    }
}
