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
    public static int EMBED_COLOR = 2263842;
}
