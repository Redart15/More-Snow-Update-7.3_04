package net.helinos.moresnow.command;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.net.command.CommandManager;

public class MoreSnowCommand {

	@Environment(EnvType.CLIENT)
	public static void registerClientCommands() {
		CommandManager.registerCommand(new CommandRemoveSnowy());
	}

	@Environment(EnvType.SERVER)
	public static void registerServerCommands() {
		CommandManager.registerCommand(new CommandRemoveSnowy());
	}
}
