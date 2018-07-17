
package jdz.UEconomy.commands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import jdz.UEconomy.UEcoFormatter;
import jdz.UEconomy.data.UEcoBank;
import jdz.bukkitUtils.commands.SubCommand;
import jdz.bukkitUtils.commands.annotations.CommandLabel;
import jdz.bukkitUtils.commands.annotations.CommandMethod;

@CommandLabel("subtract")
@CommandLabel("sub")
public class UEcoSubCommand extends SubCommand {

	@CommandMethod
	public void subtract(CommandSender sender, OfflinePlayer target, double amount) {
		if (UEcoBank.has(target, amount)) {
			UEcoBank.subtract(target, amount);

			String amountText = ChatColor.YELLOW + "$" + UEcoFormatter.charFormat(amount, 5);
			String text = amountText + ChatColor.GREEN + "was deducted from %player% account";

			sender.sendMessage(text.replace("%player%", target.getName() + "'s"));
			if (target.isOnline())
				target.getPlayer().sendMessage(text.replace("%player%", "your"));
		}
		else {
			double balance = UEcoBank.get(target);
			sender.sendMessage(ChatColor.RED + target.getName() + " only has " + ChatColor.YELLOW + "$"
					+ UEcoFormatter.charFormat(balance, 5));
		}
	}
}
