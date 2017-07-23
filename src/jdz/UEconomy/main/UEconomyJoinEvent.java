package jdz.UEconomy.main;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UEconomyJoinEvent implements Listener{
	
	@EventHandler(priority=EventPriority.HIGH)
	public void onPlayerJoin(PlayerJoinEvent e){
		if (!UEconomyAPI.hasPlayer(e.getPlayer()))
			UEconomyAPI.setBalance(e.getPlayer(), 0);
	}
}
