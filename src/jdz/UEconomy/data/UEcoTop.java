
package jdz.UEconomy.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;

import jdz.UEconomy.UEco;
import lombok.Getter;

public class UEcoTop {
	private static List<UEcoEntry> top = new ArrayList<UEcoEntry>();
	private static Map<String, Integer> positions = new HashMap<String, Integer>();
	@Getter private static double serverTotal = 0;
	private static long lastUpdate = System.currentTimeMillis();

	public static List<UEcoEntry> getTop() {
		if (System.currentTimeMillis() - lastUpdate > 600000)
			update();
		return Collections.unmodifiableList(top);
	}
	
	public static int getPosition(String name) {
		if (!positions.containsKey(name))
			return -1;
		return positions.get(name);
	}

	public static void update() {
		Bukkit.getScheduler().runTaskAsynchronously(UEco.getInstance(), () -> {
			lastUpdate = System.currentTimeMillis();
			top = UEcoDatabase.getInstance().getAll();
			top.sort((a, b) -> {
				return new Double(a.getBalance() - b.getBalance()).intValue();
			});
			
			positions.clear();
			int i=0;
			for (UEcoEntry entry: top)
				positions.put(entry.getName(), ++i);
			
			serverTotal = 0;
			for (UEcoEntry entry: top) {
				serverTotal += entry.getBalance();
				if (serverTotal > entry.getBalance() * 1e12)
					break;
			}
		});
	}

}
