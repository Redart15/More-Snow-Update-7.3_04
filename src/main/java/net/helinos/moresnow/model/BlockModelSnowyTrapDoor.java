package net.helinos.moresnow.model;

import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicTrapDoor;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;

import static net.helinos.moresnow.model.MoreSnowModels.zFactor;

public class BlockModelSnowyTrapDoor<T extends BlockLogic> extends BlockModelSnowy<T> {
	public BlockModelSnowyTrapDoor(Block<T> block, BlockModel<?> layerModel, String texID) {
		super(block, layerModel, texID);
	}

	@Override
	public boolean render(Tessellator tessellator, int x, int y, int z) {
		BlockLogicSnowy<?> logic = (BlockLogicSnowy<?>) this.block.getLogic();
		int metadata = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
		boolean isUp = BlockLogicTrapDoor.isTrapdoorOpen(metadata >> 4);
		boolean somethingRendered = false;
		int layers = logic.getRelativeLayers(metadata);
		int relativeMaxLayer = logic.getRelativeMaxLayer(metadata);
		if(isUp){
			if(layers < relativeMaxLayer){
				somethingRendered |= this.renderTrapDoor(tessellator, x, y, z);
			}
			double height = layers * 2 / 16.0;
			AABB bounds = AABB.getTemporaryBB(-zFactor, 0.0 , -zFactor, 1.0 - zFactor, height, 1.0 - zFactor);
			somethingRendered |= this.layerModel.renderStandardBlock(tessellator, bounds, x, y, z);
		}else{
			if(layers < relativeMaxLayer - 1){
				somethingRendered |= this.renderTrapDoor(tessellator, x, y, z);
			}
			double height = (layers + 1) * 2 / 16.0;
			AABB bounds = AABB.getTemporaryBB(-zFactor, 0.1875F , -zFactor, 1.0 - zFactor, height, 1.0 - zFactor);
			somethingRendered |= this.layerModel.renderStandardBlock(tessellator, bounds, x, y, z);
		}
		return somethingRendered;
	}

	public boolean renderTrapDoor(Tessellator tessellator, int x, int y, int z) {
		BlockLogicSnowy<?> logic = (BlockLogicSnowy<?>) this.block.getLogic();
		int metadata = renderBlocks.blockAccess.getBlockMetadata(x, y, z) >> 4;
		boolean isUp = BlockLogicTrapDoor.isTrapdoorOpen(metadata);
		if (isUp) {
			switch (metadata & 3) {
				case 0:
					renderBlocks.uvRotateNorth = 2;
					renderBlocks.uvRotateSouth = 1;
					break;
				case 1:
					renderBlocks.uvRotateTop = 3;
					renderBlocks.uvRotateBottom = 3;
					renderBlocks.uvRotateNorth = 1;
					renderBlocks.uvRotateSouth = 2;
					break;
				case 2:
					renderBlocks.uvRotateTop = 2;
					renderBlocks.uvRotateBottom = 1;
					renderBlocks.uvRotateEast = 1;
					renderBlocks.uvRotateWest = 2;
					break;
				case 3:
					renderBlocks.uvRotateTop = 1;
					renderBlocks.uvRotateBottom = 2;
					renderBlocks.uvRotateEast = 2;
					renderBlocks.uvRotateWest = 1;
			}
		} else if (BlockLogicTrapDoor.isUpperHalf(metadata)) {
			renderBlocks.uvRotateSouth = 3;
			renderBlocks.uvRotateNorth = 3;
			renderBlocks.uvRotateEast = 3;
			renderBlocks.uvRotateWest = 3;
		}
		BlockModel<?> storedBlockModel = BlockModelDispatcher.getInstance().getDispatch(logic.storedBlock);
		this.adjustRendering(tessellator, storedBlockModel, logic.storedBlock.getBlockBoundsFromState(renderBlocks.blockAccess, x, y, z), x, y, z);
//		storedBlockModel.renderStandardBlock(tessellator, this.block.getBlockBoundsFromState(renderBlocks.blockAccess, x, y, z), x, y, z);
		storedBlockModel.resetRenderBlocks();
		return true;
	}

	public boolean adjustRendering(Tessellator tessellator, BlockModel<?> blockModel, AABB bounds, int x, int y, int z){
		int color = BlockColorDispatcher.getInstance().getDispatch(blockModel.block).getWorldColor(renderBlocks.blockAccess, x, y, z);
		float r = (color >> 16 & 255) / 255.0F;
		float g = (color >> 8 & 255) / 255.0F;
		float b = (color & 255) / 255.0F;
		renderBlocks.enableAO = true;
		int meta = renderBlocks.blockAccess.getBlockMetadata(x, y, z) >> 4;
		renderBlocks.cache.setupCache(blockModel.block, renderBlocks.blockAccess, x, y, z);
		boolean somethingRendered = false;

		for(Side side : Side.sides) {
			somethingRendered |= renderBlocks.renderSide(tessellator, blockModel, bounds, x, y, z, r, g, b, side, meta);
		}

		renderBlocks.enableAO = false;
		return somethingRendered;
	}
}
