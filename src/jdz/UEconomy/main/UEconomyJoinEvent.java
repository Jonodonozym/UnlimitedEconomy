package jdz.UEconomy.main;

import java.math.BigDecimal;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UEconomyJoinEvent implements Listener{
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		if (!UEconomyAPI.hasPlayer(e.getPlayer()))
			UEconomyAPI.setBalance(e.getPlayer(), 0);
	}
}
