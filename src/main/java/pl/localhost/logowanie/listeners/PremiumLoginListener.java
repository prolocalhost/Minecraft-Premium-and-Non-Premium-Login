package pl.localhost.logowanie.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import pl.localhost.logowanie.managers.AuthManager;

public class PremiumLoginListener implements Listener {
    private final AuthManager authManager;

    public PremiumLoginListener(AuthManager authManager) {
        this.authManager = authManager;
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent e) {
    }
}