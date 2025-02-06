package dev.nandi0813.license.Auth;

import dev.nandi0813.license.Common;
import dev.nandi0813.license.Util.Hwid;
import dev.nandi0813.license.Util.IP;
import dev.nandi0813.license.Util.ValidationType;
import net.kyori.adventure.audience.Audience;
import org.bukkit.plugin.java.JavaPlugin;

public class Auth {

    private final JavaPlugin plugin;
    private final Audience audience;
    private final LicenseValidator licenseValidator;

    public Auth(JavaPlugin plugin, Audience audience, String baseUrl, String apiKey) {
        this.plugin = plugin;
        this.licenseValidator = new LicenseValidator(this, baseUrl, apiKey);
        this.audience = audience;
    }

    public boolean authenticate(final String productName, final String productVersion, final String serverVersion, final ValidationType validationType) {
        try {
            return licenseValidator.validateLicense(
                    productName,
                    productVersion,
                    serverVersion,
                    validationType,
                    Hwid.getCurrentHwid(),
                    IP.getCurrentIp()
            );
        } catch (Exception e) {
            Common.sendMessage(audience, "<gray>[<gold>" + productName + "<gray>] <red>An unexpected error occurred: " + e.getMessage());
            return false;
        }
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public Audience getAudience() {
        return audience;
    }

}