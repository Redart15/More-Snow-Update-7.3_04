package net.helinos.moresnow.model;

import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.phys.AABB;

public class BlockModelSnowyPlant<T extends BlockLogic> extends BlockModelSnowy<T> {
	public BlockModelSnowyPlant(Block<T> block) {
		super(block);
	}

	@Override
	public boolean render(Tessellator tessellator, int x, int y, int z) {
		int metadata = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
		// Render the slab
		AABB bounds = AABB.getTemporaryBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
		Block<?> storedBlock = ((BlockLogicSnowy<?>) this.block.getLogic()).getStoredBlock();
		BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(storedBlock);
		boolean somethingRendered = model.render(tessellator, x, y, z);
		// Render the snow
		int layers = ((BlockLogicSnowy<?>) block.getLogic()).getLayers(metadata);
		double height = layers * 2 / 16.0;
		bounds.set(0.0, 0.0, 0.0, 1.0, height, 1.0);
		somethingRendered |= this.model.renderStandardBlock(tessellator, bounds, x, y, z);
		return somethingRendered;

	}
}
