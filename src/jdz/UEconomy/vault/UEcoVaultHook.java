
package jdz.UEconomy.vault;

import java.util.UUID;

import org.bukkit.ChatColor;

import jdz.UEconomy.UEco;
import jdz.UEconomy.UEcoFormatter;
import jdz.UEconomy.data.UEcoBank;
import lombok.Getter;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

public class UEcoVaultHook extends AbstractUUIDEconomy {
	@Getter private static UUIDEconomy uuidEconomy = getInstance();
	@Getter private static UEcoVaultHook instance = new UEcoVaultHook();

	private UEcoVaultHook() {};

	@Override
	public EconomyResponse withdrawPlayer(UUID uuid, double amount) {
		if (UEcoBank.has(uuid, amount)) {
			UEcoBank.subtract(uuid, amount);
			return new EconomyResponse(amount, UEcoBank.get(uuid), ResponseType.SUCCESS, "");
		}
		return new EconomyResponse(amount, UEcoBank.get(uuid), ResponseType.FAILURE,
				ChatColor.RED + "Insufficient Funds");
	}

	@Override
	public boolean hasAccount(UUID uuid) {
		return true;
	}

	@Override
	public boolean has(UUID uuid, double amount) {
		return UEcoBank.has(uuid, amount);
	}

	@Override
	public double getBalance(UUID uuid) {
		return UEcoBank.get(uuid);
	}

	@Override
	public EconomyResponse depositPlayer(UUID uuid, double amount) {
		UEcoBank.add(uuid, amount);
		return new EconomyResponse(amount, UEcoBank.get(uuid), ResponseType.SUCCESS, "");
	}

	@Override
	public boolean createPlayerAccount(UUID uuid) {
		return true;
	}

	@Override
	public String currencyNamePlural() {
		return "Dollars";
	}

	@Override
	public String currencyNameSingular() {
		return "Dollar";
	}

	@Override
	public String format(double amount) {
		return UEcoFormatter.charFormat(amount, 6);
	}

	@Override
	public int fractionalDigits() {
		return 2;
	}

	@Override
	public String getName() {
		return "UEconomy";
	}

	@Override
	public boolean isEnabled() {
		return UEco.getInstance().isEnabled();
	}

}
