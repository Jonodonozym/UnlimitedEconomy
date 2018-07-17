
package jdz.UEconomy.data;

import java.util.List;
import java.util.UUID;

import jdz.UEconomy.UEco;
import jdz.bukkitUtils.misc.Config;

interface UEcoDatabase {
	public static final boolean useSQL = Config.getSQLConfig(UEco.getInstance()).isPreferSQL();

	public static UEcoDatabase getInstance() {
		if (useSQL)
			return UEcoDatabaseSQL.getInstance();
		return UEcoDatabaseYML.getInstance();
	}

	public UEcoEntry get(UUID uuid);

	public UEcoEntry get(String name);

	public List<UEcoEntry> getAll();

	public void set(UUID uuid, double amount);

	public void add(UUID uuid, double amount);
}
