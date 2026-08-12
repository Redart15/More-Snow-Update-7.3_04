package net.helinos.moresnow.model;

import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.helinos.moresnow.block.logic.BlockLogicSnowyFenceThin;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicFenceThin;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.primitives.AABBd;

import static net.helinos.moresnow.model.MoreSnowModels.zFactor;

public class BlockModelSnowyFenceThin<T extends BlockLogicSnowy<?>, F extends BlockLogicFenceThin> extends BlockModelSnowy<T> {

	public BlockModelSnowyFenceThin(Block<T> block, BlockModel<?> layerModel) {
		super(block, layerModel);
	}

	@Override
	public boolean render(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource, @NotNull TilePosc tilePos) {
		BlockLogicSnowyFenceThin<?, ?> logic = (BlockLogicSnowyFenceThin<?, ?>) this.block.getLogic();
		int metadata = worldSource.getBlockData(tilePos);
		boolean somethingRendered = false;
		somethingRendered |= BlockModelDispatcher.getInstance().getDispatch(logic.storedBlock()).render(tessellator, worldSource, tilePos);
		// Render snow
		int layers = logic.getLayers(metadata);
		double height = layers * 2 / 16.0;
		AABBd bounds = new AABBd(-zFactor, 0.0, -zFactor, 1.0f + zFactor, height + zFactor, 1.0f + zFactor);
		somethingRendered |= renderBlocks.renderStandardBlock(tessellator, worldSource, this.layerModel, bounds, tilePos);
		return somethingRendered;
	}

	@Override
	public void renderLayerOnInventory(@NotNull TessellatorGeneral tessellator, int metadata, byte lightIndex) {
		tessellator.offsetTranslation(0.0F, -0.25F, 0.0F);
		this.layerModel.renderStandalone(tessellator, metadata, lightIndex);
		tessellator.offsetTranslation(0.0F, 0.25F, 0.0F);
	}

}
