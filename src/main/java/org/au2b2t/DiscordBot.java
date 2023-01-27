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
import org.au2b2t.global.commands.EmbedCommand;
import org.au2b2t.global.commands.HelpCommand;
import org.au2b2t.global.listeners.PrivateMessageListener;
import org.au2b2t.util.api.API;
import org.au2b2t.global.commands.BotInfoCommand;
import org.au2b2t.local.commands.VerifySetupCommand;
import org.au2b2t.util.Config;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.UUID;

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

        jda = JDABuilder
                .createDefault(config.getToken())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .setActivity(Activity.playing("2b2t.au"))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .build();

        jda.updateCommands()
                .addCommands(new VerifySetupCommand(), new BotInfoCommand(), new EmbedCommand(), new HelpCommand())
                .queue();

        jda.addEventListener(new PrivateMessageListener());

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

    public static UUID getUserMinecraftUUID(@NonNull User user) {
        try {
            var id = user.getIdLong();
            var response = api.getDiscordUUID(id);
            System.out.printf("ID: %s JSON: %s", id, response);
            if (!response.isSuccess()) return new UUID(0L, 0L);
            return UUID.fromString(response.getUuid());
        } catch (Exception e) {
            return new UUID(0L, 0L);
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
