package org.au2b2t.api;

public class Endpoints {
    private static final String BASE_URL = "https://discord.2b2t.au/api";
    public static final String DISCORD = BASE_URL.concat("/discord");
    public static final String MINECRAFT = BASE_URL.concat("/minecraft");
    public static final String DISCORD_REGISTERED = DISCORD.concat("/isregistered?id=%s");
    public static final String DISCORD_GET_UUID = DISCORD.concat("/getuuid?id=%s");
    public static final String MINECRAFT_REGISTERED = MINECRAFT.concat("/isregistered?uuid=%s");
    public static final String MINECRAFT_GET_ID = MINECRAFT.concat("/getid?uuid=%s");
}