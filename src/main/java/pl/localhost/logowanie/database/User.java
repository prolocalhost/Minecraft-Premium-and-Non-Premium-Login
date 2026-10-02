package pl.localhost.logowanie.database;

import java.util.UUID;

public class User {
    private UUID uuid;
    private String name;
    private String passwordHash;
    private boolean premium;
    private String ip;

    public User(UUID uuid, String name, String passwordHash, boolean premium, String ip) {
        this.uuid = uuid;
        this.name = name;
        this.passwordHash = passwordHash;
        this.premium = premium;
        this.ip = ip;
    }

    public UUID getUuid() { return uuid; }
    public String getName() { return name; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public boolean isPremium() { return premium; }
    public String getIp() { return ip; }
}