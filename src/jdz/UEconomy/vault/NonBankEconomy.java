
package jdz.UEconomy.vault;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.OfflinePlayer;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

abstract class NonBankEconomy implements Economy{

	@Override
	public EconomyResponse bankBalance(String arg0) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Unsupported operation");
	}

	@Override
	public EconomyResponse bankDeposit(String arg0, double arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Unsupported operation");
	}

	@Override
	public EconomyResponse bankHas(String arg0, double arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Unsupported operation");
	}

	@Override
	public EconomyResponse bankWithdraw(String arg0, double arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Unsupported operation");
	}

	@Override
	public EconomyResponse createBank(String arg0, String arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Unsupported operation");
	}

	@Override
	public EconomyResponse createBank(String arg0, OfflinePlayer arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Unsupported operation");
	}

	@Override
	public EconomyResponse deleteBank(String arg0) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Unsupported operation");
	}

	@Override
	public List<String> getBanks() {
		return new ArrayList<String>();
	}

	@Override
	public boolean hasBankSupport() {
		return false;
	}

	@Override
	public EconomyResponse isBankMember(String arg0, String arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Unsupported operation");
	}

	@Override
	public EconomyResponse isBankMember(String arg0, OfflinePlayer arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Unsupported operation");
	}

	@Override
	public EconomyResponse isBankOwner(String arg0, String arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Unsupported operation");
	}

	@Override
	public EconomyResponse isBankOwner(String arg0, OfflinePlayer arg1) {
		return new EconomyResponse(0, 0, ResponseType.NOT_IMPLEMENTED, "Unsupported operation");
	}

}
