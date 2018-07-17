package jdz.UEconomy;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import jdz.UEconomy.commands.BalanceCommand;
import jdz.UEconomy.commands.BaltopCommand;
import jdz.UEconomy.commands.PayCommand;
import jdz.UEconomy.commands.UEcoCommandExecutor;
import jdz.UEconomy.data.UEcoBank;
import jdz.UEconomy.data.UEcoTop;
import jdz.UEconomy.placeholder.UEcoPlaceholderHook;
import jdz.UEconomy.vault.UEcoVaultHook;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;

public class UEco extends JavaPlugin {
	@Getter private static UEco instance;

	@Override
	public void onEnable() {
		instance = this;

		UEcoCommandExecutor.getInstance().register();
		new BalanceCommand().register(this);
		new BaltopCommand().register(this);
		new PayCommand().register(this);
		
		new UEcoBank().registerEvents(this);
		
		UEcoTop.update();

		if (getServer().getPluginManager().getPlugin("Vault") != null)
			Bukkit.getServer().getServicesManager().register(Economy.class, UEcoVaultHook.getInstance(), this,
					ServicePriority.High);
		
		if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null)
			new UEcoPlaceholderHook().hook();
		
		for (Player player: Bukkit.getOnlinePlayers())
			new UEcoBank().onJoin(new PlayerJoinEvent(player, ""));
	}
}