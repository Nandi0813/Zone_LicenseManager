package dev.nandi0813.license.Auth;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AuthClient {

    private final String baseUrl;
    private final String apiKey;

    public AuthClient(String baseUrl, String apiKey) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    public JSONObject post(JSONObject payload) throws IOException {
        URL url = new URL(baseUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("x-api-key", apiKey);
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = payload.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int status = connection.getResponseCode();
        InputStream inputStream = (status < HttpURLConnection.HTTP_BAD_REQUEST)
                ? connection.getInputStream()
                : connection.getErrorStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line.trim());
        }

        reader.close();
        connection.disconnect();

        JSONObject jsonResponse = new JSONObject(response.toString());
        jsonResponse.put("http_status", status);

        return jsonResponse;
    }
}