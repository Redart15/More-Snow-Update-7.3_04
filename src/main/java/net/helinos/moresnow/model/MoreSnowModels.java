package net.helinos.moresnow.model;

import net.helinos.moresnow.block.init.MoreSnowBlocks;
import net.helinos.moresnow.block.logic.BlockLogicSnowyFencePainted;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.collection.NamespaceID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.helinos.moresnow.MoreSnow.LAYERS;
import static net.helinos.moresnow.block.init.MoreSnowBlocks.*;

public class MoreSnowModels {
	public static final String SNOW_PATH = "minecraft:block/block_snow";
	public static final double zFactor = 0.0001;

	public static void initBlockModels(BlockModelDispatcher dispatcher) {
		String texID = "minecraft:block/block_snow";
		MoreSnowModels.assignModelFlower(dispatcher, texID);
		MoreSnowModels.assignModelSapling(dispatcher, texID);
		MoreSnowModels.assignModelSlab(dispatcher, texID);
		MoreSnowModels.assignModelStairs(dispatcher, texID);
		MoreSnowModels.assignModelFence(dispatcher, texID);
		MoreSnowModels.assignModelFenceThin(dispatcher, texID);
		MoreSnowModels.assignModelFenceGate(dispatcher, texID);
//			MoreSnowModels.assignModelTrapDoor(dispatcher, model, texID);
//		}

	}

	private static Block<?> getLayerBlock(Block<?> block) {
		NamespaceID namespaceId = block.namespaceId();
		String[] parts = namespaceId.value().split("[/_]");
		return parts.length > 2 ? LAYERS.getItem(parts[1]) : Blocks.LAYER_SNOW;
	}

	private static void assignModelFlower(BlockModelDispatcher dispatcher, String texID) {
		for (Block<?> block : SNOWY_FLOWERS) {
			BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(getLayerBlock(block));
			dispatcher.addDispatch(new BlockModelSnowyPlant<>(block, model, texID).setAllTextures(0, SNOW_PATH));
		}

	}

	private static void assignModelSapling(BlockModelDispatcher dispatcher, String texID) {
	}

	private static void assignModelSlab(BlockModelDispatcher dispatcher, String texID) {
		for (Block<?> block : SNOWY_SLAB) {
			BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(getLayerBlock(block));
			dispatcher.addDispatch(new BlockModelSnowySlab<>(block, model, texID).setAllTextures(0, SNOW_PATH));
		}

		for (Block<?> block : SNOWY_SLAB_PAINTED) {
			BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(getLayerBlock(block));
			dispatcher.addDispatch(new BlockModelSnowySlabPainted(block, model, texID).setAllTextures(0, SNOW_PATH));
		}

	}

	private static void assignModelStairs(BlockModelDispatcher dispatcher, String texID) {
		for (Block<?> block : SNOWY_STAIRS) {
			BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(getLayerBlock(block));
			dispatcher.addDispatch(new BlockModelSnowyStairs<>(block, model, texID).setAllTextures(0, SNOW_PATH));
		}

		for (Block<?> block : SNOWY_STAIRS_PAINTED) {
			BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(getLayerBlock(block));
			dispatcher.addDispatch(new BlockModelSnowyStairsPainted(block, model, texID).setAllTextures(0, SNOW_PATH));
		}

	}

	private static void assignModelFence(BlockModelDispatcher dispatcher, String texID) {
		for (Block<?> block : SNOWY_FENCE) {
			BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(getLayerBlock(block));
			dispatcher.addDispatch(new BlockModelSnowyFence<>(block, model, texID).setAllTextures(0, SNOW_PATH));
		}

		for (Block<BlockLogicSnowyFencePainted<?>> block : SNOWY_FENCE_PAINTED) {
			BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(getLayerBlock(block));
			dispatcher.addDispatch(new BlockModelSnowyFencePainted<>(block, model, texID).setAllTextures(0, SNOW_PATH));
		}

	}

