
package jdz.UEconomy.vault;

import java.util.UUID;

import net.milkbowl.vault.economy.EconomyResponse;

public interface UUIDEconomy {
	public EconomyResponse withdrawPlayer(UUID uuid, double amount);
	public boolean hasAccount(UUID uuid);
	public boolean has(UUID uuid, double amount);
	public double getBalance(UUID uuid);
	public EconomyResponse depositPlayer(UUID uuid, double amount);
	public boolean createPlayerAccount(UUID uuid);
}
