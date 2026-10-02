package pl.localhost.logowanie.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.localhost.logowanie.managers.AuthManager;
import pl.localhost.logowanie.managers.SessionManager;
import pl.localhost.logowanie.utils.MessageUtil;

public class LoginCommand implements CommandExecutor {
    private final AuthManager auth;
    private final SessionManager session;

    public LoginCommand(AuthManager auth, SessionManager session) {
        this.auth = auth;
        this.session = session;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player p = (Player) sender;

        if (session.isLoggedIn(p)) {
            MessageUtil.sendMessage(p, "already-logged-in");
            return true;
        }

        if (!auth.isRegistered(p.getUniqueId())) {
            MessageUtil.sendMessage(p, "not-registered");
            return true;
        }

        if (args.length != 1) {
            MessageUtil.sendRawMessage(p, "Uzycie: &b/login <haslo>");
            return true;
        }

        auth.login(p, args[0]);
        return true;
    }
}