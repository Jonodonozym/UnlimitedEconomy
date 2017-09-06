package jdz.UEconomy.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.Files;

import net.milkbowl.vault.economy.Economy;

public class UEconomyAPI extends JavaPlugin{
	PluginDescriptionFile pdfile = getDescription();
	Logger logger;
	
	static Map<String,Double> economy = new HashMap<String,Double>();
	static List<String> baltop;
	static List<Double> baltopValues;
	
	static double serverTotal;
	private static Calendar lastUpdate;
	
	static int BALTOP_DEFAULT_ROUNDING = 4;
	static double MAX_OVERDRAFT = 0;
	static int BALTOP_UPDATE_DELAY = 5;
	static int PURGE_BALTOP_DAYS = 30;
	static int NUM_BACKUPS = -1;

	static double MAX_CHAR_VALUE = 9.999e35;
	
	@Override
	public void onEnable(){
		logger = getLogger();
		logger.info(pdfile.getName() + " has been enabled (V."+pdfile.getVersion() +")");
		
		loadEconomy();
		loadConfig();
		updateBaltop();

		UEconomyCommands uec = new UEconomyCommands(this);
		this.getCommand("baltop").setExecutor(uec);
		this.getCommand("bal").setExecutor(uec);
		this.getCommand("pay").setExecutor(uec);
		this.getCommand("ueco").setExecutor(uec);
		
		getServer().getPluginManager().registerEvents(new UEconomyJoinEvent(), this);
		
		if (getServer().getPluginManager().getPlugin("Vault") != null)
			Bukkit.getServer().getServicesManager().register(Economy.class, new UEconomyVaultHook(), this, ServicePriority.High);
	}
	
	@Override
	public void onDisable(){
		purgeOldBalances(PURGE_BALTOP_DAYS);
		saveEconomy();
		logger.info(pdfile.getName() + " has been disabled (V."+pdfile.getVersion() +")");
	}
	
	/**
	 * Gets the balance of an OfflinePlayer
	 * @param name of the OfflinePlayer
	 * @return
	 */
	public static double getBalance(String player) {
		if (!economy.containsKey(player)) return 0;
		return economy.get(player);
	}
	
	/**
	 * Gets the balance of an OfflinePlayer
	 * @param player OfflinePlayer instance
	 * @return
	 */
	public static double getBalance(OfflinePlayer player) {
		return getBalance(player.getName());
	}

	
	/**
	 * Adds a specified amount to a player's account
	 * @param player
	 * @param amount
	 */
	public static void addBalance(String player, double amount){
		if (economy.containsKey(player))
			economy.put(player, economy.get(player)+amount);
	}
	
	/**
	 * Adds a specified amount to a player's account
	 * @param player
	 * @param amount
	 */
	public static void addBalance(OfflinePlayer player, double amount){
		addBalance(player.getName(), amount);
	}
	
	/**
	 * Checks if a player has enough money in their account
	 * @param player
	 * @param amount
	 * @return
	 */
	public static boolean hasEnough(String player, double amount){
		if (!economy.containsKey(player)) return false;
		return (economy.get(player) >= amount);
	}
	
	/**
	 * Checks if a player has enough money in their account
	 * @param player
	 * @param amount
	 * @return
	 */
	public static boolean hasEnough(OfflinePlayer player, double amount){
		return hasEnough(player.getName(), amount);
	}
	
	/**
	 * Subtracts a specified amount from a player's account
	 * @param player
	 * @param amount
	 */
	public static void subBalance(String player, double amount){
		if (economy.containsKey(player))
			economy.put(player, economy.get(player) - amount);
	}
	
	/**
	 * Subtracts a specified amount from a player's account
	 * @param player
	 * @param amount
	 */
	public static void subBalance(OfflinePlayer player, double amount){
		subBalance(player.getName(), amount);
	}
	
	/**
	 * Sets a player's account to the specified amount
	 * @param player
	 * @param amount
	 */
	public static void setBalance(String player, double amount){
		if (!economy.containsKey(player)) return;
		if (amount < 0)
			amount = 0;
		economy.put(player, amount);
	}
	
	/**
	 * Sets a player's account to the specified amount
	 * @param player
	 * @param amount
	 */
	public static void setBalance(OfflinePlayer player, double amount){
		setBalance(player.getName(), amount);
	}    
	
