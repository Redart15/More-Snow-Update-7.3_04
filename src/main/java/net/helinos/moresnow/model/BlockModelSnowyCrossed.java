package net.helinos.moresnow.model;

import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.minecraft.client.render.LightmapHelper;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@SuppressWarnings("java:S1905")
public class BlockModelSnowyCrossed<T extends BlockLogic> extends BlockModelSnowy<T> {

	protected BlockModelSnowyCrossed(Block<T> block, BlockModel<?> layerModel, String texID) {
		super(block, layerModel, texID);
	}

	@Override
	public void renderBlockOnInventory(Tessellator tessellator, int metadata, float brightness, float alpha, @Nullable Integer lightmapCoordinate) {
		if(((BlockLogicSnowy)block.getLogic()).layerBlock.id() == Blocks.LAYER_SNOW.id()){
			this.renderCrossInventory(tessellator, metadata, brightness, alpha, lightmapCoordinate);
			this.renderLayerOnInventory(tessellator, metadata, brightness, alpha, lightmapCoordinate);
		}else{
			super.renderBlockOnInventory(tessellator, metadata, brightness, alpha, lightmapCoordinate);
		}
	}

	@Override
	public boolean render(Tessellator tessellator, int x, int y, int z) {
		int metadata = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
		// Render the slab
		AABB bounds = AABB.getTemporaryBB(0.0, 0.0, 0.0, 1.0, 0.0, 1.0);
		boolean somethingRendered = false;
		if(((BlockLogicSnowy)block.getLogic()).layerBlock.id() == Blocks.LAYER_SNOW.id()){
			somethingRendered = this.renderCrossShaped(tessellator, x, y, z);
		}else{
			Block<?> storedBlock = ((BlockLogicSnowy<?>) this.block.getLogic()).getStoredBlock();
			BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(storedBlock);
			somethingRendered = model.render(tessellator, x, y, z);
		}
		// Render the snow
		int layers = ((BlockLogicSnowy<?>) block.getLogic()).getLayers(metadata);
		double height = layers * 2 / 16.0;
		bounds.set(0.0, 0.0, 0.0, 1.0, height, 1.0);
		somethingRendered |= this.layerModel.renderStandardBlock(tessellator, bounds, x, y, z);
		return somethingRendered;
	}

