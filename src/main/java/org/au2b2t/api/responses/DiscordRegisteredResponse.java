package org.au2b2t.api.responses;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

public class DiscordRegisteredResponse {
    @Getter
    @Setter
    @Expose
    private boolean success;
    @Getter
    @Setter
    @Expose
    private boolean registered;

    @Override
    public String toString() {
        return "DiscordRegisteredResponse{" +
                "success=" + success +
                ", registered=" + registered +
                '}';
    }
}
