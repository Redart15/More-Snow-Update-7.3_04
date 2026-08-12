package net.helinos.moresnow.model;

import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.block.Block;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.primitives.AABBd;

public class BlockModelSnowyPlant<T extends BlockLogicSnowy<?>> extends BlockModelSnowy<T> {
	public BlockModelSnowyPlant(Block<T> block, BlockModel<?> layerModel) {
		super(block, layerModel);
	}

	@Override
	public boolean render(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource, @NotNull TilePosc tilePos) {
		int metadata = worldSource.getBlockData(tilePos);
		// Render the slab
		AABBd bounds = new AABBd(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
		Block<?> storedBlock = this.block.getLogic().storedBlock();
		BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(storedBlock);
		boolean somethingRendered = model.render(tessellator, worldSource, tilePos);
		// Render the snow
		int layers = block.getLogic().getLayers(metadata);
		double height = layers * 2 / 16.0;
		bounds.setMin(0.0, 0.0, 0.0).setMax(1.0, height, 1.0);
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
