package pl.localhost.logowanie.managers;

import org.bukkit.entity.Player;
import pl.localhost.logowanie.Main;
import pl.localhost.logowanie.utils.MessageUtil;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private final Main plugin;
    private final Map<UUID, Boolean> loggedIn = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> loginAttempts = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> tasks = new ConcurrentHashMap<>();

    public SessionManager(Main plugin) { this.plugin = plugin; }

    public void addPlayer(Player player) {
        loggedIn.put(player.getUniqueId(), false);
        loginAttempts.put(player.getUniqueId(), 0);

        int timeout = plugin.getConfigManager().getConfig().getInt("security.timeout-seconds", 60);
        int taskId = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (!isLoggedIn(player)) {
                player.kickPlayer(MessageUtil.color(plugin.getConfigManager().getMessages().getString("timeout-kick")));
            }
        }, timeout * 20L);
        tasks.put(player.getUniqueId(), taskId);
    }

    public void removePlayer(Player player) {
        UUID uuid = player.getUniqueId();
        loggedIn.remove(uuid);
        loginAttempts.remove(uuid);
        if (tasks.containsKey(uuid)) {
            plugin.getServer().getScheduler().cancelTask(tasks.remove(uuid));
        }
    }

    public void loginPlayer(Player player) {
        loggedIn.put(player.getUniqueId(), true);
        if (tasks.containsKey(player.getUniqueId())) {
            plugin.getServer().getScheduler().cancelTask(tasks.remove(player.getUniqueId()));
        }
    }

    public boolean isLoggedIn(Player player) {
        return loggedIn.getOrDefault(player.getUniqueId(), false);
    }

    public void addAttempt(Player player) {
        int attempts = loginAttempts.getOrDefault(player.getUniqueId(), 0) + 1;
        loginAttempts.put(player.getUniqueId(), attempts);

        int max = plugin.getConfigManager().getConfig().getInt("security.max-login-attempts", 3);
        if (attempts >= max && plugin.getConfigManager().getConfig().getBoolean("security.kick-on-max-attempts", true)) {
            plugin.getServer().getScheduler().runTask(plugin, () ->
                    player.kickPlayer(MessageUtil.color(plugin.getConfigManager().getMessages().getString("max-attempts-kick")))
            );
        }
    }
}