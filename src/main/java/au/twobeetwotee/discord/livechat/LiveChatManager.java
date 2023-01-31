package au.twobeetwotee.discord.livechat;

import au.twobeetwotee.discord.Main;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.NonNull;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;

public class LiveChatManager {

    private final JDA jda;
    private final Gson gson;
    private final HashMap<Long, Long> guildMap; // [GuildID, GuildTextChanelID]
    private final HashMap<Long, LiveChatListener> listenerMap = new HashMap<>();
    private final String path = "livechats.json";

    public LiveChatManager() {
        jda = Main.getJda();
        gson = Main.getGson();
        guildMap = loadLiveChats();
        guildMap.forEach((guildId, textChannelId) -> {
            try {
                var guild = jda.getGuildById(guildId);
                var channel = jda.getTextChannelById(textChannelId);
                if (guild != null && channel != null) {
                    var newListener = new LiveChatListener(guild, channel);
                    listenerMap.put(guild.getIdLong(), newListener);
                } else {
                    System.out.printf("%s %s %s %s", guildId, textChannelId, guild, channel);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        listenerMap.values().forEach(LiveChatListener::init);
    }

    public void registerNewLiveChat(@NonNull Guild guild, @NonNull TextChannel textChannel) {
        var newListener = new LiveChatListener(guild, textChannel);

        guildMap.put(guild.getIdLong(), textChannel.getIdLong());
        jda.addEventListener(newListener);

        saveLiveChats();
    }

    @SneakyThrows
    public HashMap<Long, Long> loadLiveChats() {
        var path = Paths.get(this.path);
        try {
            var reader = Files.newBufferedReader(path);

            HashMap<Long, Long> hashMap = gson.fromJson(reader, new TypeToken<HashMap<Long, Long>>(){}.getType());

            reader.close();

            return hashMap;
        } catch (IOException e) {
            Files.write(path, Collections.singleton(gson.toJson(new HashMap<>())));
            return new HashMap<>();
        }
    }

    @SneakyThrows
    public void saveLiveChats() {
        var path = Paths.get(this.path);
        var json = gson.toJson(guildMap);
        Files.write(path, Collections.singleton(json));
    }

}