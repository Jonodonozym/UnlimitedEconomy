
package jdz.UEconomy.placeholder;

import org.bukkit.entity.Player;

import jdz.UEconomy.UEco;
import jdz.UEconomy.UEcoFormatter;
import jdz.UEconomy.data.UEcoBank;
import me.clip.placeholderapi.external.EZPlaceholderHook;

public class UEcoPlaceholderHook extends EZPlaceholderHook {

	public UEcoPlaceholderHook() {
		super(UEco.getInstance(), "UEco");
	}

	@Override
	public String onPlaceholderRequest(Player player, String identifier) {
		if (identifier.equalsIgnoreCase("balance"))
			return "$"+UEcoFormatter.charFormat(UEcoBank.get(player));
		return null;
	}
}
