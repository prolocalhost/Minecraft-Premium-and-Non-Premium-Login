package pl.localhost.logowanie.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.localhost.logowanie.Main;
import pl.localhost.logowanie.utils.MessageUtil;

import java.io.File;

public class ConfigManager {
    private final Main plugin;
    private FileConfiguration config;
    private FileConfiguration messages;

    public ConfigManager(Main plugin) {
        this.plugin = plugin;
        loadFiles();
        MessageUtil.init(this);
    }

    public void loadFiles() {
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();

        File msgFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!msgFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        this.messages = YamlConfiguration.loadConfiguration(msgFile);
    }

    public FileConfiguration getConfig() { return config; }
    public FileConfiguration getMessages() { return messages; }
}