
package jdz.UEconomy.data;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;

import jdz.UEconomy.UEco;
import jdz.bukkitUtils.sql.SqlDatabase;
import lombok.Getter;

class UEcoDatabaseSQL extends SqlDatabase implements UEcoDatabase {
	@Getter private static final UEcoDatabaseSQL instance = new UEcoDatabaseSQL();

	private UEcoDatabaseSQL() {
		super(UEco.getInstance());
		UEcoEntry.createTable(UEcoEntry.class, this);
		UEcoEntry.createIndex(UEcoEntry.class, "name", this);
	}

	@Override
	public UEcoEntry get(UUID uuid) {
		UEcoEntry entry = UEcoEntry.selectFirst(this, UEcoEntry.class, "WHERE uuid = '" + uuid + "'");
		if (entry == null)
			entry = new UEcoEntry(Bukkit.getOfflinePlayer(uuid), 0);
		return entry;
	}

	@SuppressWarnings("deprecation")
	@Override
	public UEcoEntry get(String name) {
		UEcoEntry entry = UEcoEntry.selectFirst(this, UEcoEntry.class, "WHERE name = '" + name + "'");
		if (entry == null)
			entry = new UEcoEntry(Bukkit.getOfflinePlayer(name), 0);
		return entry;
	}

	@Override
	public List<UEcoEntry> getAll() {
		return UEcoEntry.selectAll(this, UEcoEntry.class);
	}

	@Override
	public void set(UUID uuid, double amount) {
		executeTransaction(() -> {
			UEcoEntry entry = UEcoEntry.selectFirstForUpdate(this, UEcoEntry.class, "WHERE uuid = '" + uuid + "'");
			if (entry == null)
				entry = new UEcoEntry(uuid, Bukkit.getOfflinePlayer(uuid).getName(), 0);
			entry.setBalance(amount);

			entry.update(this);
			return true;
		});
	}

	@Override
	public void add(UUID uuid, double amount) {
		executeTransaction(() -> {
			UEcoEntry entry = UEcoEntry.selectFirstForUpdate(this, UEcoEntry.class, "WHERE uuid = '" + uuid + "'");
			if (entry == null)
				entry = new UEcoEntry(uuid, Bukkit.getOfflinePlayer(uuid).getName(), 0);
			entry.setBalance(entry.getBalance() + amount);

			entry.update(this);
			return true;
		});
	}
}
