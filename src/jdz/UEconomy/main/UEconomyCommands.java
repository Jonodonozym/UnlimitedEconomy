package jdz.UEconomy.main;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

//@SuppressWarnings("deprecation")
public class UEconomyCommands implements CommandExecutor {
		private final UEconomyAPI plugin;

		public UEconomyCommands(UEconomyAPI plugin) {
			this.plugin = plugin; // Store the plug-in in situations where you need it.
		}

		@Override
		public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
			//commands for both			
			switch(command.getName().toLowerCase()){
			
			case "balancetop":				
				UEconomyAPI.updateBaltop();
				
				int page = 1;
				try { page = Integer.parseInt(args[0]); }
				catch (Exception e) { page = 1;}
				int maxPages = Math.max(1,(UEconomyAPI.baltop.size()+9)/10);
				page = Math.min(maxPages,page);
				
				
				String[] baltopMessage = new String[11];
				int count = page == 1 ? 1 : 0;
				int s = count;
				baltopMessage[0] = ChatColor.GREEN +"=== Baltop page "+page+" of "+ maxPages +" ===";
				if (page == 1)
					baltopMessage[1] = ChatColor.YELLOW+"Server Total: $"+UEconomyAPI.charFormat(UEconomyAPI.serverTotal,UEconomyAPI.BALTOP_DEFAULT_ROUNDING);
				
				for (int i=0; i<10-s; i++){
					try {
						int pos = page*10+i-11+s;
						String other = UEconomyAPI.baltop.get(pos);
						baltopMessage[i+s+1] = pos+1+". "+other+":"+ChatColor.RED+" $"+
								UEconomyAPI.charFormat(UEconomyAPI.baltopValues.get(pos),UEconomyAPI.BALTOP_DEFAULT_ROUNDING);
						if (other != null) count++;
					}
					catch (IndexOutOfBoundsException e) { continue; }
				}
				
				String[] finalBaltopMessage = new String[count+1];
				for (int i=0; i<finalBaltopMessage.length; i++)
					finalBaltopMessage[i] = baltopMessage[i];
				sender.sendMessage(finalBaltopMessage);
				break;

			case "ueco":
				if (args.length == 0)
					return false;
				else switch (args[0].toLowerCase()) {
				case "set":
				case "add":
				case "subtract":
				case "sub":
					if (args.length < 3)
						sender.sendMessage(ChatColor.RED+"Error: expected 3 arguments. /ueco [sub-command] [player] [amount]");
					else {
						OfflinePlayer other = plugin.getServer().getOfflinePlayer(args[1]);
						if (!UEconomyAPI.hasPlayer(other)){
							sender.sendMessage(ChatColor.RED+args[1]+" has never joined the server before!");
							break;
						}
						double amount = Double.parseDouble(UEconomyAPI.charToEngr(args[2]));
						switch (args[0].toLowerCase()){
						case "set":
							UEconomyAPI.setBalance(other, amount);
							sender.sendMessage(ChatColor.GREEN+other.getName()+"'s balance was set to $"+UEconomyAPI.charFormat(amount,UEconomyAPI.BALTOP_DEFAULT_ROUNDING));
							break;
						case "add":
							UEconomyAPI.addBalance(other, amount);
							sender.sendMessage(ChatColor.GREEN+"$"+amount+" was added to "+other.getName()+"'s account");
							break;
						case "sub":
						case "subtract":
							if (UEconomyAPI.hasEnough(other, amount)){
								UEconomyAPI.subBalance(other, amount);
								sender.sendMessage(ChatColor.GREEN+"$"+amount+" was subtracted to "+other.getName()+"'s account");
							}
							else
								sender.sendMessage(ChatColor.RED+other.getName()+" doesn't have enough money in their account to subtract $"+amount);
							break;
						}
					}
					break;
				case "updatebaltop":
					UEconomyAPI.forceBaltopUpdate();
					sender.sendMessage("Baltop successfully updated");
					break;
				default: sender.sendMessage(ChatColor.RED+"/ueco "+args[0]+" is not a command");
				}
				break;

			case "balance":
				if (args.length == 0){
					if (sender instanceof Player){
						Player player = (Player) sender;
						player.sendMessage(ChatColor.GREEN+"Balance: "+ChatColor.RED+"$"+
								UEconomyAPI.charFormat(UEconomyAPI.getBalance(player),UEconomyAPI.BALTOP_DEFAULT_ROUNDING));
					} else
						sender.sendMessage("You're the console, you don't have a balance!");
				}
				else {
					if (UEconomyAPI.hasPlayer(plugin.getServer().getOfflinePlayer(args[0]))){
						double bal = UEconomyAPI.getBalance(plugin.getServer().getOfflinePlayer(args[0]));
						sender.sendMessage(ChatColor.GREEN+"Balance of "+args[0]+": "+ChatColor.RED+"$"+UEconomyAPI.charFormat(bal,UEconomyAPI.BALTOP_DEFAULT_ROUNDING));
					}
					else
						sender.sendMessage(ChatColor.RED+args[0]+" is not a user!");
				}       
				break;

			case "pay":
				if (sender instanceof Player){
					if (args.length > 1) {
						Player player = (Player) sender;
						Player other = plugin.getServer().getPlayer(args[0]);
						if (other != null) {
							double amount = Double.parseDouble(UEconomyAPI.charToEngr(args[1]));
							String val = UEconomyAPI.charFormat(amount,UEconomyAPI.BALTOP_DEFAULT_ROUNDING);
							if (UEconomyAPI.getBalance(player) >= 0){
								UEconomyAPI.addBalance(other, amount);
								UEconomyAPI.subBalance(player, amount);
								player.sendMessage(ChatColor.GREEN+"$"+val+" has been sent to "+other.getName());
								other.sendMessage(ChatColor.GREEN+sender.getName()+" sent you $"+val);
							}
							else
								sender.sendMessage(ChatColor.RED+"You don't have enough money to do that!");
						}
						else
							sender.sendMessage(ChatColor.RED+args[0]+" is not online!");
					}
					sender.sendMessage(ChatColor.RED+"Error: expected 2 arguents. /pay [player] [amount]");
				}
				else
					sender.sendMessage("Consoles can't pay people. Use /ueco set instead.");
				break;
				
			default: plugin.logger.info(command.toString()); return false;
			}
			return true;
		}
	}
