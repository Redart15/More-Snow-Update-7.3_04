package net.helinos.moresnow;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.helinos.moresnow.command.MoreSnowCommand;

import static net.helinos.moresnow.MoreSnow.LOGGER;

public class MoreSnowServer implements DedicatedServerModInitializer {

	@Override
	public void onInitializeServer() {
		LOGGER.info("Register server side commands.");
		MoreSnowCommand.registerServerCommands();
	}
}