	/**
	 * Checks if the economy has an account for a player. If the player
	 * has logged in, then an account is automatically created for them.
	 * @param player
	 * @return
	 */
	public static boolean hasPlayer(String player){
		return economy.containsKey(player);
	}
	
	/**
	 * Checks if the economy has an account for a player. If the player
	 * has logged in, then an account is automatically created for them.
	 * @param player
	 * @return
	 */
	public static boolean hasPlayer(OfflinePlayer player){
		return hasPlayer(player.getName());
	}
	
	static boolean updateBaltop(){
		//only updates every 15 minutes
		if (BALTOP_UPDATE_DELAY <= 0){
			forceBaltopUpdate();
			return true;
		}
		
		Calendar now = Calendar.getInstance();
		int timeDifference = Integer.MAX_VALUE;
		if (lastUpdate != null)
			timeDifference = (int)((now.getTimeInMillis() - lastUpdate.getTimeInMillis()) / 60000);
		if (timeDifference > BALTOP_UPDATE_DELAY){
			forceBaltopUpdate();
			return true;
		}
		
		return false;
	}
	
	static void forceBaltopUpdate(){
		new Thread(){
			@Override
			public void run(){
				setPriority(MIN_PRIORITY);
				baltop = new ArrayList<String>(economy.keySet());
				baltopValues = new ArrayList<Double>();
				
				baltop.sort(new Comparator<String>(){
					@Override
					public int compare(String p1, String p2) {
						return (economy.get(p2).compareTo(economy.get(p1)));
					}
				});
				
				for (String s: baltop)
					baltopValues.add(economy.get(s));

				serverTotal = 0;
				for (double d: economy.values())
					serverTotal += d;
				
				lastUpdate = Calendar.getInstance();
			}
		}.run();
	}
	
	@SuppressWarnings("deprecation")
	private void purgeOldBalances(int days){
		if (days <= 0) return;
		long milis = days * 86400000L;
		Set<String> toRemove = new HashSet<String>();
		for (String p: economy.keySet()){
			if (getServer().getOfflinePlayer(p).getLastPlayed() > milis)
				toRemove.add(p);
		}
		for (String p: toRemove)
			economy.remove(p);
	}
	
	private void loadConfig(){
		File file = new File(getDataFolder() + File.separator + "config.yml");
		if(!file.exists())
			this.saveDefaultConfig();
		else{
			BALTOP_UPDATE_DELAY = this.getConfig().getInt("baltopUpdate");
			PURGE_BALTOP_DAYS = this.getConfig().getInt("purgeDays");
			NUM_BACKUPS = this.getConfig().getInt("numBackups");
		}
	}
	
	private void loadEconomy(){
		File dataFolder = getDataFolder();
		if(!dataFolder.exists())
		    dataFolder.mkdir();
		File f = new File(getDataFolder(), "playerBalances.txt");
		if (f.exists()){
			try {
				BufferedReader br = new BufferedReader( new FileReader(f));
				economy = new HashMap<String,Double>();
				
				int numPlayers = Integer.parseInt(br.readLine());
				for (int i=0 ; i<numPlayers; i++){
					String[] args = br.readLine().split("\\|");
					String player = args[0];
					double balance = Double.parseDouble(args[1]);
					economy.put(player, balance);
				}
				br.close();
				logger.info("Loading player economy successful!");
				
			} catch (IOException e) { e.printStackTrace(); }
		}
	}
	
