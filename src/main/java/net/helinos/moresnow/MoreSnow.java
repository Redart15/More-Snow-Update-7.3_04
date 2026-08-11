package net.helinos.moresnow;

import net.fabricmc.api.ModInitializer;
import net.helinos.moresnow.block.MoreSnowBlocks;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.registry.Registry;
import net.minecraft.core.lang.I18n;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.event.defs.CommonEvents;
import turniplabs.halplibe.util.dependency.Key;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoreSnow implements ModInitializer{
	public static final String MOD_ID = HalpLibe.registerMod("moresnow");
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	protected static final Key KEY = Key.of(MOD_ID);
	public static final Registry<Block<?>> LAYERS = new Registry<>();
	public static I18n LANGUAGE = null;

	@Override
	public void onInitialize() {
		LOGGER.info("Initialize MoreSnow.");
		CommonEvents.BEFORE_GAME_START.listen(KEY, this::beforeGameStart);
		CommonEvents.AFTER_GAME_START.listen(KEY, this::afterGameStart);
	}

	public void afterGameStart(){
		LANGUAGE = I18n.getInstance();
	}

	public void beforeGameStart() {
		LOGGER.info("Register more snow layers.");
		MoreSnow.LAYERS.register("snow", Blocks.LAYER_SNOW);
		MoreSnow.LAYERS.register("leaves", Blocks.LAYER_LEAVES_OAK);
		MoreSnow.LAYERS.register("slate", Blocks.LAYER_SLATE);
		MoreSnow.LAYERS.register("ashe", Blocks.LAYER_ASH);
	}
}
