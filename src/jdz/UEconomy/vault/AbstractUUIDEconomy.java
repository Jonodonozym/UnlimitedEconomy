package jdz.UEconomy.vault;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import net.milkbowl.vault.economy.EconomyResponse;

@SuppressWarnings("deprecation")
abstract class AbstractUUIDEconomy extends NonBankEconomy implements UUIDEconomy {

	@Override
	public boolean createPlayerAccount(String name) {
		return createPlayerAccount(Bukkit.getOfflinePlayer(name));
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer player) {
		return createPlayerAccount(player.getUniqueId());
	}

	@Override
	public boolean createPlayerAccount(String name, String world) {
		return createPlayerAccount(name);
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer player, String world) {
		return createPlayerAccount(player);
	}

	@Override
	public EconomyResponse depositPlayer(String name, double amount) {
		return depositPlayer(Bukkit.getPlayer(name).getUniqueId(), amount);
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
		return depositPlayer(player.getUniqueId(), amount);
	}

	@Override
	public EconomyResponse depositPlayer(String name, String world, double amount) {
		return depositPlayer(name, amount);
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer player, String world, double amount) {
		return depositPlayer(player, amount);
	}

	@Override
	public double getBalance(String name) {
		return getBalance(Bukkit.getOfflinePlayer(name));
	}

	@Override
	public double getBalance(OfflinePlayer player) {
		return getBalance(player.getUniqueId());
	}

	@Override
	public double getBalance(String name, String world) {
		return getBalance(name);
	}

	@Override
	public double getBalance(OfflinePlayer player, String world) {
		return getBalance(player);
	}

	@Override
	public boolean has(String name, double amount) {
		return has(Bukkit.getOfflinePlayer(name), amount);
	}

	@Override
	public boolean has(OfflinePlayer player, double amount) {
		return has(player.getUniqueId(), amount);
	}

	@Override
	public boolean has(String name, String world, double amount) {
		return has(name, amount);
	}

	@Override
	public boolean has(OfflinePlayer player, String world, double amount) {
		return has(player, amount);
	}

	@Override
	public boolean hasAccount(String name) {
		return hasAccount(Bukkit.getOfflinePlayer(name));
	}

	@Override
	public boolean hasAccount(OfflinePlayer player) {
		return hasAccount(player.getUniqueId());
	}

	@Override
	public boolean hasAccount(String name, String world) {
		return hasAccount(name);
	}

	@Override
	public boolean hasAccount(OfflinePlayer player, String world) {
		return hasAccount(player);
	}

	@Override
	public EconomyResponse withdrawPlayer(String name, double amount) {
		return withdrawPlayer(Bukkit.getOfflinePlayer(name), amount);
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
		return withdrawPlayer(player.getUniqueId(), amount);
	}

	@Override
	public EconomyResponse withdrawPlayer(String name, String world, double amount) {
		return withdrawPlayer(name, amount);
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer player, String world, double amount) {
		return withdrawPlayer(player, amount);
	}
}
