package dev.nandi0813.license.Util;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class IP {

    public static @Nullable String getCurrentIp() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader((new URL("http://checkip.amazonaws.com")).openStream()))) {
            return reader.readLine();
        } catch (IOException var14) {
            return null;
        }
    }

}
