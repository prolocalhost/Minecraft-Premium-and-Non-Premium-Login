package pl.localhost.logowanie.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.localhost.logowanie.managers.AuthManager;
import pl.localhost.logowanie.managers.SessionManager;
import pl.localhost.logowanie.utils.MessageUtil;

public class RegisterCommand implements CommandExecutor {
    private final AuthManager auth;
    private final SessionManager session;

    public RegisterCommand(AuthManager auth, SessionManager session) {
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

        if (auth.isRegistered(p.getUniqueId())) {
            MessageUtil.sendMessage(p, "already-registered");
            return true;
        }

        if (args.length != 2) {
            MessageUtil.sendRawMessage(p, "Uzycie: &b/register <haslo> <haslo>");
            return true;
        }

        if (!args[0].equals(args[1])) {
            MessageUtil.sendMessage(p, "passwords-not-match");
            return true;
        }

        auth.register(p, args[0]);
        return true;
    }
}