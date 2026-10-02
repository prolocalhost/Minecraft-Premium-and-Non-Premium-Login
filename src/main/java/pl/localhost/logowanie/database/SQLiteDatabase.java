package pl.localhost.logowanie.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import pl.localhost.logowanie.Main;
import java.io.File;
import java.sql.*;
import java.util.UUID;

public class SQLiteDatabase implements IDatabase {
    private HikariDataSource dataSource;
    private final Main plugin;

    public SQLiteDatabase(Main plugin) { this.plugin = plugin; }

    @Override
    public void init() {
        File dbFile = new File(plugin.getDataFolder() + "/database", "auth.db");
        dbFile.getParentFile().mkdirs();
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlite:" + dbFile.getAbsolutePath());
        config.setDriverClassName("org.sqlite.JDBC");
        dataSource = new HikariDataSource(config);
        createTable();
    }

    private void createTable() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "uuid VARCHAR(36) PRIMARY KEY, " +
                    "name VARCHAR(16), " +
                    "password VARCHAR(100), " +
                    "premium BOOLEAN, " +
                    "ip VARCHAR(45))");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public User getUser(UUID uuid) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE uuid=?")) {
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new User(uuid, rs.getString("name"), rs.getString("password"), rs.getBoolean("premium"), rs.getString("ip"));
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public User getUserByName(String name) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE name=?")) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new User(UUID.fromString(rs.getString("uuid")), rs.getString("name"), rs.getString("password"), rs.getBoolean("premium"), rs.getString("ip"));
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    @Override
    public void saveUser(User user) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("REPLACE INTO users (uuid, name, password, premium, ip) VALUES (?, ?, ?, ?, ?)")) {
            ps.setString(1, user.getUuid().toString());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPasswordHash());
            ps.setBoolean(4, user.isPremium());
            ps.setString(5, user.getIp());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void deleteUser(UUID uuid) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE uuid=?")) {
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void close() { if (dataSource != null) dataSource.close(); }
}