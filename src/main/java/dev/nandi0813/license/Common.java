package dev.nandi0813.license;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class Common {

    public static void sendMessage(Audience audience, String string) {
        audience.sendMessage(MiniMessage.miniMessage().deserialize(string));
    }

}
