package net.helinos.moresnow.model;

import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.helinos.moresnow.block.logic.BlockLogicSnowyFenceGate;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import static net.helinos.moresnow.model.MoreSnowModels.zFactor;

public class BlockModelSnowyFenceGate<T extends BlockLogic> extends BlockModelSnowy<T> {

	public BlockModelSnowyFenceGate(Block<T> block, BlockModel<?> layerModel, String texID) {
		super(block, layerModel, texID);
	}

	@Override
	public boolean render(Tessellator tessellator, int x, int y, int z) {
		AABB bounds = this.block.getBounds();
		BlockLogicSnowyFenceGate<?> logic = (BlockLogicSnowyFenceGate<?>) this.block.getLogic();
		int metadata = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
		boolean somethingRendered = false;
		somethingRendered |= this.renderFenceGate(tessellator, x, y, z, logic, metadata, bounds);
		this.renderingSnow = true;
		int layers = logic.getLayers(metadata);
		double height = layers * 2 / 16.0;
		bounds = AABB.getTemporaryBB(MoreSnowModels.zFactor, 0.0, MoreSnowModels.zFactor, 1.0f - MoreSnowModels.zFactor, height - zFactor, 1.0f - MoreSnowModels.zFactor);
		somethingRendered |= this.layerModel.renderStandardBlock(tessellator, bounds, x, y, z);
		this.renderingSnow = false;
		return somethingRendered;
	}

	private boolean renderFenceGate(Tessellator tessellator, int x, int y, int z, BlockLogicSnowyFenceGate<?> logic, int metadata, AABB bounds) {
		int direction = logic.getDirection(metadata);
		boolean isOpen = logic.isOpen(metadata);
		if (direction != 3 && direction != 1) {
			bounds.set(0.0, 0.3125, 0.4375, 0.125, 1.0, 0.5625);
			this.renderStandardBlock(tessellator, bounds, x, y, z);
			bounds.set(0.875, 0.3125, 0.4375, 1.0, 1.0, 0.5625);
		} else {
			bounds.set(0.4375, 0.3125, 0.0, 0.5625, 1.0, 0.125);
			this.renderStandardBlock(tessellator, bounds, x, y, z);
			bounds.set(0.4375, 0.3125, 0.875, 0.5625, 1.0, 1.0);
		}
		boolean somethingRendered = this.renderStandardBlock(tessellator, bounds, x, y, z);
		if (!isOpen) {
			somethingRendered |= this.renderClosedGate(tessellator, x, y, z, direction, bounds);
		} else {
			somethingRendered |= this.renderOpenGate(tessellator, x, y, z, direction, bounds);
		}
		return somethingRendered;
	}

	private boolean renderClosedGate(Tessellator tessellator, int x, int y, int z, int direction, AABB bounds) {
		if (direction != 3 && direction != 1) {
			bounds.set(0.375, 0.375, 0.4375, 0.5, 0.9375, 0.5625);
			this.renderStandardBlock(tessellator, bounds, x, y, z);
			bounds.set(0.5, 0.375, 0.4375, 0.625, 0.9375, 0.5625);
			this.renderStandardBlock(tessellator, bounds, x, y, z);
			bounds.set(0.625, 0.375, 0.4375, 0.875, 0.5625, 0.5625);
			this.renderStandardBlock(tessellator, bounds, x, y, z);
			bounds.set(0.625, 0.75, 0.4375, 0.875, 0.9375, 0.5625);
			this.renderStandardBlock(tessellator, bounds, x, y, z);
			bounds.set(0.125, 0.375, 0.4375, 0.375, 0.5625, 0.5625);
			this.renderStandardBlock(tessellator, bounds, x, y, z);
			bounds.set(0.125, 0.75, 0.4375, 0.375, 0.9375, 0.5625);
			return this.renderStandardBlock(tessellator, bounds, x, y, z);
		} else {
			bounds.set(0.4375, 0.375, 0.375, 0.5625, 0.9375, 0.5);
			this.renderStandardBlock(tessellator, bounds, x, y, z);
			bounds.set(0.4375, 0.375, 0.5, 0.5625, 0.9375, 0.625);
			this.renderStandardBlock(tessellator, bounds, x, y, z);
			bounds.set(0.4375, 0.375, 0.625, 0.5625, 0.5625, 0.875);
			this.renderStandardBlock(tessellator, bounds, x, y, z);
			bounds.set(0.4375, 0.75, 0.625, 0.5625, 0.9375, 0.875);
			this.renderStandardBlock(tessellator, bounds, x, y, z);
			bounds.set(0.4375, 0.375, 0.125, 0.5625, 0.5625, 0.375);
			this.renderStandardBlock(tessellator, bounds, x, y, z);
			bounds.set(0.4375, 0.75, 0.125, 0.5625, 0.9375, 0.375);
			return this.renderStandardBlock(tessellator, bounds, x, y, z);
		}
	}

