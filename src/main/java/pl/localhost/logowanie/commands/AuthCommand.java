package pl.localhost.logowanie.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.localhost.logowanie.Main;
import pl.localhost.logowanie.database.User;
import pl.localhost.logowanie.managers.AuthManager;
import pl.localhost.logowanie.utils.MessageUtil;

public class AuthCommand implements CommandExecutor {
    private final Main plugin;
    private final AuthManager auth;

    public AuthCommand(Main plugin, AuthManager auth) {
        this.plugin = plugin;
        this.auth = auth;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("unregister")) {
            if (!(sender instanceof Player)) return true;
            Player p = (Player) sender;
            if (!auth.isRegistered(p.getUniqueId())) {
                MessageUtil.sendMessage(p, "not-registered");
                return true;
            }
            auth.getDb().deleteUser(p.getUniqueId());
            p.kickPlayer("§cTwoje konto zostalo usuniete.");
            return true;
        }

        if (!sender.hasPermission("auth.admin")) {
            MessageUtil.sendMessage(sender, "no-permission");
            return true;
        }

        if (args.length == 0) {
            MessageUtil.sendRawMessage(sender, "&8--- &bPomoc Admina &8---");
            MessageUtil.sendRawMessage(sender, "&7/auth reload &8- &fPrzeladuj config");
            MessageUtil.sendRawMessage(sender, "&7/auth info <nick> &8- &fInformacje");
            MessageUtil.sendRawMessage(sender, "&7/auth unregister <nick> &8- &fUsun konto");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.getConfigManager().loadFiles();
            MessageUtil.sendMessage(sender, "reload-success");
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("info")) {
            User user = auth.getDb().getUserByName(args[1]);
            if (user == null) {
                MessageUtil.sendRawMessage(sender, "&cGracz nie istnieje w bazie.");
                return true;
            }
            MessageUtil.sendRawMessage(sender, "&8[&bInfo&8] &7UUID: &f" + user.getUuid());
            MessageUtil.sendRawMessage(sender, "&8[&bInfo&8] &7Premium: &f" + user.isPremium());
            MessageUtil.sendRawMessage(sender, "&8[&bInfo&8] &7Ostatnie IP: &f" + user.getIp());
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("unregister")) {
            User user = auth.getDb().getUserByName(args[1]);
            if (user == null) {
                MessageUtil.sendRawMessage(sender, "&cGracz nie istnieje w bazie.");
                return true;
            }
            auth.getDb().deleteUser(user.getUuid());
            MessageUtil.sendRawMessage(sender, "&aKonto gracza " + args[1] + " usuniete.");
            return true;
        }

        return true;
    }
}