
package jdz.UEconomy.events;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.HandlerList;

import jdz.bukkitUtils.events.Event;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BalanceChangeEvent extends Event {
	private final UUID uuid;
	private final double oldBalance, newBalance;
	
	public boolean isFor(OfflinePlayer player) {
		return player.getUniqueId().equals(uuid);
	}
	
	public static HandlerList getHandlerList() {
		return getHandlers(BalanceChangeEvent.class);
	}
}
