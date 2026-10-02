package pl.localhost.logowanie.database;
import java.util.UUID;

public interface IDatabase {
    void init();
    User getUser(UUID uuid);
    User getUserByName(String name);
    void saveUser(User user);
    void deleteUser(UUID uuid);
    void close();
}