
package jdz.UEconomy.data;

import java.util.List;
import java.util.UUID;

interface UEcoDatabase {
	public static UEcoDatabase getInstance() {
		return null; // TODO
	}
	
	public UEcoEntry get(UUID uuid);
	public UEcoEntry get(String name);
	public List<UEcoEntry> getAll();
	
	public void set(UUID uuid, double amount);
	public void add(UUID uuid, double amount);
}
