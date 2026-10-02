package pl.localhost.logowanie.managers;

import pl.localhost.logowanie.Main;
import pl.localhost.logowanie.database.*;
import java.util.UUID;

public class DatabaseManager {
    private IDatabase activeDatabase;

    public DatabaseManager(Main plugin, ConfigManager config) {
        String type = config.getConfig().getString("database.type", "JSON").toUpperCase();

        switch (type) {
            case "MYSQL":
                activeDatabase = new MySQLDatabase(plugin, config);
                break;
            case "SQLITE":
                activeDatabase = new SQLiteDatabase(plugin);
                break;
            case "JSON":
            default:
                activeDatabase = new JsonDatabase(plugin);
                break;
        }
        activeDatabase.init();
    }

    public User getUser(UUID uuid) { return activeDatabase.getUser(uuid); }
    public User getUserByName(String name) { return activeDatabase.getUserByName(name); }
    public void saveUser(User user) { activeDatabase.saveUser(user); }
    public void deleteUser(UUID uuid) { activeDatabase.deleteUser(uuid); }
    public void close() { activeDatabase.close(); }
}