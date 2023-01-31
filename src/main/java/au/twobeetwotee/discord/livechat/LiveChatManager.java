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
            var guild = jda.getGuildById(guildId);
            var channel = jda.getTextChannelById(textChannelId);
            System.out.printf("%s %s %s %s", guild, channel, guildId, textChannelId);
            if (guild != null && channel != null) {
                var newListener = new LiveChatListener(guild, channel, jda);
                listenerMap.put(guild.getIdLong(), newListener);
                System.out.printf("Added to listener map %s %s",
                        guild.getIdLong(),
                        newListener
                );
            }
        });

        listenerMap.values().forEach(liveChatListener -> {
            System.out.printf("Started live chat for %s %s",
                    liveChatListener.getGuild().getName(),
                    liveChatListener.getGuildChannel().getName()
                );
        });

        System.out.println(gson.toJson(guildMap));
    }

    public void registerNewLiveChat(@NonNull Guild guild, @NonNull TextChannel textChannel) {
        var newListener = new LiveChatListener(guild, textChannel, jda);
        guildMap.put(newListener.getGuild().getIdLong(), newListener.getGuildChannel().getIdLong());
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
