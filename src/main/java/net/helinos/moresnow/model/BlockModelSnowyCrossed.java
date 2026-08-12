package net.helinos.moresnow.model;

import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.primitives.AABBd;

@SuppressWarnings("java:S1905")
public class BlockModelSnowyCrossed<T extends BlockLogicSnowy<?>> extends BlockModelSnowy<T> {

	protected BlockModelSnowyCrossed(Block<T> block, BlockModel<?> layerModel) {
		super(block, layerModel);
	}

	@Override
	public void renderStandalone(@NotNull TessellatorGeneral tessellator, int metadata, byte lightIndex) {
		if(block.getLogic().layerBlock().id() == Blocks.LAYER_SNOW.id()){
			this.renderCrossInventory(tessellator, metadata, lightIndex);
			this.renderLayerOnInventory(tessellator, metadata, lightIndex);
		}else{
			super.renderStandalone(tessellator, metadata, lightIndex);
		}
	}

	@Override
	public boolean render(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource, @NotNull TilePosc tilePos) {
		int metadata = worldSource.getBlockData(tilePos);
		// Render the slab
		AABBd bounds = new AABBd(0.0, 0.0, 0.0, 1.0, 0.0, 1.0);
		boolean somethingRendered = false;
		if(block.getLogic().layerBlock().id() == Blocks.LAYER_SNOW.id()){
			somethingRendered |= this.renderCrossShaped(tessellator, worldSource, tilePos);
		}else{
			Block<?> storedBlock = this.block.getLogic().storedBlock();
			BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(storedBlock);
			somethingRendered |= model.render(tessellator, worldSource, tilePos);
		}
		// Render the snow
		int layers = block.getLogic().getLayers(metadata);
		double height = layers * 2 / 16.0;
		bounds.setMin(0.0, 0.0, 0.0).setMax(1.0, height, 1.0);
		somethingRendered |= renderBlocks.renderStandardBlock(tessellator, worldSource, this.layerModel, bounds, tilePos);
		return somethingRendered;
	}

	private void renderCrossInventory(@NotNull TessellatorGeneral tessellator, int metadata, byte lightIndex) {
		IconCoordinate texIndex = this.getBlockTextureFromSideAndMetadata(Side.BOTTOM, metadata);
		if (renderBlocks.overrideBlockTexture != null) {
			texIndex = renderBlocks.overrideBlockTexture;
		}

		double minU = texIndex.getIconUMin();
		double maxU = texIndex.getIconUMax();
		double minV = texIndex.getIconVMin();
		double maxV = texIndex.getIconVMax();
		tessellator.offsetTranslation(-0.5F, -0.5F, -0.5F);
		tessellator.startDrawingQuads();
		tessellator.setColor1i(this.getStandaloneTintColor(metadata));
		tessellator.setLightmapCoord1i(lightIndex);
		double xd = 0.0F;
		double yd = 0.0F;
		double zd = 0.0F;
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
		tessellator.offsetTranslation(0.5F, 0.5F, 0.5F);
	}

	private boolean renderCrossShaped(@NotNull TessellatorGeneral tessellator,@NotNull WorldSource worldSource,@NotNull TilePosc tilePosc) {
		tessellator.setLightmapCoord1i(this.block.getLightIndex(worldSource, tilePosc));
		int color = BlockColorDispatcher.getInstance().getDispatch(this.block).getWorldColor(worldSource, tilePosc, 0);
		float r = (float)(color >> 16 & 255) / 255.0F;
		float g = (float)(color >> 8 & 255) / 255.0F;
		float b = (float)(color & 255) / 255.0F;
		int metadata = worldSource.getBlockData(tilePosc);
		tessellator.setColorOpaque3f(r, g, b);
		int x = tilePosc.x();
		int y = tilePosc.y();
		int z = tilePosc.z();
		double xd = x;
		double yd = y;
		double zd = z;
		Block<?> stored = this.block.getLogic().storedBlock();
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
	public void renderLayerOnInventory(@NotNull TessellatorGeneral tessellator, int metadata, byte lightIndex) {
		tessellator.offsetTranslation(0.0F, -0.25F, 0.0F);
		this.layerModel.renderStandalone(tessellator, metadata, lightIndex);
		tessellator.offsetTranslation(0.0F, 0.25F, 0.0F);
	}
}
