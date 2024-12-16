package dev.nandi0813.license.Auth;

import dev.nandi0813.license.Common;
import dev.nandi0813.license.Util.ConfigFile;
import dev.nandi0813.license.Util.PlatformID;
import org.json.JSONObject;

public class LicenseValidator extends ConfigFile {

    private final Auth auth;
    private final AuthClient authClient;

    private String platformId = PlatformID.getPlatformID();
    private String licenseKey = null;
    private String tempPassword = null;

    public LicenseValidator(Auth auth, String baseUrl, String apiKey) {
        super(auth.getPlugin(), "", "license");

        this.auth = auth;
        this.authClient = new AuthClient(baseUrl, apiKey);
        
        this.reloadFile();
        this.getData();
    }

    @Override
    public void setData() {
        if (platformId != null) {
            this.set("platform-id", platformId);
        }

        if (licenseKey != null) {
            this.set("license-key", licenseKey);
        }

        if (tempPassword != null) {
            this.set("temp-password", tempPassword);
        }

        this.saveFile();
    }

    @Override
    public void getData() {
        if (this.isSet("platform-id")) {
            this.platformId = (String) this.get("platform-id");
        }

        if (this.isSet("license-key")) {
            this.licenseKey = (String) this.get("license-key");
        }

        if (this.isSet("temp-password")) {
            this.tempPassword = (String) this.get("temp-password");
        }
    }

    public boolean validateLicense(String productName, String productVersion, String serverId, String ipAddress) {
        try {
            JSONObject payload = new JSONObject();
            payload.put("platform_id", platformId);
            payload.put("license_key", licenseKey);
            payload.put("product_name", productName);
            payload.put("product_version", productVersion);
            payload.put("server_id", serverId);
            payload.put("ip_address", ipAddress);

            JSONObject response = authClient.post(payload);
            return handleResponse(response, productName);
        } catch (Exception e) {
            Common.sendMessage(auth.getAudience(), "<gray><st>---------------------------------");
            Common.sendMessage(auth.getAudience(), "<gray>[<gold>" + productName + "<gray>] <red>Unexpected error occurred. Conntact support on discord. Message: " + e.getMessage());
            Common.sendMessage(auth.getAudience(), "<gray><st>---------------------------------");
            return false;
        }
    }

    private boolean handleResponse(JSONObject response, String productName) {
        String statusCode = response.optString("status_code", "UNKNOWN");
        String message = response.optString("message", "No message provided.");

        switch (statusCode) {
            case "LICENSE_VALIDATED":
                Common.sendMessage(auth.getAudience(), "<gray>[<gold>" + productName + "<gray>] <green>License validated successfully!");
                return true;

            case "USER_NOT_AUTHORIZED":
                String licenseKey = response.optString("license_key", null);
                String tempPassword = response.optString("temporary_password", null);

                if (licenseKey != null) {
                    this.licenseKey = licenseKey;
                }

                if (tempPassword != null) {
                    this.tempPassword = tempPassword;
                }

                this.setData();

                Common.sendMessage(auth.getAudience(), "<gray><st>---------------------------------");
                Common.sendMessage(auth.getAudience(), "<gray>[<gold>" + productName + "<gray>] <red>" + message);
                Common.sendMessage(auth.getAudience(), "<yellow>License Key: <white>" + (this.licenseKey != null ? this.licenseKey : "N/A"));
                Common.sendMessage(auth.getAudience(), "<yellow>Temporary Password: <white>" + (this.tempPassword != null ? this.tempPassword : "N/A"));
                Common.sendMessage(auth.getAudience(), "<gray><st>---------------------------------");
                return false;

            default:
                Common.sendMessage(auth.getAudience(), "<gray><st>---------------------------------");
                Common.sendMessage(auth.getAudience(), "<gray>[<gold>" + productName + "<gray>] <red>" + message);
                Common.sendMessage(auth.getAudience(), "<gray><st>---------------------------------");
                return false;
        }
    }
}