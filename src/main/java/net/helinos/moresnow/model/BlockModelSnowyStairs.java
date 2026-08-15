package net.helinos.moresnow.model;

import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.helinos.moresnow.block.interfaces.IBlockLogicSnowyRotation;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicLayerBase;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.primitives.AABBd;

public class BlockModelSnowyStairs<T extends BlockLogicSnowy<?>> extends BlockModelSnowy<T> {
	public BlockModelSnowyStairs(Block<T> block, BlockModel<?> layerModel) {
		super(block, layerModel);
	}

	@Override
	public boolean render(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource, @NotNull TilePosc tilePos) {
		int metadata = worldSource.getBlockData(tilePos);

		// Render the stairs
		boolean somethingRendered = false;
		BlockLogicSnowy<?> logic = this.block.getLogic();
		int horizontalRotation = ((IBlockLogicSnowyRotation) logic).getRotation(metadata);

		AABBd bounds = new AABBd(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
		if (horizontalRotation == 0) {
			bounds.setMin(0.0, 0.0, 0.0).setMax(0.5, 0.5, 1.0);
			somethingRendered |= renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
			bounds.setMin(0.5, 0.0, 0.0).setMax( 1.0, 1.0, 1.0);
			somethingRendered |= renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
		} else if (horizontalRotation == 1) {
			bounds.setMin(0.0, 0.0, 0.0).setMax( 0.5, 1.0, 1.0);
			somethingRendered |= renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
			bounds.setMin(0.5, 0.0, 0.0).setMax( 1.0, 0.5, 1.0);
			somethingRendered |= renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
		} else if (horizontalRotation == 2) {
			bounds.setMin(0.0, 0.0, 0.0).setMax( 1.0, 0.5, 0.5);
			somethingRendered |= renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
			bounds.setMin(0.0, 0.0, 0.5).setMax( 1.0, 1.0, 1.0);
			somethingRendered |= renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
		} else {
			bounds.setMin(0.0, 0.0, 0.0).setMax( 1.0, 1.0, 0.5);
			somethingRendered |= renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
			bounds.setMin(0.0, 0.0, 0.5).setMax( 1.0, 0.5, 1.0);
			somethingRendered |= renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
		}

		// Render the snow
		int layers = logic.getLayers(metadata);
		double heightFromSnow = layers * 2 / 16.0;

		// Render the snow
		int x = tilePos.x();
		int y = tilePos.y();
		int z = tilePos.z();
		if (horizontalRotation == 0) {
			bounds.setMin(0.0, 0.5, 0.0).setMax( 0.5, 0.5 + heightFromSnow, 1.0);
			somethingRendered |= renderBlocks.renderStandardBlock(tessellator, worldSource, this.layerModel, bounds, tilePos);
			bounds.setMin(0.5, 0.0, 0.0).setMax( 1.0, heightFromSnow, 1.0);
			somethingRendered |= renderSnowLayerAboveBlock(tessellator, worldSource, bounds, new TilePos(x, y + 1, z));
		} else if (horizontalRotation == 1) {
			bounds.setMin(0.5, 0.5, 0.0).setMax( 1.0, 0.5 + heightFromSnow, 1.0);
			somethingRendered |= renderBlocks.renderStandardBlock(tessellator, worldSource, this.layerModel, bounds, tilePos);
			bounds.setMin(0.0, 0.0, 0.0).setMax( 0.5, heightFromSnow, 1.0);
			somethingRendered |= renderSnowLayerAboveBlock(tessellator, worldSource, bounds, new TilePos(x, y + 1, z));
		} else if (horizontalRotation == 2) {
			bounds.setMin(0.0, 0.5, 0.0).setMax( 1.0, 0.5 + heightFromSnow, 0.5);
			somethingRendered |= renderBlocks.renderStandardBlock(tessellator, worldSource, this.layerModel, bounds, tilePos);
			bounds.setMin(0.0, 0.0, 0.5).setMax( 1.0, heightFromSnow, 1.0);
			somethingRendered |= renderSnowLayerAboveBlock(tessellator, worldSource, bounds, new TilePos(x, y + 1, z));
		} else {
			bounds.setMin(0.0, 0.5, 0.5).setMax( 1.0, 0.5 + heightFromSnow, 1.0);
			somethingRendered |= renderBlocks.renderStandardBlock(tessellator, worldSource, this.layerModel, bounds, tilePos);
			bounds.setMin(0.0, 0.0, 0.0).setMax(1.0, heightFromSnow, 0.5);
			somethingRendered |= renderSnowLayerAboveBlock(tessellator, worldSource, bounds, new TilePos(x, y + 1, z));
		}
		return somethingRendered;
	}

	private boolean renderSnowLayerAboveBlock(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource, AABBd bounds, @NotNull TilePosc tilePos) {
		Block<?> block = worldSource.getBlockType(tilePos);
		if(block.id() == Blocks.AIR.id()){
			return renderBlocks.renderStandardBlock(tessellator, worldSource, this.layerModel, bounds, tilePos);
		}
		BlockLogic logic = block.getLogic();
		if(logic instanceof BlockLogicLayerBase){
			int layer = worldSource.getBlockData(tilePos) & 7;
			if(layer + 1 < 4) {
				bounds.minY = (2 * (1 + layer)) / 16.0F;
				return renderBlocks.renderStandardBlock(tessellator, worldSource, this.layerModel, bounds, tilePos);
			}
		}
		return false;
	}

	@Override
	public void renderStandalone(@NotNull TessellatorGeneral tessellator, int metadata, byte lightIndex) {
		Block<?> storedBlock = this.block.getLogic().storedBlock();
		BlockModel<?> storedBlockModel = BlockModelDispatcher.getInstance().getDispatch(storedBlock);
		storedBlockModel.renderStandalone(tessellator, metadata, lightIndex);
		int color = (BlockColorDispatcher.getInstance().getDispatch(this.block.getLogic().layerBlock())).getFallbackColor(metadata, 0);
//		tessellator.setColor2i(color, 255);
		boolean invTint = renderBlocks.useInventoryTint;
		renderBlocks.useInventoryTint = true;
		float yOffset = 0.5F;
		AABBd bounds = new AABBd(0.0, 0.5, 0.5, 1.0, 0.5 + 2 / 16.0, 1.0);
		tessellator.offsetTranslation(-0.5F, 0.0F - yOffset, -0.5F);
		this.layerModel.renderBlockWithBounds(tessellator, bounds, metadata, lightIndex, color);
		renderBlocks.useInventoryTint = invTint;
		tessellator.offsetTranslation(0.5F, yOffset, 0.5F);
	}
}
