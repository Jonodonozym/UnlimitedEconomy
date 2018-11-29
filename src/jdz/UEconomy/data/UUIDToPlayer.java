
package jdz.UEconomy.data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

import jdz.bukkitUtils.events.Listener;

public class UUIDToPlayer implements Listener {
	private static final Map<UUID, OfflinePlayer> playerMap = new HashMap<UUID, OfflinePlayer>();
	
	public static OfflinePlayer get(UUID uuid) {
		if (!playerMap.containsKey(uuid))
			playerMap.put(uuid, Bukkit.getOfflinePlayer(uuid));
		return playerMap.get(uuid);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		playerMap.put(event.getPlayer().getUniqueId(), event.getPlayer());
	}
}
