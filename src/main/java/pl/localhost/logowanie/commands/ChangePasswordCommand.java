package pl.localhost.logowanie.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.localhost.logowanie.database.User;
import pl.localhost.logowanie.managers.AuthManager;
import pl.localhost.logowanie.managers.SessionManager;
import pl.localhost.logowanie.security.PasswordManager;
import pl.localhost.logowanie.utils.MessageUtil;

public class ChangePasswordCommand implements CommandExecutor {
    private final AuthManager auth;
    private final SessionManager session;

    public ChangePasswordCommand(AuthManager auth, SessionManager session) {
        this.auth = auth;
        this.session = session;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("");
            return true;
        }

        Player p = (Player) sender;
        if (!session.isLoggedIn(p)) {
            MessageUtil.sendRawMessage(p, "&cMusisz byc zalogowany, aby zmienic haslo!");
            return true;
        }
        if (args.length != 2) {
            MessageUtil.sendRawMessage(p, "Uzycie: &b/changepassword <stare> <nowe>");
            return true;
        }

        User user = auth.getDb().getUser(p.getUniqueId());
        if (user == null) {
            MessageUtil.sendMessage(p, "not-registered");
            return true;
        }

        String oldPassword = args[0];
        String newPassword = args[1];

        if (!PasswordManager.checkPassword(oldPassword, user.getPasswordHash())) {
            MessageUtil.sendRawMessage(p, "&cStare haslo jest nieprawidlowe!");
            return true;
        }
        String newHash = PasswordManager.hashPassword(newPassword);
        user.setPasswordHash(newHash);
        auth.getDb().saveUser(user);

        MessageUtil.sendRawMessage(p, "&aTwoje haslo zostalo pomyslnie zmienione!");
        return true;
    }
}