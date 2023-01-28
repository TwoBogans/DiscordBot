package au.twobeetwotee.discord.util;

import lombok.Getter;

@Getter
public class Config {

    private final String token;
    private final long userRole;
    private final long logsChannel;
    private final long boosterChannel;
    private final long mainGuild;

    public Config() {
        this.token = "";
        this.userRole = 0L;
        this.logsChannel = 0L;
        this.boosterChannel = 0L;
        this.mainGuild = 0L;
    }
}
