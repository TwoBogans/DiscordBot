package org.au2b2t;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.au2b2t.api.API;
import org.au2b2t.commands.EmbedCommand;
import org.au2b2t.listeners.JoinListener;
import org.au2b2t.listeners.MessageListener;
import org.au2b2t.util.Config;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public static void main(String[] args) {
        gson = new GsonBuilder().setPrettyPrinting().create();

        config = loadConfig();

        jda = JDABuilder
                .createDefault(config.getToken())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .setActivity(Activity.playing("2b2t.au"))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .build();

        jda.updateCommands()
                .addCommands(new EmbedCommand())
                .queue();

        api = new API();
    }

    public static boolean isUserVerified(@NonNull User user) {
        try {
            var id = user.getIdLong();
            var response = api.getDiscordRegistered(id);
            System.out.printf("ID: %s JSON: %s", id, response);
            return response.isSuccess() && response.isRegistered();
        } catch (Exception e) {
            return false;
        }
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
