package jdz.UEconomy.main;

import java.math.BigDecimal;
import java.util.List;

import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;

public class UEconomyVaultHook extends AbstractEconomy{
	public static BigDecimal limit = new BigDecimal(10000000000000.0);
	
	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public double getBalance(String arg0) {
		return UEconomyAPI.getBalance(arg0);
	}

	@Override
	public double getBalance(String arg0, String arg1) {
		return getBalance(arg0);
	}

	@Override
	public EconomyResponse depositPlayer(String arg0, double arg1) {
		UEconomyAPI.addBalance(arg0, arg1);
		return new EconomyResponse(arg1, UEconomyAPI.getBalance(arg0), EconomyResponse.ResponseType.SUCCESS, null);
	}

	@Override
	public EconomyResponse depositPlayer(String arg0, String arg1, double arg2) {
		return depositPlayer(arg0, arg2);
	}

	@Override
	public boolean has(String arg0, double arg1) {
		return UEconomyAPI.hasEnough(arg0, arg1);
	}

	@Override
	public boolean has(String arg0, String arg1, double arg2) {
		return has(arg0, arg2);
	}

	@Override
	public EconomyResponse withdrawPlayer(String arg0, double arg1) {
		if (UEconomyAPI.hasEnough(arg0, arg1)){
			UEconomyAPI.subBalance(arg0, arg1);
			return new EconomyResponse(arg1, UEconomyAPI.getBalance(arg0), EconomyResponse.ResponseType.SUCCESS, null);
		}
		return new EconomyResponse(0, UEconomyAPI.getBalance(arg0), EconomyResponse.ResponseType.FAILURE, arg0+" has insufficient funds");
	}
	
	@Override
	public EconomyResponse withdrawPlayer(String arg0, String arg1, double arg2) {
		return withdrawPlayer(arg0, arg2);
	}

	@Override
	public boolean createPlayerAccount(String arg0) {
		if (UEconomyAPI.hasPlayer(arg0))
			return false;
		UEconomyAPI.economy.put(arg0, 0.0);
		return true;
	}

	@Override
	public boolean createPlayerAccount(String arg0, String arg1) {
		return createPlayerAccount(arg0);
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
	public String format(double arg0) {
		return UEconomyAPI.charFormat(arg0, 4);
	}

	@Override
	public int fractionalDigits() {
		return -1;
	}

	@Override
	public String getName() {
		return "ueco";
	}

	@Override
	public boolean hasAccount(String arg0) {
		return UEconomyAPI.hasPlayer(arg0);
	}

	@Override
	public boolean hasAccount(String arg0, String arg1) {
		return hasAccount(arg0, arg1);
	}
	

	
	
	
	


	@Override
	public boolean hasBankSupport() {
		return false;
	}

	@Override
	public List<String> getBanks() {
		return null;
	}

	@Override
	public EconomyResponse bankBalance(String arg0) {
		return new EconomyResponse(0, UEconomyAPI.getBalance(arg0), EconomyResponse.ResponseType.SUCCESS, null);
	}

	@Override
	public EconomyResponse deleteBank(String arg0) {
		return new EconomyResponse(0,0,EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Account names not supported");
	}

	@Override
	public EconomyResponse bankDeposit(String arg0, double arg1) {
		return new EconomyResponse(0,0,EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Account names not supported");
	}

	@Override
	public EconomyResponse bankHas(String arg0, double arg1) {
		return new EconomyResponse(0,0,EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Account names not supported");
	}

	@Override
	public EconomyResponse isBankMember(String arg0, String arg1) {
		return new EconomyResponse(0,0,EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Account names not supported");
	}

	@Override
	public EconomyResponse isBankOwner(String arg0, String arg1) {
		return new EconomyResponse(0,0,EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Account names not supported");
	}

	@Override
	public EconomyResponse bankWithdraw(String arg0, double arg1) {
		return new EconomyResponse(0,0,EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Account names not supported");
	}

	@Override
	public EconomyResponse createBank(String arg0, String arg1) {
		return new EconomyResponse(0,0,EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Account names not supported");
	}

}
