# localhost-logowanie

[![Java](https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
[![Platform](https://img.shields.io/badge/Platform-Paper%20%2F%20Spigot%201.20%2B-blue.svg)](https://papermc.io/)
[![Build](https://img.shields.io/badge/Build-Maven-C71A36.svg)](https://maven.apache.org/)
[![Security](https://img.shields.io/badge/Security-BCrypt-green.svg)](https://en.wikipedia.org/wiki/Bcrypt)

A lightweight, secure, and modern authentication and login management plugin for Minecraft servers (**Paper / Spigot 1.20+**).

---

## 🌟 Key Features

- **🔐 Robust Password Security**: Uses industry-standard **BCrypt** (cost factor 12) password hashing to guarantee account security.
- **⚡ Mojang Premium Auto-Login**:
  - Asynchronously queries the Mojang API to detect authentic premium Minecraft accounts.
  - Automatically registers and logs in premium users.
  - Generates a secure fallback/backup password in case of an IP address change.
  - Built-in in-memory lookup cache to minimize redundant HTTP requests.
- **💾 Multi-Database Backend**:
  - **JSON**: Fast flat-file storage for small servers.
  - **SQLite**: Zero-configuration relational database stored locally.
  - **MySQL**: Enterprise-grade database storage using **HikariCP** high-performance connection pooling.
- **🛡️ Full Pre-Login Protection**:
  - Restricts player movement (X/Z coordinates locked).
  - Blocks chat messages and unauthorized commands until authenticated.
  - Prevents breaking, placing, interacting with blocks, inventory clicking, and dropping items.
  - Protects players from taking entity/environment damage while not logged in.
- **⏱️ Anti-Brute-Force & Session Protection**:
  - Configurable login timeout (kicks idle players after *X* seconds).
  - Configurable maximum login attempts with automatic kicking upon exceeding the limit.
- **🎨 100% Configurable Messages**:
  - Full support for Minecraft color codes (`&`).
  - Customizable prefixes and translated status notifications via `messages.yml`.

---

## 📋 Commands & Permissions

### Player Commands

| Command | Aliases | Description | Permission |
| :--- | :--- | :--- | :--- |
| `/register <password> <password>` | `/reg` | Register a new account with a confirmed password | Everyone |
| `/login <password>` | `/l` | Log in to an existing registered account | Everyone |
| `/changepassword <old> <new>` | `/zmienhaslo` | Change your current password | Everyone |
| `/unregister` | — | Remove your own registration and get disconnected | Everyone |

### Admin Commands

| Command | Description | Permission |
| :--- | :--- | :--- |
| `/auth` | Displays the admin help menu | `auth.admin` |
| `/auth reload` | Reloads `config.yml` and `messages.yml` | `auth.admin` |
| `/auth info <player>` | Displays account UUID, Premium flag, and last recorded IP | `auth.admin` |
| `/auth unregister <player>` | Deletes a player's account from the database | `auth.admin` |

---

## ⚙️ Configuration

### `config.yml`
```yaml
database:
  # Available types: JSON, SQLITE, MYSQL
  type: JSON
  mysql:
    host: "127.0.0.1"
    port: 3306
    database: "serwer"
    user: "root"
    password: ""

security:
  # Time in seconds before an unlogged player is kicked
  timeout-seconds: 60
  # Maximum failed password attempts before kicking
  max-login-attempts: 3
  kick-on-max-attempts: true
```

### `messages.yml`
All messages sent to players can be fully customized with standard Minecraft color codes (`&`):
```yaml
prefix: "&8[&bLogowanie&8] &7"
login-request: "Wpisz &b/login <haslo> &7aby grac!"
register-request: "Wpisz &b/register <haslo> <haslo> &7aby grac!"
login-success: "&aZalogowano pomyslnie!"
register-success: "&aZarejestrowano pomyslnie! Zostales automatycznie zalogowany."
premium-auto-login: "&aWykryto sesje Premium! Zalogowano automatycznie."
wrong-password: "&cNieprawidlowe haslo! Sprobuj ponownie."
passwords-not-match: "&cHasla nie sa identyczne!"
already-logged-in: "&cJestes juz zalogowany!"
already-registered: "&cTo konto jest juz zarejestrowane!"
not-registered: "&cTo konto nie jest zarejestrowane. Uzyj /register."
timeout-kick: "&cPrzekroczono czas na logowanie (60 sekund)."
max-attempts-kick: "&cZbyt wiele nieudanych prob logowania."
no-permission: "&cBrak uprawnien."
reload-success: "&aPrzeladowano konfiguracje."
```

---

## 🚀 Installation

1. Make sure your server runs **Java 17+** and **Paper / Spigot 1.20+**.
2. Download the latest release `.jar` file or compile it from source.
3. Place the `.jar` file into your server's `plugins/` directory.
4. Restart your server to generate configuration files in `plugins/localhost-logowanie/`.
5. Adjust `config.yml` (e.g. configure MySQL if desired) and `messages.yml`.
6. Run `/auth reload` or restart the server to apply changes.

---

## 🛠️ Building from Source

To build this project from source, you need **JDK 17+** and **Apache Maven**:

```bash
# Clone the repository
git clone https://github.com/your-username/localhost-logowanie.git

# Navigate to the project directory
cd localhost-logowanie

# Build with Maven
mvn clean package
```

The shaded output JAR will be generated inside the `target/` directory:
```
target/localhost-logowanie-1.0-SNAPSHOT.jar
```

---

## 👤 Author & Support

- **Author**: Localhost
- **Discord**: `localhost_127001`
