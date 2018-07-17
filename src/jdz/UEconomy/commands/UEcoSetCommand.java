
package jdz.UEconomy.commands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import jdz.UEconomy.UEcoFormatter;
import jdz.UEconomy.data.UEcoBank;
import jdz.bukkitUtils.commands.SubCommand;
import jdz.bukkitUtils.commands.annotations.CommandLabel;
import jdz.bukkitUtils.commands.annotations.CommandMethod;

@CommandLabel("set")
public class UEcoSetCommand extends SubCommand {

	@CommandMethod
	public void set(CommandSender sender, OfflinePlayer target, double amount) {
		UEcoBank.set(target, amount);

		String amountText = ChatColor.YELLOW + "$" + UEcoFormatter.charFormat(amount, 5);
		String text = ChatColor.GREEN + "%player% account has been set to " + ChatColor.GOLD + amountText;

		sender.sendMessage(text.replace("%player%", target.getName() + "'s"));
		if (target.isOnline())
			target.getPlayer().sendMessage(text.replace("%player%", "your"));
	}
}
