package net.helinos.moresnow.block;

import net.helinos.moresnow.MoreSnow;
import net.helinos.moresnow.block.init.MoreSnowBlocks;
import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.helinos.moresnow.block.logic.BlockLogicSnowyPlant;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicFlower;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;

import java.util.ArrayList;

import turniplabs.halplibe.util.toml.Toml;
import turniplabs.halplibe.util.BlockInitEntrypoint;

public class MSBlocks implements BlockInitEntrypoint {
//	public static Block<?> SNOWY_PARTIAL;
	public static int[] blockIds;

	private static Toml rawConfig;
	private static boolean configChanged = false;

	public void afterBlockInit() {
		MoreSnow.LOGGER.info("Initializing Blocks.");
		MoreSnowBlocks.init();
		rawConfig = MoreSnow.CONFIG.getRawParsed();

	}

	public static boolean tryMakeSnowy(World world, int id, int x, int y, int z) {
		return MoreSnowBlocks.convertBlock(world, id, x, y, z);
	}

	public static boolean tryMakeSnowy(Chunk chunk, int id, int x, int y, int z) {
		return MoreSnowBlocks.convertBlock(chunk, id, x, y, z);
	}
}
