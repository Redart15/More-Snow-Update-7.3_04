package net.helinos.moresnow.model;

import net.helinos.moresnow.block.MoreSnowBlocks;
import net.helinos.moresnow.block.logic.*;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;

import static net.helinos.moresnow.block.MoreSnowBlocks.*;
import static net.helinos.moresnow.MoreSnow.*;

public class MoreSnowModels {
	public static final double zFactor = 0.0001;

	private MoreSnowModels(){}

	public static void initBlockModels(BlockModelDispatcher dispatcher) {
		LOGGER.info("Assign models to snowy blocks.");
		MoreSnowModels.assignModelCrossed(dispatcher);
		MoreSnowModels.assignModelSlab(dispatcher);
		MoreSnowModels.assignModelStairs(dispatcher);
		MoreSnowModels.assignModelFence(dispatcher);
		MoreSnowModels.assignModelFenceThin(dispatcher);
		MoreSnowModels.assignModelFenceGate(dispatcher);
		LOGGER.info("Finished assigning models.");
	}

	private static void assignModelCrossed(BlockModelDispatcher dispatcher){
		for (Block<? extends BlockLogicSnowy<?>> block : SNOWY_FLOWERS) {
			BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(getLayerBlock(block));
			dispatcher.addDispatch(new BlockModelSnowyCrossed<>(block, model));
		}
	}

	private static void assignModelSlab(BlockModelDispatcher dispatcher) {
		for (Block<? extends BlockLogicSnowy<?>> block : SNOWY_SLAB) {
			BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(getLayerBlock(block));
			dispatcher.addDispatch(new BlockModelSnowySlab<>(block, model));
		}

		for (Block<BlockLogicSnowySlabPainted<?>> block : SNOWY_SLAB_PAINTED) {
			BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(getLayerBlock(block));
			dispatcher.addDispatch(new BlockModelSnowySlabPainted<>(block, model));
		}

	}

	private static void assignModelStairs(BlockModelDispatcher dispatcher) {
		for (Block<? extends BlockLogicSnowy<?>> block : SNOWY_STAIRS) {
			BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(getLayerBlock(block));
			dispatcher.addDispatch(new BlockModelSnowyStairs<>(block, model));
		}

		for (Block<BlockLogicSnowyStairsPainted<?>> block : SNOWY_STAIRS_PAINTED) {
			BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(getLayerBlock(block));
			dispatcher.addDispatch(new BlockModelSnowyStairsPainted<>(block, model));
		}

	}

	private static void assignModelFence(BlockModelDispatcher dispatcher) {
		for (Block<? extends BlockLogicSnowy<?>> block : SNOWY_FENCE) {
			BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(getLayerBlock(block));
			dispatcher.addDispatch(new BlockModelSnowyFence<>(block, model));
		}

		for (Block<BlockLogicSnowyFencePainted<?>> block : SNOWY_FENCE_PAINTED) {
			BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(getLayerBlock(block));
			dispatcher.addDispatch(new BlockModelSnowyFencePainted<>(block, model));
		}

	}

	private static void assignModelFenceThin(BlockModelDispatcher dispatcher) {
		BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(Blocks.LAYER_SNOW);
		dispatcher.addDispatch(new BlockModelSnowyFenceThin<>(MoreSnowBlocks.SNOWY_FENCE_CHAINLINK, model));
		dispatcher.addDispatch(new BlockModelSnowyFenceThin<>(MoreSnowBlocks.SNOWY_FENCE_STEEL, model));
		dispatcher.addDispatch(new BlockModelSnowyFenceThin<>(MoreSnowBlocks.SNOWY_FENCE_WALLPAPER, model));

		model = BlockModelDispatcher.getInstance().getDispatch(Blocks.LAYER_LEAVES_OAK);
		dispatcher.addDispatch(new BlockModelSnowyFenceThin<>(MoreSnowBlocks.LEAVY_FENCE_CHAINLINK, model));
		dispatcher.addDispatch(new BlockModelSnowyFenceThin<>(MoreSnowBlocks.LEAVY_FENCE_STEEL, model));
		dispatcher.addDispatch(new BlockModelSnowyFenceThin<>(MoreSnowBlocks.LEAVY_FENCE_WALLPAPER, model));

		model = BlockModelDispatcher.getInstance().getDispatch(Blocks.LAYER_SLATE);
		dispatcher.addDispatch(new BlockModelSnowyFenceThin<>(MoreSnowBlocks.SLATY_FENCE_CHAINLINK, model));
		dispatcher.addDispatch(new BlockModelSnowyFenceThin<>(MoreSnowBlocks.SLATY_FENCE_STEEL, model));
		dispatcher.addDispatch(new BlockModelSnowyFenceThin<>(MoreSnowBlocks.SLATY_FENCE_WALLPAPER, model));

		model = BlockModelDispatcher.getInstance().getDispatch(Blocks.LAYER_ASH);
		dispatcher.addDispatch(new BlockModelSnowyFenceThin<>(MoreSnowBlocks.ASHY_FENCE_CHAINLINK, model));
		dispatcher.addDispatch(new BlockModelSnowyFenceThin<>(MoreSnowBlocks.ASHY_FENCE_STEEL, model));
		dispatcher.addDispatch(new BlockModelSnowyFenceThin<>(MoreSnowBlocks.ASHY_FENCE_WALLPAPER, model));

		for (Block<? extends BlockLogicSnowy<?>> block : SNOWY_FENCE_THIN) {
			BlockModel<?> nmodel = BlockModelDispatcher.getInstance().getDispatch(getLayerBlock(block));
			dispatcher.addDispatch(new BlockModelSnowyFenceThin<>(block, nmodel));
		}
	}

	private static void assignModelFenceGate(BlockModelDispatcher dispatcher) {
		for (Block<? extends BlockLogicSnowy<?>> block : SNOWY_FENCE_GATE) {
			BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(getLayerBlock(block));
			dispatcher.addDispatch(new BlockModelSnowyFenceGate<>(block, model));
		}

		for (Block<BlockLogicSnowyFenceGatePainted<?>> block : SNOWY_FENCE_GATES_PAINTED) {
			BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(getLayerBlock(block));
			dispatcher.addDispatch(new BlockModelSnowyFenceGatePainted<>(block, model));
		}

	}
}
