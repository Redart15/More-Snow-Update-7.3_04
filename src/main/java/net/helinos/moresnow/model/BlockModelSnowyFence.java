package net.helinos.moresnow.model;

import net.helinos.moresnow.block.logic.BlockLogicSnowyFence;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import static net.helinos.moresnow.model.MoreSnowModels.zFactor;

public class BlockModelSnowyFence<T extends BlockLogic> extends BlockModelSnowy<T> {
	public BlockModelSnowyFence(Block<T> block, BlockModel<?> layerModel, String texID) {
		super(block, layerModel, texID);
	}

	@Override
	public boolean render(Tessellator tessellator, int x, int y, int z) {
		BlockLogicSnowyFence<?, ?> logic = (BlockLogicSnowyFence<?, ?>) this.block.getLogic();
		int metadata = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
		boolean somethingRendered = false;
		somethingRendered |= this.renderSnowLayers(tessellator, x, y, z, logic);
		int layers = logic.getLayers(metadata);
		double height = layers * 2 / 16.0;
		AABB bounds = AABB.getTemporaryBB(zFactor, 0.0, zFactor, 1.0f - zFactor, height - zFactor, 1.0f - zFactor);
		somethingRendered |= this.layerModel.renderStandardBlock(tessellator, bounds, x, y, z);
		return somethingRendered;
	}

	private boolean renderSnowLayers(Tessellator tessellator, int x, int y, int z, BlockLogicSnowyFence<?, ?> logic) {
		boolean somethingRendered = false;
		// Center post
		AABB bounds = AABB.getTemporaryBB(0.375, 0.0, 0.375, 0.625, 1.0, 0.625);
		this.renderStandardBlock(tessellator, bounds, x, y, z);
		boolean connectEast = logic.canConnectTo(renderBlocks.blockAccess, x - 1, y, z);
		boolean connectWest = logic.canConnectTo(renderBlocks.blockAccess, x + 1, y, z);
		boolean connectNorth = logic.canConnectTo(renderBlocks.blockAccess, x, y, z - 1);
		boolean connectSouth = logic.canConnectTo(renderBlocks.blockAccess, x, y, z + 1);
		boolean renderEastWest = connectEast || connectWest;
		boolean renderNorthSouth = connectNorth || connectSouth;

		float east = connectEast ? 0.0F : 0.4375F;
		float west = connectWest ? 1.0F : 0.5625F;
		float north = connectNorth ? 0.0F : 0.4375F;
		float south = connectSouth ? 1.0F : 0.5625F;

		// Upper connecting posts
		if (renderEastWest) {
			bounds.set(east, 0.75, 0.4375, west, 0.9375, 0.5625);
			somethingRendered |= this.renderStandardBlock(tessellator, bounds, x, y, z);
		}

		if (renderNorthSouth) {
			bounds.set(0.4375, 0.75, north, 0.5625, 0.9375, south);
			somethingRendered |= this.renderStandardBlock(tessellator, bounds, x, y, z);
		}

		// Lower connecting posts
		if (renderEastWest) {
			bounds.set(east, 0.375, 0.4375, west, 0.5625, 0.5625);
			somethingRendered |= this.renderStandardBlock(tessellator, bounds, x, y, z);
		}

		if (renderNorthSouth) {
			bounds.set(0.4375, 0.375, north, 0.5625, 0.5625, south);
			somethingRendered |= this.renderStandardBlock(tessellator, bounds, x, y, z);
		}
		return somethingRendered;
	}

	@Override
	public void renderLayerOnInventory(Tessellator tessellator, int metadata, float brightness, float alpha, @Nullable Integer lightmapCoordinate) {
		GL11.glTranslatef(0.0F, -0.25F, 0.0F);
		this.layerModel.renderBlockOnInventory(tessellator, metadata, brightness, alpha, lightmapCoordinate);
	}
}
