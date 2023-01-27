package org.au2b2t.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.au2b2t.DiscordBot;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

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
        return TimeUtil.getRelativeTime(DiscordBot.getStartTime() / 1000, false, false);
    }

}
