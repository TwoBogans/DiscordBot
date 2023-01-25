package org.au2b2t.api.responses;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

public class DiscordGetUUIDResponse {
    @Getter
    @Setter
    @Expose
    private boolean success;
    @Getter
    @Setter
    @Expose
    private String uuid;
}
