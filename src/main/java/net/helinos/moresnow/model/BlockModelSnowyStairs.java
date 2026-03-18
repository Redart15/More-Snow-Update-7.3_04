package net.helinos.moresnow.model;

import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.helinos.moresnow.block.interfaces.IBlockLogicSnowyRotation;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicLayerBase;
import net.minecraft.core.util.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

public class BlockModelSnowyStairs<T extends BlockLogic> extends BlockModelSnowy<T> {
	public BlockModelSnowyStairs(Block<T> block, BlockModel<?> layerModel, String texID) {
		super(block, layerModel, texID);
	}

	@Override
	public boolean render(Tessellator tessellator, int x, int y, int z) {
		int metadata = renderBlocks.blockAccess.getBlockMetadata(x, y, z);

		// Render the stairs
		boolean somethingRendered = false;
		BlockLogicSnowy<?> logic = (BlockLogicSnowy<?>) this.block.getLogic();
		int horizontalRotation = ((IBlockLogicSnowyRotation) logic).getRotation(metadata);

		AABB bounds = AABB.getTemporaryBB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
		if (horizontalRotation == 0) {
			bounds.set(0.0, 0.0, 0.0, 0.5, 0.5, 1.0);
			somethingRendered |= this.renderStandardBlock(tessellator, bounds, x, y, z);
			bounds.set(0.5, 0.0, 0.0, 1.0, 1.0, 1.0);
			somethingRendered |= this.renderStandardBlock(tessellator, bounds, x, y, z);
		} else if (horizontalRotation == 1) {
			bounds.set(0.0, 0.0, 0.0, 0.5, 1.0, 1.0);
			somethingRendered |= this.renderStandardBlock(tessellator, bounds, x, y, z);
			bounds.set(0.5, 0.0, 0.0, 1.0, 0.5, 1.0);
			somethingRendered |= this.renderStandardBlock(tessellator, bounds, x, y, z);
		} else if (horizontalRotation == 2) {
			bounds.set(0.0, 0.0, 0.0, 1.0, 0.5, 0.5);
			somethingRendered |= this.renderStandardBlock(tessellator, bounds, x, y, z);
			bounds.set(0.0, 0.0, 0.5, 1.0, 1.0, 1.0);
			somethingRendered |= this.renderStandardBlock(tessellator, bounds, x, y, z);
		} else {
			bounds.set(0.0, 0.0, 0.0, 1.0, 1.0, 0.5);
			somethingRendered |= this.renderStandardBlock(tessellator, bounds, x, y, z);
			bounds.set(0.0, 0.0, 0.5, 1.0, 0.5, 1.0);
			somethingRendered |= this.renderStandardBlock(tessellator, bounds, x, y, z);
		}

		// Render the snow
		int layers = logic.getLayers(metadata);
		double heightFromSnow = layers * 2 / 16.0;

		// Render the snow
		if (horizontalRotation == 0) {
			bounds.set(0.0, 0.5, 0.0, 0.5, 0.5 + heightFromSnow, 1.0);
			somethingRendered |= this.layerModel.renderStandardBlock(tessellator, bounds, x, y, z);
			somethingRendered |= renderSnowLayerAboveBlock(tessellator, bounds.set(0.5, 0.0, 0.0, 1.0, heightFromSnow, 1.0), x, y + 1, z);
		} else if (horizontalRotation == 1) {
			bounds.set(0.5, 0.5, 0.0, 1.0, 0.5 + heightFromSnow, 1.0);
			somethingRendered |= this.layerModel.renderStandardBlock(tessellator, bounds, x, y, z);
			somethingRendered |= renderSnowLayerAboveBlock(tessellator, bounds.set(0.0, 0.0, 0.0, 0.5, heightFromSnow, 1.0), x, y + 1, z);
		} else if (horizontalRotation == 2) {
			bounds.set(0.0, 0.5, 0.0, 1.0, 0.5 + heightFromSnow, 0.5);
			somethingRendered |= this.layerModel.renderStandardBlock(tessellator, bounds, x, y, z);
			somethingRendered |= renderSnowLayerAboveBlock(tessellator, bounds.set(0.0, 0.0, 0.5, 1.0, heightFromSnow, 1.0), x, y + 1, z);
		} else {
			bounds.set(0.0, 0.5, 0.5, 1.0, 0.5 + heightFromSnow, 1.0);
			somethingRendered |= this.layerModel.renderStandardBlock(tessellator, bounds, x, y, z);
			somethingRendered |= renderSnowLayerAboveBlock(tessellator, bounds.set(0.0, 0.0, 0.0, 1.0, heightFromSnow, 0.5), x, y + 1, z);
		}
		return somethingRendered;
	}

	private boolean renderSnowLayerAboveBlock(Tessellator tessellator, AABB bounds, int x, int y, int z) {
		Block<?> block = renderBlocks.blockAccess.getBlock(x, y, z);
		if(block == null){
			return this.layerModel.renderStandardBlock(tessellator, bounds, x, y, z);
		}
		BlockLogic logic = block.getLogic();
		if(logic == null){
			return false;
		}
		if(logic instanceof BlockLogicLayerBase){
			int layer = renderBlocks.blockAccess.getBlockMetadata(x, y, z) & 7;
			bounds.minY = (2 * (1 + layer)) / 16.0F;
			return this.layerModel.renderStandardBlock(tessellator, bounds, x, y, z);
		}
		return false;
	}

	@Override
	public void renderLayerOnInventory(Tessellator tessellator, int metadata, float brightness, float alpha, @Nullable Integer lightmapCoordinate) {
		if (renderBlocks.useInventoryTint) {
			int color = (BlockColorDispatcher.getInstance().getDispatch(this.block)).getFallbackColor(metadata);
			float r = (color >> 16 & 255) / 255.0F;
			float g = (color >> 8 & 255) / 255.0F;
			float b = (color & 255) / 255.0F;
			GL11.glColor4f(r * brightness, g * brightness, b * brightness, alpha);
		} else {
			GL11.glColor4f(brightness, brightness, brightness, alpha);
		}
		float yOffset = 0.5F;
		AABB bounds = AABB.getTemporaryBB(0.0, 0.5, 0.5, 1.0, 0.5 + 2 / 16.0, 1.0);
		GL11.glTranslatef(-0.5F, 0.0F - yOffset, -0.5F);
		((BlockModelStandard<BlockLogic>)this.layerModel).renderBlockWithBounds(tessellator, bounds, metadata, brightness, alpha, lightmapCoordinate);
		GL11.glTranslatef(0.5F, yOffset, 0.5F);
	}
}
