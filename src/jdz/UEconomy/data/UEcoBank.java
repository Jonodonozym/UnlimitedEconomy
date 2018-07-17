
package jdz.UEconomy.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import jdz.UEconomy.UEco;
import jdz.bukkitUtils.events.Listener;

public class UEcoBank implements Listener {
	private static final Map<UUID, UEcoEntry> economy = new HashMap<UUID, UEcoEntry>();

	public static boolean has(OfflinePlayer player, double amount) {
		return has(player.getUniqueId(), amount);
	}

	public static double get(OfflinePlayer player) {
		return get(player.getUniqueId());
	}

	public static void set(OfflinePlayer player, double amount) {
		set(player.getUniqueId(), amount);
	}

	public static void add(OfflinePlayer player, double amount) {
		add(player.getUniqueId(), amount);
	}

	public static void subtract(OfflinePlayer player, double amount) {
		subtract(player.getUniqueId(), amount);
	}

	public static boolean has(UUID uuid, double amount) {
		return get(uuid) >= amount;
	}

	public static double get(UUID uuid) {
		if (!economy.containsKey(uuid))
			return UEcoDatabase.getInstance().get(uuid).getBalance();
		else
			return economy.get(uuid).getBalance();
	}

	public static void set(UUID uuid, double amount) {
		if (!economy.containsKey(uuid))
			UEcoDatabase.getInstance().set(uuid, amount);
		else
			economy.get(uuid).setBalance(amount);
	}

	public static void add(UUID uuid, double amount) {
		if (!economy.containsKey(uuid))
			UEcoDatabase.getInstance().add(uuid, amount);
		else
			economy.get(uuid).setBalance(economy.get(uuid).getBalance() + amount);
	}

	public static void subtract(UUID uuid, double amount) {
		add(uuid, -amount);
	}

	public static Map<UUID, UEcoEntry> getEconomy() {
		return Collections.unmodifiableMap(economy);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Bukkit.getScheduler().runTaskAsynchronously(UEco.getInstance(), () -> {
			UUID uuid = event.getPlayer().getUniqueId();
			economy.put(uuid, UEcoDatabase.getInstance().get(uuid));
		});
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Bukkit.getScheduler().runTaskAsynchronously(UEco.getInstance(), () -> {
			UUID uuid = event.getPlayer().getUniqueId();
			UEcoDatabase.getInstance().set(uuid, economy.remove(uuid).getBalance());
		});
	}
}