	private boolean renderOpenGate(Tessellator tessellator, int x, int y, int z, int direction, AABB bounds) {
		switch (direction) {
			case 0:
				bounds.set(0.0, 0.375, 0.8125, 0.125, 0.9375, 0.9375);
				this.renderStandardBlock(tessellator, bounds, x, y, z);
				bounds.set(0.875, 0.375, 0.8125, 1.0, 0.9375, 0.9375);
				this.renderStandardBlock(tessellator, bounds, x, y, z);
				bounds.set(0.0, 0.375, 0.5625, 0.125, 0.5625, 0.8125);
				this.renderStandardBlock(tessellator, bounds, x, y, z);
				bounds.set(0.875, 0.375, 0.5625, 1.0, 0.5625, 0.8125);
				this.renderStandardBlock(tessellator, bounds, x, y, z);
				bounds.set(0.0, 0.75, 0.5625, 0.125, 0.9375, 0.8125);
				this.renderStandardBlock(tessellator, bounds, x, y, z);
				bounds.set(0.875, 0.75, 0.5625, 1.0, 0.9375, 0.8125);
				return this.renderStandardBlock(tessellator, bounds, x, y, z);
			case 1:
				bounds.set(0.0625, 0.375, 0.0, 0.1875, 0.9375, 0.125);
				this.renderStandardBlock(tessellator, bounds, x, y, z);
				bounds.set(0.0625, 0.375, 0.875, 0.1875, 0.9375, 1.0);
				this.renderStandardBlock(tessellator, bounds, x, y, z);
				bounds.set(0.1875, 0.375, 0.0, 0.4375, 0.5625, 0.125);
				this.renderStandardBlock(tessellator, bounds, x, y, z);
				bounds.set(0.1875, 0.375, 0.875, 0.4375, 0.5625, 1.0);
				this.renderStandardBlock(tessellator, bounds, x, y, z);
				bounds.set(0.1875, 0.75, 0.0, 0.4375, 0.9375, 0.125);
				this.renderStandardBlock(tessellator, bounds, x, y, z);
				bounds.set(0.1875, 0.75, 0.875, 0.4375, 0.9375, 1.0);
				return this.renderStandardBlock(tessellator, bounds, x, y, z);
			case 2:
				bounds.set(0.0, 0.375, 0.0625, 0.125, 0.9375, 0.1875);
				this.renderStandardBlock(tessellator, bounds, x, y, z);
				bounds.set(0.875, 0.375, 0.0625, 1.0, 0.9375, 0.1875);
				this.renderStandardBlock(tessellator, bounds, x, y, z);
				bounds.set(0.0, 0.375, 0.1875, 0.125, 0.5625, 0.4375);
				this.renderStandardBlock(tessellator, bounds, x, y, z);
				bounds.set(0.875, 0.375, 0.1875, 1.0, 0.5625, 0.4375);
				this.renderStandardBlock(tessellator, bounds, x, y, z);
				bounds.set(0.0, 0.75, 0.1875, 0.125, 0.9375, 0.4375);
				this.renderStandardBlock(tessellator, bounds, x, y, z);
				bounds.set(0.875, 0.75, 0.1875, 1.0, 0.9375, 0.4375);
				return this.renderStandardBlock(tessellator, bounds, x, y, z);
			case 3:
			default:
				bounds.set(0.8125, 0.375, 0.0, 0.9375, 0.9375, 0.125);
				this.renderStandardBlock(tessellator, bounds, x, y, z);
				bounds.set(0.8125, 0.375, 0.875, 0.9375, 0.9375, 1.0);
				this.renderStandardBlock(tessellator, bounds, x, y, z);
				bounds.set(0.5625, 0.375, 0.0, 0.8125, 0.5625, 0.125);
				this.renderStandardBlock(tessellator, bounds, x, y, z);
				bounds.set(0.5625, 0.375, 0.875, 0.8125, 0.5625, 1.0);
				this.renderStandardBlock(tessellator, bounds, x, y, z);
				bounds.set(0.5625, 0.75, 0.0, 0.8125, 0.9375, 0.125);
				this.renderStandardBlock(tessellator, bounds, x, y, z);
				bounds.set(0.5625, 0.75, 0.875, 0.8125, 0.9375, 1.0);
				return this.renderStandardBlock(tessellator, bounds, x, y, z);
		}
	}

	@Override
	public void renderLayerOnInventory(Tessellator tessellator, int metadata, float brightness, float alpha, @Nullable Integer lightmapCoordinate) {
		GL11.glTranslatef(0.0F, -0.25F, 0.0F);
		this.layerModel.renderBlockOnInventory(tessellator, metadata, brightness, alpha, lightmapCoordinate);
	}
}
