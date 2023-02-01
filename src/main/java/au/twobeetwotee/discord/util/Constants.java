package au.twobeetwotee.discord.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public static String WEBSITE = "https://2b2t.au";
    public static String LOGO_URL = WEBSITE.concat("/New_2b2t_Australia_Logo.png");
    public static String LOGO_SQUARE_URL = WEBSITE.concat("/2b2t_Australia_Logo_Square.png");
    public static String ADMIN = "Australian Hausemaster";
    public static String ADMIN_URL = WEBSITE.concat("/")
            .concat(ADMIN.replaceAll(" ", "_"))
            .concat(".gif");
    public static String SERVER_URL = "https://api.loohpjames.com/serverbanner.png?ip=2b2t.au";
    public static String MINOTAR_BODY = "https://minotar.net/armor/body/%s/100.png";
    public static String MINOTAR_BUST = "https://minotar.net/armor/bust/%s/100.png";
    public static String MINOTAR_HEAD = "https://minotar.net/avatar/%s/100.png";
    public static String NAMEMC_URL = "https://namemc.com/search?q=%s";
    public static int EMBED_COLOR = 2263842;
}
