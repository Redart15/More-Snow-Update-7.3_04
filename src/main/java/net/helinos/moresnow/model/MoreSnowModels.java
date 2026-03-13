package net.helinos.moresnow.model;

import net.helinos.moresnow.block.init.MoreSnowBlocks;
import net.helinos.moresnow.block.logic.BlockLogicSnowyFencePainted;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;

import static net.helinos.moresnow.block.init.MoreSnowBlocks.*;

public class MoreSnowModels {
	public static final String SNOW_PATH = "minecraft:block/block_snow";

	public static void initBlockModels(BlockModelDispatcher dispatcher) {
		MoreSnowModels.assignModelFlower(dispatcher);
		MoreSnowModels.assignModelSapling(dispatcher);
		MoreSnowModels.assignModelSlab(dispatcher);
		MoreSnowModels.assignModelStairs(dispatcher);
		MoreSnowModels.assignModelFence(dispatcher);
		MoreSnowModels.assignModelFenceThin(dispatcher);
		MoreSnowModels.assignModelFenceGate(dispatcher);
	}

	private static void assignModelFlower(BlockModelDispatcher dispatcher) {
		for (Block<?> block : SNOWY_FLOWERS) {
			dispatcher.addDispatch(new BlockModelSnowyPlant<>(block).setAllTextures(0, SNOW_PATH));
		}
	}

	private static void assignModelSapling(BlockModelDispatcher dispatcher) {
	}

	private static void assignModelSlab(BlockModelDispatcher dispatcher) {
		for (Block<?> block : SNOWY_SLAB) {
			dispatcher.addDispatch(new BlockModelSnowySlab<>(block).setAllTextures(0, SNOW_PATH));
		}

		for (Block<?> block : SNOWY_SLAB_PAINTED) {
			dispatcher.addDispatch(new BlockModelSnowySlabPainted(block).setAllTextures(0, SNOW_PATH));
		}
	}

	private static void assignModelStairs(BlockModelDispatcher dispatcher) {
		for (Block<?> block : SNOWY_STAIRS) {
			dispatcher.addDispatch(new BlockModelSnowyStairs<>(block).setAllTextures(0, SNOW_PATH));
		}

		for (Block<?> block : SNOWY_STAIRS_PAINTED) {
			dispatcher.addDispatch(new BlockModelSnowyStairsPainted(block).setAllTextures(0, SNOW_PATH));
		}
	}

	private static void assignModelFence(BlockModelDispatcher dispatcher) {
		for (Block<?> block : SNOWY_FENCE) {
			dispatcher.addDispatch(new BlockModelSnowyFence<>(block).setAllTextures(0, SNOW_PATH));
		}

		for (Block<BlockLogicSnowyFencePainted<?>> block : SNOWY_FENCE_PAINTED) {
			dispatcher.addDispatch(new BlockModelSnowyFencePainted<>(block).setAllTextures(0, SNOW_PATH));
		}
	}

	private static void assignModelFenceThin(BlockModelDispatcher dispatcher) {
		dispatcher.addDispatch(new BlockModelSnowyFenceThin<>(
			MoreSnowBlocks.SNOWY_FENCE_CHAINLINK,
			Blocks.FENCE_CHAINLINK.getLogic().getClass(),
			TextureRegistry.getTexture("minecraft:block/fence_chain/center"),
			null,
			TextureRegistry.getTexture("minecraft:block/fence_chain/top"),
			TextureRegistry.getTexture("minecraft:block/fence_chain/column")
		).setAllTextures(0, SNOW_PATH));

		dispatcher.addDispatch(new BlockModelSnowyFenceThin<>(
			MoreSnowBlocks.SNOWY_FENCE_STEEL,
			Blocks.FENCE_STEEL.getLogic().getClass(),
			TextureRegistry.getTexture("minecraft:block/fence_steel/center"),
			null,
			TextureRegistry.getTexture("minecraft:block/fence_steel/top"),
			TextureRegistry.getTexture("minecraft:block/fence_steel/column")
		).setAllTextures(0, SNOW_PATH));

		dispatcher.addDispatch(new BlockModelSnowyFenceThin<>(
			MoreSnowBlocks.SNOWY_FENCE_WALLPAPER,
			Blocks.FENCE_PAPER_WALL.getLogic().getClass(),
			TextureRegistry.getTexture("minecraft:block/fence_paper/center"),
			null,
			null,
			TextureRegistry.getTexture("minecraft:block/fence_paper/column")
		).setAllTextures(0, SNOW_PATH));

	}

	private static void assignModelFenceGate(BlockModelDispatcher dispatcher) {
		for (Block<?> block : SNOWY_FENCE_GATE) {
			dispatcher.addDispatch(new BlockModelSnowyFenceGate<>(block).setAllTextures(0, SNOW_PATH));
		}

		for (Block<?> block : SNOWY_FENCE_GATES_PAINTED) {
			dispatcher.addDispatch(new BlockModelSnowyFenceGatePainted(block).setAllTextures(0, SNOW_PATH));
		}
	}

	public void initItemModels(ItemModelDispatcher dispatcher) {
	}

	public void initEntityModels(EntityRenderDispatcher dispatcher) {
	}

	public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {
	}

	public void initBlockColors(BlockColorDispatcher dispatcher) {
	}
}
