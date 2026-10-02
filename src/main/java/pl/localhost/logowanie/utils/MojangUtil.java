package pl.localhost.logowanie.utils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MojangUtil {

    private static final Map<String, Boolean> premiumCache = new ConcurrentHashMap<>();
    public static boolean isPremiumName(String name) {
        String lowerName = name.toLowerCase();
        if (premiumCache.containsKey(lowerName)) {
            return premiumCache.get(lowerName);
        }

        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(2500);
            connection.setReadTimeout(2500);

            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                premiumCache.put(lowerName, true);
                return true;
            } else if (responseCode == 204 || responseCode == 404) {
                premiumCache.put(lowerName, false);
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}