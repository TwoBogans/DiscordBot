package au.twobeetwotee.discord.livechat;

import au.twobeetwotee.discord.Main;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

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
            if (guild != null) {
                var channel = guild.getTextChannelById(textChannelId);
                if (channel != null) {
                    registerNewLiveChat(guild, channel, false);
                }
            }
        });

        saveLiveChats();

        jda.addEventListener(new Listener(this));

        System.out.println(gson.toJson(guildMap));
    }

    @RequiredArgsConstructor
    protected static class Listener extends ListenerAdapter {
        @NonNull LiveChatManager manager;
        @Override
        public void onChannelDelete(@NotNull ChannelDeleteEvent event) {
            if (!event.isFromType(ChannelType.TEXT)) return;
            if (manager.listenerMap.containsKey(event.getGuild().getIdLong())) {
                manager.removeLiveChat(event.getGuild(), event.getChannel().asTextChannel());
            }
        }
    }

    public void removeLiveChat(@NonNull LiveChatListener listener) {
        removeLiveChat(listener.getGuild(), listener.getGuildChannel());
    }

    public void removeLiveChat(@NonNull Guild guild, @NonNull TextChannel textChannel) {
        var id = guild.getIdLong();
        var listener = listenerMap.get(id);

        // Remove Listeners
        jda.removeEventListener(listener.getListener());
        listener.getThread().stop();

        // Remove From Memory
        guildMap.remove(id, textChannel.getIdLong());
        listenerMap.remove(id);

        // Save File
        saveLiveChats();

        // Debug
        System.out.printf("Removed Live Chat: %s %s", guild.getName(), textChannel.getName());
    }

    public void registerNewLiveChat(@NonNull Guild guild, @NonNull TextChannel textChannel, boolean save) {
        var id = guild.getIdLong();

        if (listenerMap.containsKey(id)) {
            removeLiveChat(listenerMap.get(id));
        }

        var newListener = new LiveChatListener(guild, textChannel);
        var newId = newListener.getGuild().getIdLong();
        var newChannel = newListener.getGuildChannel().getIdLong();

        guildMap.put(newId, newChannel);
        listenerMap.put(newId, newListener);

        System.out.printf("Registered Live Chat: %s %s", guild.getIdLong(), newListener);

        if (save) {
            saveLiveChats();
        }
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
