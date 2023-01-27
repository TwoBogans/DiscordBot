package org.au2b2t.util.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.au2b2t.util.api.responses.DiscordGetUUIDResponse;
import org.au2b2t.util.api.responses.DiscordRegisteredResponse;

import java.io.IOException;

// TODO Add methods for all api.2b2t.au endpoints
public class API {

    @Getter
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public DiscordRegisteredResponse getDiscordRegistered(long id) throws IOException {
        return gson.fromJson(Http.GET(String.format(Endpoints.DISCORD_REGISTERED, id)), DiscordRegisteredResponse.class);
    }

    public DiscordGetUUIDResponse getDiscordUUID(long id) throws IOException {
        return gson.fromJson(Http.GET(String.format(Endpoints.DISCORD_GET_UUID, id)), DiscordGetUUIDResponse.class);
    }

}
