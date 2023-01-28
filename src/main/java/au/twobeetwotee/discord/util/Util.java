package au.twobeetwotee.discord.util;

import au.twobeetwotee.discord.Main;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.NonNull;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

public class Util {

    /**
     * Converts a {@link JsonObject} to {@link Message}.
     * For use with <a href="https://discohook.org/">...</a>
     *
     * @param json JsonObject
     * @return Message
     */
    public static MessageCreateData jsonToMessage(JsonObject json){
        MessageCreateBuilder messageBuilder = new MessageCreateBuilder();

        JsonPrimitive contentObj = json.getAsJsonPrimitive("content");

        // Make sure the object is not null before adding it onto the embed.
        contentObj = Objects.requireNonNullElse(contentObj, new JsonPrimitive(""));

        messageBuilder.setContent(contentObj.getAsString());

        JsonArray embedsArray = json.getAsJsonArray("embeds");

        ArrayList<MessageEmbed> embeds = new ArrayList<>();

        // Make sure the array is not null before adding it onto the embed.
        embedsArray = Objects.requireNonNullElse(embedsArray, new JsonArray());

        // Loop over the embeds array and add each one by order to the message.
        embedsArray.forEach(embed -> embeds.add(jsonToEmbed(embed.getAsJsonObject())));

        messageBuilder.setEmbeds(embeds);

        return messageBuilder.build();
    }

    private static MessageEmbed jsonToEmbed(JsonObject json) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        JsonPrimitive titleObj = json.getAsJsonPrimitive("title");

        if (titleObj != null){ // Make sure the object is not null before adding it onto the embed.
            embedBuilder.setTitle(titleObj.getAsString());
        }

        JsonObject authorObj = json.getAsJsonObject("author");
        if (authorObj != null) {
            String authorName = authorObj.get("name").getAsString();
            String authorIconUrl = authorObj.get("icon_url").getAsString();
            if (authorIconUrl != null) // Make sure the icon_url is not null before adding it onto the embed. If its null then add just the author's name.
                embedBuilder.setAuthor(authorName, authorIconUrl);
            else
                embedBuilder.setAuthor(authorName);
        }

        JsonPrimitive descObj = json.getAsJsonPrimitive("description");
        if (descObj != null){
            embedBuilder.setDescription(descObj.getAsString());
        }

        JsonPrimitive colorObj = json.getAsJsonPrimitive("color");
        if (colorObj != null){
            Color color = new Color(colorObj.getAsInt());
            embedBuilder.setColor(color);
        }

        JsonArray fieldsArray = json.getAsJsonArray("fields");
        if (fieldsArray != null) {
            // Loop over the fields array and add each one by order to the embed.
            fieldsArray.forEach(ele -> {
                String name = ele.getAsJsonObject().get("name").getAsString();
                String content = ele.getAsJsonObject().get("value").getAsString();
                boolean inline = false;
                if (ele.getAsJsonObject().get("inline") != null) {
                    inline = ele.getAsJsonObject().get("inline").getAsBoolean();
                }
                embedBuilder.addField(name, content, inline);
            });
        }

        JsonObject thumbnailObj = json.getAsJsonObject("thumbnail");
        if (thumbnailObj != null){
            embedBuilder.setThumbnail(thumbnailObj.get("url").getAsString());
        }

        JsonObject imageObj = json.getAsJsonObject("image");
        if (imageObj != null){
            embedBuilder.setImage(imageObj.get("url").getAsString());
        }

        JsonObject footerObj = json.getAsJsonObject("footer");
        if (footerObj != null){
            String content = footerObj.get("text").getAsString();
            String footerIconUrl = footerObj.get("icon_url").getAsString();

            if (footerIconUrl != null)
                embedBuilder.setFooter(content, footerIconUrl);
            else
                embedBuilder.setFooter(content);
        }

        return embedBuilder.build();
    }

    public static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;

        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder buffer = new StringBuilder();

            int read;

            char[] chars = new char[1024];

            while ((read = reader.read(chars)) != -1) {
                buffer.append(chars, 0, read);
            }

            return buffer.toString();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public static String getUptime() {
        return TimeUtil.getRelativeTime(Main.getStartTime() / 1000, false, false);
    }

    public static void log(MessageEmbed... embeds) {
        final var config = Main.getConfig();
        final var guild = Main.getJda().getGuildById(config.getMainGuild()); // 2b2t au

        if (guild == null) return;

        final var logsChannel = guild.getChannelById(TextChannel.class, config.getLogsChannel()); // 2b2tau logs channel

        if (logsChannel == null) return;

        logsChannel.sendMessageEmbeds(Arrays.asList(embeds)).queue();
    }

    public static boolean isUserVerified(@NonNull User user) {
        try {
            var id = user.getIdLong();
            var response = Main.getApi().getDiscordRegistered(id);
            System.out.printf("ID: %s JSON: %s", id, response);
            return response.isSuccess() && response.isRegistered();
        } catch (Exception e) {
            return false;
        }
    }

    public static UUID getUserMinecraftUUID(@NonNull User user) {
        try {
            var id = user.getIdLong();
            var response = Main.getApi().getDiscordUUID(id);
            if (!response.isSuccess()) return new UUID(0L, 0L);
            return UUID.fromString(response.getUuid());
        } catch (Exception e) {
            return new UUID(0L, 0L);
        }
    }

    public static Emoji randomEmojiFromGuild(@NonNull Guild guild, Boolean animatedOnly) {
        final var random = new Random();
        final var emojiList = guild.getEmojis().stream().filter(richCustomEmoji -> animatedOnly && richCustomEmoji.isAnimated()).toList();
        return emojiList.get(random.nextInt(emojiList.size()));
    }

    /**
     * Creates new instance of a class by calling a constructor that receives ctorClassArgs arguments.
     *
     * @link <a href="https://github.com/Alluxio/alluxio/blob/master/core/common/src/main/java/alluxio/util/CommonUtils.java#L279">...</a>
     * @param <T> type of the object
     * @param cls the class to create
     * @param ctorClassArgs parameters type list of the constructor to initiate, if null default
     *        constructor will be called
     * @param ctorArgs the arguments to pass the constructor
     * @return new class object
     * @throws RuntimeException if the class cannot be instantiated
     */
    public static <T> T createNewClassInstance(Class<T> cls, Class<?>[] ctorClassArgs,
                                               Object[] ctorArgs) {
        try {
            if (ctorClassArgs == null) {
                return cls.newInstance();
            }
            Constructor<T> ctor = cls.getConstructor(ctorClassArgs);
            return ctor.newInstance(ctorArgs);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

}
