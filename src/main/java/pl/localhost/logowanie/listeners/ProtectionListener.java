package pl.localhost.logowanie.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import pl.localhost.logowanie.managers.SessionManager;

public class ProtectionListener implements Listener {
    private final SessionManager sessionManager;

    public ProtectionListener(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMove(PlayerMoveEvent e) {
        if (!sessionManager.isLoggedIn(e.getPlayer())) {
            if (e.getFrom().getX() != e.getTo().getX() || e.getFrom().getZ() != e.getTo().getZ()) {
                e.setTo(e.getFrom());
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(AsyncPlayerChatEvent e) {
        if (!sessionManager.isLoggedIn(e.getPlayer())) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommand(PlayerCommandPreprocessEvent e) {
        if (!sessionManager.isLoggedIn(e.getPlayer())) {
            String cmd = e.getMessage().split(" ")[0].toLowerCase();
            if (!cmd.equals("/login") && !cmd.equals("/l") && !cmd.equals("/register") && !cmd.equals("/reg")) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler public void onBreak(BlockBreakEvent e) { if (!sessionManager.isLoggedIn(e.getPlayer())) e.setCancelled(true); }
    @EventHandler public void onPlace(BlockPlaceEvent e) { if (!sessionManager.isLoggedIn(e.getPlayer())) e.setCancelled(true); }
    @EventHandler public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof org.bukkit.entity.Player) {
            if (!sessionManager.isLoggedIn((org.bukkit.entity.Player) e.getEntity())) e.setCancelled(true);
        }
    }
    @EventHandler public void onInteract(PlayerInteractEvent e) { if (!sessionManager.isLoggedIn(e.getPlayer())) e.setCancelled(true); }
    @EventHandler public void onDrop(PlayerDropItemEvent e) { if (!sessionManager.isLoggedIn(e.getPlayer())) e.setCancelled(true); }
    @EventHandler public void onInventory(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof org.bukkit.entity.Player && !sessionManager.isLoggedIn((org.bukkit.entity.Player) e.getWhoClicked())) e.setCancelled(true);
    }
}