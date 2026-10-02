package pl.localhost.logowanie.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.file.FileConfiguration;
import pl.localhost.logowanie.Main;
import pl.localhost.logowanie.managers.ConfigManager;

import java.sql.*;
import java.util.UUID;

public class MySQLDatabase implements IDatabase {
    private HikariDataSource dataSource;
    private final Main plugin;
    private final ConfigManager configManager;

    public MySQLDatabase(Main plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public void init() {
        FileConfiguration config = configManager.getConfig();

        String host = config.getString("database.mysql.host", "127.0.0.1");
        int port = config.getInt("database.mysql.port", 3306);
        String database = config.getString("database.mysql.database", "serwer");
        String user = config.getString("database.mysql.user", "root");
        String password = config.getString("database.mysql.password", "");

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        hikariConfig.setUsername(user);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setMaxLifetime(1800000);
        hikariConfig.setKeepaliveTime(0);
        hikariConfig.setConnectionTimeout(5000);
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("useServerPrepStmts", "true");

        try {
            this.dataSource = new HikariDataSource(hikariConfig);
            createTable();
            plugin.getLogger().info("polaczono elegancko wszystko smiga ELO! MySQL.");
        } catch (Exception e) {
            plugin.getLogger().severe("nie udalo sie polaczyc z mysql sprawdz config.ym");
            e.printStackTrace();
        }
    }

    private void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS users (" +
                "uuid VARCHAR(36) NOT NULL PRIMARY KEY, " +
                "name VARCHAR(16) NOT NULL, " +
                "password VARCHAR(100) NOT NULL, " +
                "premium BOOLEAN NOT NULL, " +
                "ip VARCHAR(45) NOT NULL" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;";

        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            plugin.getLogger().severe("blad z tabela MySQL!");
            e.printStackTrace();
        }
    }

    @Override
    public User getUser(UUID uuid) {
        String query = "SELECT * FROM users WHERE uuid = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            uuid,
                            rs.getString("name"),
                            rs.getString("password"),
                            rs.getBoolean("premium"),
                            rs.getString("ip")
                    );
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("bledzik pobierania (UUID) z MySQL!");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User getUserByName(String name) {
        String query = "SELECT * FROM users WHERE name = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            UUID.fromString(rs.getString("uuid")),
                            rs.getString("name"),
                            rs.getString("password"),
                            rs.getBoolean("premium"),
                            rs.getString("ip")
                    );
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("blad pobierania nazwy gracza (name z mysql");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveUser(User user) {
        String query = "INSERT INTO users (uuid, name, password, premium, ip) VALUES (?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE name = ?, password = ?, premium = ?, ip = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, user.getUuid().toString());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPasswordHash());
            ps.setBoolean(4, user.isPremium());
            ps.setString(5, user.getIp());
            ps.setString(6, user.getName());
            ps.setString(7, user.getPasswordHash());
            ps.setBoolean(8, user.isPremium());
            ps.setString(9, user.getIp());

            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("jakis blad z zapisywaniem gracza MySQL!");
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(UUID uuid) {
        String query = "DELETE FROM users WHERE uuid = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("blad wypierdalania jakiegos cwela z bazy danych MySQL!");
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            plugin.getLogger().info("polaczenie sie wyjebalo z baza MySQL.");
        }
    }
}