	private void saveEconomy(){

		if(!getDataFolder().exists())
			getDataFolder().mkdir();
		
		File f = new File(getDataFolder(), "playerBalances.txt");
		if(f.exists()){
			Calendar now = Calendar.getInstance();
			int year = now.get(Calendar.YEAR);
			int month = now.get(Calendar.MONTH) + 1; // Note: zero based!
			int day = now.get(Calendar.DAY_OF_MONTH);
			int hour = now.get(Calendar.HOUR_OF_DAY);
			int minute = now.get(Calendar.MINUTE);
			int second = now.get(Calendar.SECOND);

			String timestamp = String.format("%d-%02d-%02d %02d-%02d-%02d", year, month, day, hour, minute, second);
			
			File backupDir = new File(getDataFolder() + File.separator + "Backups");
			
			if (!backupDir.exists())
				backupDir.mkdir();
			
			File[] fileList = backupDir.listFiles();
			if (NUM_BACKUPS > 0 && fileList.length >= NUM_BACKUPS+1)
				for (int i=0; i<fileList.length-NUM_BACKUPS; i++){
					fileList[i].delete();
					logger.info("backup deleted");
				}
				
			try {
				Files.copy(f, new File(backupDir,"playerBalances - "+timestamp+".txt"));
				logger.info("Previous economy was backed up successfully.");
			} catch (IOException e) { logger.info("Error: previous economy failed to back-up"); }

			f = new File(getDataFolder(), "playerBalances.txt");
		}
		try {
			BufferedWriter bw = new BufferedWriter( new FileWriter(f));
			
			bw.write(""+economy.keySet().size());
			for (String p: economy.keySet()){
				bw.newLine();
				bw.write(p+"|"+String.format("%10.9e", economy.get(p)));
			}
			
			bw.flush();
			bw.close();
			logger.info("Saving player economy successful!");
		} catch (IOException e) { e.printStackTrace(); }
	}
	

	public static String charFormat(double value, int significantFigures){
		return charFormat(value, significantFigures, 3, false);
	}
	
	public static String charFormat(double value, int significantFigures, boolean isWholeNumber){
		return charFormat(value, significantFigures, 3, isWholeNumber);
	}
	
	/**
	 * Helper method to format doubles using character notation instead of scientific. Goes up to 100Dc (1E+35)
	 * @param value
	 * @param minimumRadix Any numbers with a radix (1*10^radix) above this will be in scientific notation
	 * @param significant figures The number of significant figures in the number. Suggested is at least 4
	 * @return	A string of the number
	 */
	public static String charFormat(double val, int significantFigures, int minRadix, boolean isWholeNumber){

        if (val <= 0) return "0";
        
        significantFigures = significantFigures < 1? 1: significantFigures;
		String s = String.format("%"+significantFigures+"."+(significantFigures-1)+"e", val);
		String[] args = s.split("e");
		if (args.length == 1)
			return String.format("%.2f", Double.parseDouble(s));
		int dp = 1;
		int radix = Integer.parseInt(args[1]);
		while (radix % 3 != 0){ radix--; dp++; }
		
		if (radix < minRadix){
			if (!isWholeNumber || val%1.0 != 0)
				return String.format("%.2f", val);
			return (int)val+"";
		}

		args[0] = args[0].replace(".", "");
		if (dp < significantFigures){
			args[0] = args[0].substring(0, dp)+"."+args[0].substring(dp);
		}
		else{
			while (args[0].length() < dp){
				args[0] = args[0]+"0";
			}
		}
		
		String suffix = engrToChar(radix);
        if (suffix.equals("NaN")) return s;
        return args[0]+suffix;
	}
	
	public static String charToEngr(String string){
		String suffix = "";
		int len = 0;
		if (string.length() > 1)
		switch(string.substring(string.length()-1).toLowerCase()){
			case "k": suffix = "e+3"; len = 1; break;
			case "m": suffix = "e+6"; len = 1; break;
			case "b": suffix = "e+9"; len = 1; break;
			case "t": suffix = "e+12"; len = 1; break;
		}
		if (string.length() > 2)
		switch (string.substring(string.length()-2).toLowerCase()){
			case "qa": suffix = "e+15"; len = 2; break;
			case "qi": suffix = "e+18"; len = 2; break;
			case "sx": suffix = "e+21"; len = 2; break;
			case "sp": suffix = "e+24"; len = 2; break;
			case "oc": suffix = "e+27"; len = 2; break;
			case "no": suffix = "e+30"; len = 2; break;
			case "dc": suffix = "e+33"; len = 2; break;
		}
		return string.substring(0,string.length()-len)+suffix;
	}
	
	
	private static String engrToChar(int radix){
		String suffix = "";
			switch(radix){
			case 3: suffix = "k"; break;
			case 6: suffix = "M"; break;
			case 9: suffix = "B"; break;
			case 12: suffix = "T"; break;
			case 15: suffix = "Qa"; break;
			case 18: suffix = "Qi"; break;
			case 21: suffix = "Sx"; break;
			case 24: suffix = "Sp"; break;
			case 27: suffix = "Oc"; break;
			case 30: suffix = "No"; break;
			case 33: suffix = "Dc"; break;
			default:	return "NaN";
			}
			return suffix;
	}
}