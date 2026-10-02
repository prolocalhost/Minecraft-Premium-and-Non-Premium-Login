package pl.localhost.logowanie.managers;

import org.bukkit.entity.Player;
import pl.localhost.logowanie.database.User;
import pl.localhost.logowanie.security.PasswordManager;
import pl.localhost.logowanie.utils.MessageUtil;

import java.util.UUID;

public class AuthManager {
    private final DatabaseManager db;
    private final SessionManager session;
    private final ConfigManager config;

    public AuthManager(DatabaseManager db, SessionManager session, ConfigManager config) {
        this.db = db;
        this.session = session;
        this.config = config;
    }

    public boolean isRegistered(UUID uuid) {
        return db.getUser(uuid) != null;
    }

    public void register(Player player, String password) {
        String hash = PasswordManager.hashPassword(password);
        User user = new User(player.getUniqueId(), player.getName(), hash, false, player.getAddress().getAddress().getHostAddress());
        db.saveUser(user);
        session.loginPlayer(player);
        MessageUtil.sendMessage(player, "register-success");
    }

    public void registerPremiumAuto(Player player) {
        String backupPassword = UUID.randomUUID().toString().substring(0, 8);
        String hash = PasswordManager.hashPassword(backupPassword);

        User user = new User(player.getUniqueId(), player.getName(), hash, true, player.getAddress().getAddress().getHostAddress());
        db.saveUser(user);
        session.loginPlayer(player);

        MessageUtil.sendMessage(player, "premium-auto-login");
        MessageUtil.sendRawMessage(player, "&7Zarejestrowano bezpiecznie przez API Mojang.");
        MessageUtil.sendRawMessage(player, "&7Twoje awaryjne haslo (w razie zmiany IP): &b" + backupPassword);
    }

    public void login(Player player, String password) {
        User user = db.getUserByName(player.getName());
        if (user == null) {
            MessageUtil.sendMessage(player, "not-registered");
            return;
        }

        if (PasswordManager.checkPassword(password, user.getPasswordHash())) {
            User updatedUser = new User(user.getUuid(), user.getName(), user.getPasswordHash(), user.isPremium(), player.getAddress().getAddress().getHostAddress());
            db.saveUser(updatedUser);

            session.loginPlayer(player);
            MessageUtil.sendMessage(player, "login-success");
        } else {
            session.addAttempt(player);
            MessageUtil.sendMessage(player, "wrong-password");
        }
    }

    public void forceLogin(Player player) {
        session.loginPlayer(player);
        MessageUtil.sendMessage(player, "premium-auto-login");
    }

    public DatabaseManager getDb() { return db; }
}