	private static void assignModelFenceThin(BlockModelDispatcher dispatcher, String texID) {
		BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(Blocks.LAYER_SNOW);
		dispatcher.addDispatch(new BlockModelSnowyFenceThin<>(MoreSnowBlocks.SNOWY_FENCE_CHAINLINK, model, texID).setAllTextures(0, SNOW_PATH));
		dispatcher.addDispatch(new BlockModelSnowyFenceThin<>(MoreSnowBlocks.SNOWY_FENCE_STEEL, model, texID).setAllTextures(0, SNOW_PATH));
		dispatcher.addDispatch(new BlockModelSnowyFenceThin<>(MoreSnowBlocks.SNOWY_FENCE_WALLPAPER, model, texID).setAllTextures(0, SNOW_PATH));

		model = BlockModelDispatcher.getInstance().getDispatch(Blocks.LAYER_LEAVES_OAK);
		dispatcher.addDispatch(new BlockModelSnowyFenceThin<>(MoreSnowBlocks.LEAVY_FENCE_CHAINLINK, model, texID).setAllTextures(0, SNOW_PATH));
		dispatcher.addDispatch(new BlockModelSnowyFenceThin<>(MoreSnowBlocks.LEAVY_FENCE_STEEL, model, texID).setAllTextures(0, SNOW_PATH));
		dispatcher.addDispatch(new BlockModelSnowyFenceThin<>(MoreSnowBlocks.LEAVY_FENCE_WALLPAPER, model, texID).setAllTextures(0, SNOW_PATH));

		model = BlockModelDispatcher.getInstance().getDispatch(Blocks.LAYER_SLATE);
		dispatcher.addDispatch(new BlockModelSnowyFenceThin<>(MoreSnowBlocks.SLATY_FENCE_CHAINLINK, model, texID).setAllTextures(0, SNOW_PATH));
		dispatcher.addDispatch(new BlockModelSnowyFenceThin<>(MoreSnowBlocks.SLATY_FENCE_STEEL, model, texID).setAllTextures(0, SNOW_PATH));
		dispatcher.addDispatch(new BlockModelSnowyFenceThin<>(MoreSnowBlocks.SLATY_FENCE_WALLPAPER, model, texID).setAllTextures(0, SNOW_PATH));
	}

	private static void assignModelFenceGate(BlockModelDispatcher dispatcher, String texID) {
		for (Block<?> block : SNOWY_FENCE_GATE) {
			BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(getLayerBlock(block));
			dispatcher.addDispatch(new BlockModelSnowyFenceGate<>(block, model, texID).setAllTextures(0, SNOW_PATH));
		}

		for (Block<?> block : SNOWY_FENCE_GATES_PAINTED) {
			BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(getLayerBlock(block));
			dispatcher.addDispatch(new BlockModelSnowyFenceGatePainted(block, model, texID).setAllTextures(0, SNOW_PATH));
		}

	}

	private static void assignModelTrapDoor(BlockModelDispatcher dispatcher, String texID) {
//		BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(layerBlock);
//		for (Block<?> block : SNOWY_TRAPDOOR) {
//			dispatcher.addDispatch(new BlockModelSnowyTrapDoor(block, model, texID).setAllTextures(0, SNOW_PATH));
//		}
//
//		for (Block<?> block : SNOWY_TRAPDOOR_PAINTED) {
////			dispatcher.addDispatch(new BlockModelSnowyFenceGatePainted(block).setAllTextures(0, SNOW_PATH));
//		}
	}

	public void initItemModels(ItemModelDispatcher dispatcher, String texID) {
	}

	public void initEntityModels(EntityRenderDispatcher dispatcher, String texID) {
	}

	public void initTileEntityModels(TileEntityRenderDispatcher dispatcher, String texID) {
	}

	public void initBlockColors(BlockColorDispatcher dispatcher, String texID) {
	}
}
