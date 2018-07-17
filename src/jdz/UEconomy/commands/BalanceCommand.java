
package jdz.UEconomy.commands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import jdz.UEconomy.UEcoFormatter;
import jdz.UEconomy.data.UEcoBank;
import jdz.bukkitUtils.commands.Command;
import jdz.bukkitUtils.commands.annotations.CommandLabel;
import jdz.bukkitUtils.commands.annotations.CommandMethod;

@CommandLabel("bal")
public class BalanceCommand extends Command {

	@CommandMethod
	public void showBalance(CommandSender sender) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You're the console, you don't have a balance!");
			return;
		}
		Player player = (Player) sender;
		sender.sendMessage(ChatColor.GOLD + UEcoFormatter.charFormat(UEcoBank.get(player), 6));
	}


	@CommandMethod
	public void showBalance(CommandSender sender, OfflinePlayer target) {
		sender.sendMessage(ChatColor.GOLD + UEcoFormatter.charFormat(UEcoBank.get(target), 6));
	}
}
