package pl.localhost.logowanie;

import org.bukkit.plugin.java.JavaPlugin;
import pl.localhost.logowanie.commands.*;
import pl.localhost.logowanie.listeners.*;
import pl.localhost.logowanie.managers.*;

public class Main extends JavaPlugin {

    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private SessionManager sessionManager;
    private AuthManager authManager;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        this.sessionManager = new SessionManager(this);
        this.databaseManager = new DatabaseManager(this, configManager);
        this.authManager = new AuthManager(databaseManager, sessionManager, configManager);

        getServer().getPluginManager().registerEvents(new PlayerListener(this, authManager, sessionManager), this);
        getServer().getPluginManager().registerEvents(new PremiumLoginListener(authManager), this);
        getServer().getPluginManager().registerEvents(new ProtectionListener(sessionManager), this);

        getCommand("login").setExecutor(new LoginCommand(authManager, sessionManager));
        getCommand("register").setExecutor(new RegisterCommand(authManager, sessionManager));
        getCommand("changepassword").setExecutor(new ChangePasswordCommand(authManager, sessionManager));
        getCommand("unregister").setExecutor(new AuthCommand(this, authManager));
        getCommand("auth").setExecutor(new AuthCommand(this, authManager));

        getLogger().info("plugin on loclahost logowanie");
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.close();
        }
        getLogger().info("pluiginnn off");
    }

    public ConfigManager getConfigManager() { return configManager; }
}