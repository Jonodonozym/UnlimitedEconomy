
package jdz.UEconomy.commands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import jdz.UEconomy.UEcoFormatter;
import jdz.UEconomy.data.UEcoBank;
import jdz.bukkitUtils.commands.SubCommand;
import jdz.bukkitUtils.commands.annotations.CommandLabel;
import jdz.bukkitUtils.commands.annotations.CommandMethod;

@CommandLabel("add")
public class UEcoAddCommand extends SubCommand {

	@CommandMethod
	public void add(CommandSender sender, OfflinePlayer target, double amount) {
		UEcoBank.add(target, amount);
		
		String amountText = ChatColor.YELLOW + "$" + UEcoFormatter.charFormat(amount, 5);
		String text = amountText + ChatColor.GREEN + "was added to %player% account";
		
		sender.sendMessage(text.replace("%player%", target.getName() + "'s"));
		if (target.isOnline())
			target.getPlayer().sendMessage(text.replace("%player%", "your"));
	}
}
