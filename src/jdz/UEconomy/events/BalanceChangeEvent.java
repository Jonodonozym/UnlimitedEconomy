
package jdz.UEconomy.events;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;

import jdz.UEconomy.data.UUIDToPlayer;
import jdz.bukkitUtils.events.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class BalanceChangeEvent extends Event {
	@Getter private final OfflinePlayer player;
	@Getter private final UUID uuid;
	@Getter private final double oldBalance, newBalance;

	public BalanceChangeEvent(UUID uuid, double oldBalance, double newBalance) {
		this(UUIDToPlayer.get(uuid), uuid, oldBalance, newBalance);
	}

	public static HandlerList getHandlerList() {
		return getHandlers(BalanceChangeEvent.class);
	}
}
