
package jdz.UEconomy.commands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import jdz.UEconomy.UEco;
import jdz.UEconomy.UEcoFormatter;
import jdz.UEconomy.data.UEcoBank;
import jdz.bukkitUtils.commands.Command;
import jdz.bukkitUtils.commands.annotations.CommandLabel;
import jdz.bukkitUtils.commands.annotations.CommandMethod;
import jdz.bukkitUtils.commands.annotations.CommandPermission;
import jdz.bukkitUtils.commands.annotations.CommandPlayerOnly;
import jdz.bukkitUtils.messengers.OfflineMessenger;

@CommandLabel("pay")
@CommandPlayerOnly
@CommandPermission("ueco.pay")
public class PayCommand extends Command {
	OfflineMessenger messenger = OfflineMessenger.getMessenger(UEco.getInstance());

	@CommandMethod
	public void pay(Player sender, OfflinePlayer target, double amount) {
		if (!UEcoBank.has(sender, amount)) {
			sender.sendMessage(ChatColor.RED + "You don't have that much money!");
			return;
		}

		UEcoBank.subtract(sender, amount);
		UEcoBank.add(target, amount);

		String amountText = ChatColor.GOLD + "$" + UEcoFormatter.charFormat(amount, 6);
		String targetName = target.isOnline() ? target.getPlayer().getCustomName() : target.getName();

		sender.sendMessage(ChatColor.GREEN + "You paid " + targetName + " " + amountText);

		messenger.message(target, ChatColor.GREEN + sender.getCustomName() + "Paid you " + amountText);
	}
}
