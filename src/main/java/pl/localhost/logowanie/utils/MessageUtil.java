package pl.localhost.logowanie.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import pl.localhost.logowanie.managers.ConfigManager;

public class MessageUtil {
    private static ConfigManager config;

    public static void init(ConfigManager configManager) {
        config = configManager;
    }

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static void sendMessage(CommandSender sender, String key) {
        if (config == null) return;
        String prefix = config.getMessages().getString("prefix", "");
        String msg = config.getMessages().getString(key, "Brak wiadomosci: " + key);
        sender.sendMessage(color(prefix + msg));
    }

    public static void sendRawMessage(CommandSender sender, String msg) {
        if (config == null) return;
        String prefix = config.getMessages().getString("prefix", "");
        sender.sendMessage(color(prefix + msg));
    }
}