	private void renderCrossInventory(Tessellator tessellator, int metadata, float brightness, float alpha, @Nullable Integer lightmapCoordinate) {
		if (LightmapHelper.isLightmapEnabled()) {
			brightness = 1.0F;
		}

		float r = 1.0F;
		float g = 1.0F;
		float b = 1.0F;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float yOffset = 0.0F;
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		IconCoordinate texIndex = this.getBlockTextureFromSideAndMetadata(Side.BOTTOM, metadata);
		if (renderBlocks.overrideBlockTexture != null) {
			texIndex = renderBlocks.overrideBlockTexture;
		}

		double minU = texIndex.getIconUMin();
		double maxU = texIndex.getIconUMax();
		double minV = texIndex.getIconVMin();
		double maxV = texIndex.getIconVMax();
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(r * brightness, g * brightness, b * brightness, alpha);
		if (LightmapHelper.isLightmapEnabled() && lightmapCoordinate != null) {
			tessellator.setLightmapCoord(lightmapCoordinate);
		}

		double xd = 0.0F;
		double yd = 0.0F;
		double zd = 0.0F;
		double minX = xd + (double)0.5F - 0.45;
		double maxX = xd + (double)0.5F + 0.45;
		double minZ = zd + (double)0.5F - 0.45;
		double maxZ = zd + (double)0.5F + 0.45;
		tessellator.addVertexWithUV(minX, yd + 1.0F + (double)0.0F, minZ, minU, minV);
		tessellator.addVertexWithUV(minX, yd + (double)0.0F, minZ, minU, maxV);
		tessellator.addVertexWithUV(maxX, yd + (double)0.0F, maxZ, maxU, maxV);
		tessellator.addVertexWithUV(maxX, yd + (double)1.0F + (double)0.0F, maxZ, maxU, minV);
		tessellator.addVertexWithUV(maxX, yd + (double)1.0F + (double)0.0F, maxZ, minU, minV);
		tessellator.addVertexWithUV(maxX, yd + (double)0.0F, maxZ, minU, maxV);
		tessellator.addVertexWithUV(minX, yd + (double)0.0F, minZ, maxU, maxV);
		tessellator.addVertexWithUV(minX, yd + (double)1.0F + (double)0.0F, minZ, maxU, minV);
		tessellator.addVertexWithUV(minX, yd + (double)1.0F + (double)0.0F, maxZ, minU, minV);
		tessellator.addVertexWithUV(minX, yd + (double)0.0F, maxZ, minU, maxV);
		tessellator.addVertexWithUV(maxX, yd + (double)0.0F, minZ, maxU, maxV);
		tessellator.addVertexWithUV(maxX, yd + (double)1.0F + (double)0.0F, minZ, maxU, minV);
		tessellator.addVertexWithUV(maxX, yd + (double)1.0F + (double)0.0F, minZ, minU, minV);
		tessellator.addVertexWithUV(maxX, yd + (double)0.0F, minZ, minU, maxV);
		tessellator.addVertexWithUV(minX, yd + (double)0.0F, maxZ, maxU, maxV);
		tessellator.addVertexWithUV(minX, yd + (double)1.0F + (double)0.0F, maxZ, maxU, minV);
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	private boolean renderCrossShaped(Tessellator tessellator, int x, int y, int z) {
		float brightness = 1.0F;
		if (!LightmapHelper.isLightmapEnabled()) {
			brightness = this.getBlockBrightness(renderBlocks.blockAccess, x, y, z);
		} else {
			tessellator.setLightmapCoord(this.block.getLightmapCoord(renderBlocks.blockAccess, x, y, z));
		}
		int metadata = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
		float r = 1.0F;
		float g = 1.0F;
		float b = 1.0F;
		tessellator.setColorOpaque_F(brightness * r, brightness * g, brightness * b);
		double xd = (double) x;
		double yd = (double) y;
		double zd = (double) z;
		Block<?> stored = ((BlockLogicSnowy)this.block.getLogic()).storedBlock;
		if (stored == Blocks.TALLGRASS || stored == Blocks.TALLGRASS_FERN || stored == Blocks.SPINIFEX) {
			long dRandom = (long) x * 3129871L ^ (long) z * 116129781L ^ (long) y;
			dRandom = dRandom * dRandom * 42317861L + dRandom * 11L;
			xd += ((double)((float)(dRandom >> 16 & 15L) / 15.0F) - (double)0.5F) * (double)0.5F;
			yd += ((double)((float)(dRandom >> 20 & 15L) / 15.0F) - (double)1.0F) * 0.2;
			zd += ((double)((float)(dRandom >> 24 & 15L) / 15.0F) - (double)0.5F) * (double)0.5F;
		}

		IconCoordinate texIndex = this.getBlockTextureFromSideAndMetadata(Side.BOTTOM, metadata);
		if (renderBlocks.overrideBlockTexture != null) {
			texIndex = renderBlocks.overrideBlockTexture;
		}

		double minU = texIndex.getIconUMin();
		double maxU = texIndex.getIconUMax();
		double minV = texIndex.getIconVMin();
		double maxV = texIndex.getIconVMax();
		float yOffset = 0.0F;
		double minX = xd + (double)0.5F - 0.45;
		double maxX = xd + (double)0.5F + 0.45;
		double minZ = zd + (double)0.5F - 0.45;
		double maxZ = zd + (double)0.5F + 0.45;
		tessellator.addVertexWithUV(minX, yd + (double)1.0F + (double)0.0F, minZ, minU, minV);
		tessellator.addVertexWithUV(minX, yd + (double)0.0F, minZ, minU, maxV);
		tessellator.addVertexWithUV(maxX, yd + (double)0.0F, maxZ, maxU, maxV);
		tessellator.addVertexWithUV(maxX, yd + (double)1.0F + (double)0.0F, maxZ, maxU, minV);
		tessellator.addVertexWithUV(maxX, yd + (double)1.0F + (double)0.0F, maxZ, minU, minV);
		tessellator.addVertexWithUV(maxX, yd + (double)0.0F, maxZ, minU, maxV);
		tessellator.addVertexWithUV(minX, yd + (double)0.0F, minZ, maxU, maxV);
		tessellator.addVertexWithUV(minX, yd + 1.0F + 0.0F, minZ, maxU, minV);
		tessellator.addVertexWithUV(minX, yd + 1.0F + 0.0F, maxZ, minU, minV);
		tessellator.addVertexWithUV(minX, yd + 0.0F, maxZ, minU, maxV);
		tessellator.addVertexWithUV(maxX, yd + 0.0F, minZ, maxU, maxV);
		tessellator.addVertexWithUV(maxX, yd + 1.0F + 0.0F, minZ, maxU, minV);
		tessellator.addVertexWithUV(maxX, yd + 1.0F + 0.0F, minZ, minU, minV);
		tessellator.addVertexWithUV(maxX, yd + 0.0F, minZ, minU, maxV);
		tessellator.addVertexWithUV(minX, yd + 0.0F, maxZ, maxU, maxV);
		tessellator.addVertexWithUV(minX, yd + 1.0F + 0.0F, maxZ, maxU, minV);
		return true;
	}

	@Override
	public void renderLayerOnInventory(Tessellator tessellator, int metadata, float brightness, float alpha, @Nullable Integer lightmapCoordinate) {
		GL11.glTranslatef(0.0F, -0.25F, 0.0F);
		this.layerModel.renderBlockOnInventory(tessellator, metadata, brightness, alpha, lightmapCoordinate);
	}
}
