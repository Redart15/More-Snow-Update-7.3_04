package net.helinos.moresnow;

import net.fabricmc.api.ClientModInitializer;
import net.helinos.moresnow.command.MoreSnowCommand;
import net.helinos.moresnow.model.MoreSnowModels;
import turniplabs.halplibe.event.defs.ClientEvents;

import static net.helinos.moresnow.MoreSnow.KEY;
import static net.helinos.moresnow.MoreSnow.LOGGER;

public class MoreSnowClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		LOGGER.info("Register client side commands.");
		MoreSnowCommand.registerClientCommands();
		ClientEvents.BLOCK_MODEL_RELOAD.listen(KEY, MoreSnowModels::initBlockModels);
	}
}
