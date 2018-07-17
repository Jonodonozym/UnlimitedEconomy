
package jdz.UEconomy.data;

import java.util.UUID;

import org.bukkit.OfflinePlayer;

import jdz.bukkitUtils.sql.ORM.PrimaryKey;
import jdz.bukkitUtils.sql.ORM.SQLDataClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Wither;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = { "uuid" }, callSuper = false)
public class UEcoEntry extends SQLDataClass implements Comparable<UEcoEntry>{
	@PrimaryKey private final UUID uuid;
	private String name;
	@Wither private double balance;

	public UEcoEntry(OfflinePlayer player, double balance) {
		this.uuid = player.getUniqueId();
		this.name = player.getName();
		this.balance = balance;
	}

	@Override
	public int compareTo(UEcoEntry o) {
		return new Double(getBalance() - o.getBalance()).intValue();
	}
}
