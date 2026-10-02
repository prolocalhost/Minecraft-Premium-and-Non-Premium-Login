package pl.localhost.logowanie.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.localhost.logowanie.Main;
import pl.localhost.logowanie.database.User;
import pl.localhost.logowanie.managers.AuthManager;
import pl.localhost.logowanie.managers.SessionManager;
import pl.localhost.logowanie.utils.MessageUtil;
import pl.localhost.logowanie.utils.MojangUtil;

public class PlayerListener implements Listener {
    private final Main plugin;
    private final AuthManager auth;
    private final SessionManager session;

    public PlayerListener(Main plugin, AuthManager auth, SessionManager session) {
        this.plugin = plugin;
        this.auth = auth;
        this.session = session;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        session.addPlayer(p);
        String currentIp = p.getAddress().getAddress().getHostAddress();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            boolean isPremium = MojangUtil.isPremiumName(p.getName());
            Bukkit.getScheduler().runTask(plugin, () -> {
                if (!p.isOnline()) return;

                User user = auth.getDb().getUserByName(p.getName());

                if (isPremium) {
                    if (user == null) {
                        auth.registerPremiumAuto(p);
                    } else {
                        if (user.getIp().equals(currentIp)) {
                            auth.forceLogin(p);
                        } else {
                            MessageUtil.sendRawMessage(p, "&c&lUWAGA! &cWykryto zmiane adresu IP dla konta Premium.");
                            MessageUtil.sendRawMessage(p, "&7Wpisz swoje haslo: &b/login <haslo>");
                            MessageUtil.sendMessage(p, "login-request");
                        }
                    }
                } else {
                    if (user != null) {
                        MessageUtil.sendMessage(p, "login-request");
                    } else {
                        MessageUtil.sendMessage(p, "register-request");
                    }
                }
            });
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        session.removePlayer(e.getPlayer());
    }
}