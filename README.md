<div align="center">
  <h1>🛡️ Localhost-login - Advanced Authentication</h1>
  <p>A comprehensive authentication system for Minecraft servers (Premium & Cracked) featuring Auto-Login.</p>

  <img src="https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java" />
  <img src="https://img.shields.io/badge/API-Spigot%20%7C%20Paper-009688?style=for-the-badge&logo=minecraft&logoColor=white" alt="API" />
  <img src="https://img.shields.io/badge/Database-MySQL%20%7C%20SQLite-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="Database" />
</div>

## 📌 About the Project

**[Plugin Name]** is a modern and secure authentication system designed for offline-mode (Cracked/Non-Premium) servers. The plugin automatically detects players with a legitimate Minecraft license (Premium) and logs them in without requiring a password, while Non-Premium accounts are protected by a strongly encrypted password.

## ✨ Key Features

- 🚀 **Premium Auto-Login:** Automatic session verification with Mojang servers (or via Velocity/BungeeCord forwarding).
- 🔒 **High Security:** Passwords are securely hashed using the **BCrypt** algorithm (protection against data breaches).
- 🛑 **Strict Action Blocking:** Completely blocks movement, interactions, damage, item dropping, and chat until the player is authenticated.
- 💾 **Database Support:** Save data locally using SQLite or asynchronously via MySQL/MariaDB for server networks.
- 🎨 **Fully Configurable:** Customize all messages, prefixes, and timeouts.
- ⚡ **Anti-Bot Protection:** Limits account registrations per IP address and includes delays between login attempts.

## 🛠️ Commands & Permissions

| Command | Description | Permission |
| :--- | :--- | :--- |
| `/register <password> <repeat>` | Register a new account. | None (Default) |
| `/login <password>` | Log in to the server. | None (Default) |
| `/changepassword <old> <new>`| Change your current password. | None (Default) |
| `/authadmin unregister <player>` | Remove a player's account from the database. | `auth.admin` |
| `/authadmin reload` | Reload the plugin configuration. | `auth.admin` |

## 🚀 Installation

1. Download the latest plugin version from the [Releases](../../releases) tab.
2. Drop the downloaded `.jar` file into your server's `/plugins` folder.
3. Restart your server or load the plugin (e.g., via PlugMan).
4. Navigate to the `/plugins/[Plugin Name]/` directory and configure the `config.yml` file (especially database settings if using MySQL).
5. Done! (It is recommended to enable `bungeecord: true` in `spigot.yml` if you are using a proxy).

## ⚙️ Configuration Example (config.yml)

```yaml
database:
  type: SQLITE # Available: SQLITE, MYSQL
  host: "127.0.0.1"
  port: 3306
  database: "auth_db"
  user: "root"
  password: "password"

settings:
  timeout-seconds: 60 # Time to login before being kicked
  max-accounts-per-ip: 3
  premium-autologin: true

messages:
  prefix: "&8[&bAuth&8] &7"
  please-login: "Please login using: &b/login <password>"
  please-register: "Please register using: &b/register <password> <password>"
  success-login: "&aSuccessfully logged in!"
  premium-auto-login: "&aPremium account detected! Auto-login successful."
