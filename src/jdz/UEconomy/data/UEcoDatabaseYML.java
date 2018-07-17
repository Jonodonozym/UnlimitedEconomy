
package jdz.UEconomy.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.PluginDisableEvent;

import jdz.UEconomy.UEco;
import jdz.bukkitUtils.events.Listener;
import jdz.bukkitUtils.misc.Config;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
class UEcoDatabaseYML implements UEcoDatabase, Listener {
	@Getter private static final UEcoDatabaseYML instance = new UEcoDatabaseYML();

	private final File configFile = Config.getConfigFile(UEco.getInstance(), "data");
	private FileConfiguration config;

	private UEcoDatabaseYML() {
		registerEvents(UEco.getInstance());

		if (!configFile.exists())
			config = new YamlConfiguration();
		else
			config = YamlConfiguration.loadConfiguration(configFile);

		// save every 10 minutes
		Bukkit.getScheduler().runTaskTimerAsynchronously(UEco.getInstance(), () -> {
			save();
		}, 12000, 12000);
	}

	@Override
	public UEcoEntry get(UUID uuid) {
		String name = config.getString(uuid + ".name", Bukkit.getOfflinePlayer(uuid).getName());
		double amount = config.getDouble(uuid + ".bal", 0);
		return new UEcoEntry(uuid, name, amount);
	}

	@SuppressWarnings("deprecation")
	@Override
	public UEcoEntry get(String name) {
		UUID uuid = UUID.fromString(config.getString(name + ".uuid", "" + Bukkit.getOfflinePlayer(name).getUniqueId()));
		double amount = config.getDouble(name + ".bal", 0);
		return new UEcoEntry(uuid, name, amount);
	}

	@Override
	public List<UEcoEntry> getAll() {
		List<UEcoEntry> entries = new ArrayList<UEcoEntry>();
		for (String key : config.getKeys(false))
			if (config.contains(key + ".name"))
				entries.add(get(UUID.fromString(key)));
		return entries;
	}

	@Override
	public void set(UUID uuid, double amount) {
		config.set(uuid + ".bal", amount);
		config.set(config.getString(uuid+".name"), amount);
	}

	@Override
	public void add(UUID uuid, double amount) {
		double newAmount = get(uuid).getBalance()+amount;
		
		config.set(uuid + ".bal", newAmount);
		config.set(config.getString(uuid+".name"), newAmount);
	}

	@EventHandler
	public void onPluginDisable(PluginDisableEvent event) {
		if (!event.getPlugin().equals(UEco.getInstance()))
			return;

		save();
	}

	private void save() {
		try {
			config.save(configFile);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

}
