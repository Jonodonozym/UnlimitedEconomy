
package jdz.UEconomy.commands;

import java.util.Arrays;
import java.util.List;

import jdz.UEconomy.UEco;
import jdz.UEconomy.UEcoFormatter;
import jdz.bukkitUtils.ArgumentParsers;
import jdz.bukkitUtils.commands.AboutPluginCommand;
import jdz.bukkitUtils.commands.CommandExecutor;
import jdz.bukkitUtils.commands.SubCommand;
import jdz.bukkitUtils.commands.annotations.CommandExecutorPermission;
import lombok.Getter;

@CommandExecutorPermission("ueco.admin")
public class UEcoCommandExecutor extends CommandExecutor {
	@Getter private static final UEcoCommandExecutor instance = new UEcoCommandExecutor();
	@Getter private final List<SubCommand> subCommands = Arrays.asList(new UEcoSetCommand(), new UEcoAddCommand(),
			new UEcoSubCommand(), new AboutPluginCommand(UEco.getInstance()));

	private UEcoCommandExecutor() {
		super(UEco.getInstance(), "ueco", false);
		ArgumentParsers.addParser(double.class, (s) -> {
			return UEcoFormatter.parse(s);
		});
		ArgumentParsers.addParser(Double.class, (s) -> {
			return UEcoFormatter.parse(s);
		});
	}

}
