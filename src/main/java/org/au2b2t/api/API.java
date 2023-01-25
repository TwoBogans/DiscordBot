package org.au2b2t.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.au2b2t.api.responses.DiscordRegisteredResponse;

import java.io.IOException;

public class API {

    @Getter
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public DiscordRegisteredResponse getDiscordRegistered(long id) throws IOException {
        return gson.fromJson(Http.GET(String.format(Endpoints.DISCORD_REGISTERED, id)), DiscordRegisteredResponse.class);
    }

}
