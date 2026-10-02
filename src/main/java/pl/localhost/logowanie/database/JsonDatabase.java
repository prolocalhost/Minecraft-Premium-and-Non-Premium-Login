package pl.localhost.logowanie.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import pl.localhost.logowanie.Main;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JsonDatabase implements IDatabase {
    private final File file;
    private final Gson gson;
    private Map<UUID, User> users;

    public JsonDatabase(Main plugin) {
        File dir = new File(plugin.getDataFolder(), "database");
        if (!dir.exists()) dir.mkdirs();
        this.file = new File(dir, "users.json");
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.users = new HashMap<>();
    }

    @Override
    public void init() {
        if (!file.exists()) {
            saveToFile();
            return;
        }
        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<UUID, User>>() {}.getType();
            users = gson.fromJson(reader, type);
            if (users == null) users = new HashMap<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getUser(UUID uuid) { return users.get(uuid); }

    @Override
    public User getUserByName(String name) {
        return users.values().stream().filter(u -> u.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    @Override
    public void saveUser(User user) {
        users.put(user.getUuid(), user);
        saveToFile();
    }

    @Override
    public void deleteUser(UUID uuid) {
        users.remove(uuid);
        saveToFile();
    }

    @Override
    public void close() { saveToFile(); }

    private void saveToFile() {
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}