package net.helinos.moresnow.model;

import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.helinos.moresnow.block.logic.BlockLogicSnowyFenceGate;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.block.Block;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

import static net.helinos.moresnow.model.MoreSnowModels.zFactor;

public class BlockModelSnowyFenceGate<T extends BlockLogicSnowy<?>> extends BlockModelSnowy<T> {

	public BlockModelSnowyFenceGate(Block<T> block, BlockModel<?> layerModel) {
		super(block, layerModel);
	}

	@Override
	public boolean render(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource, @NotNull TilePosc tilePos) {
		AABBdc bounds = this.block.getBounds();
		BlockLogicSnowyFenceGate<?> logic = (BlockLogicSnowyFenceGate<?>) this.block.getLogic();
		int metadata = worldSource.getBlockData(tilePos);
		boolean somethingRendered = false;
		somethingRendered |= this.renderFenceGate(tessellator, worldSource, tilePos, logic, metadata, new AABBd(bounds));
		int layers = logic.getLayers(metadata);
		double height = layers * 2 / 16.0;
		AABBd nextBound = new AABBd(-MoreSnowModels.zFactor, 0.0, -MoreSnowModels.zFactor, 1.0f + MoreSnowModels.zFactor, height + zFactor, 1.0f + MoreSnowModels.zFactor);
		somethingRendered |= renderBlocks.renderStandardBlock(tessellator, worldSource, this.layerModel, nextBound, tilePos);
		return somethingRendered;
	}

	private boolean renderFenceGate(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource, @NotNull TilePosc tilePos, BlockLogicSnowyFenceGate<?> logic, int metadata, AABBd bounds) {
		int direction = logic.getDirection(metadata);
		boolean isOpen = logic.isOpen(metadata);
		if (direction != 3 && direction != 1) {
			bounds.setMin(0.0, 0.3125, 0.4375).setMax(0.125, 1.0, 0.5625);
			renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
			bounds.setMin(0.875, 0.3125, 0.4375).setMax( 1.0, 1.0, 0.5625);
		} else {
			bounds.setMin(0.4375, 0.3125, 0.0).setMax(0.5625, 1.0, 0.125);
			renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
			bounds.setMin(0.4375, 0.3125, 0.875).setMax( 0.5625, 1.0, 1.0);
		}
		boolean somethingRendered = renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
		if (!isOpen) {
			somethingRendered |= this.renderClosedGate(tessellator, worldSource, tilePos, direction, bounds);
		} else {
			somethingRendered |= this.renderOpenGate(tessellator, worldSource, tilePos, direction, bounds);
		}
		return somethingRendered;
	}

	private boolean renderClosedGate(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource, @NotNull TilePosc tilePos, int direction, AABBd bounds) {
		if (direction != 3 && direction != 1) {
			bounds.setMin(0.375, 0.375, 0.4375).setMax( 0.5, 0.9375, 0.5625);
			renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
			bounds.setMin(0.5, 0.375, 0.4375).setMax( 0.625, 0.9375, 0.5625);
			renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
			bounds.setMin(0.625, 0.375, 0.4375).setMax( 0.875, 0.5625, 0.5625);
			renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
			bounds.setMin(0.625, 0.75, 0.4375).setMax( 0.875, 0.9375, 0.5625);
			renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
			bounds.setMin(0.125, 0.375, 0.4375).setMax( 0.375, 0.5625, 0.5625);
			renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
			bounds.setMin(0.125, 0.75, 0.4375).setMax( 0.375, 0.9375, 0.5625);
			return renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
		} else {
			bounds.setMin(0.4375, 0.375, 0.375).setMax( 0.5625, 0.9375, 0.5);
			renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
			bounds.setMin(0.4375, 0.375, 0.5).setMax( 0.5625, 0.9375, 0.625);
			renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
			bounds.setMin(0.4375, 0.375, 0.625).setMax( 0.5625, 0.5625, 0.875);
			renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
			bounds.setMin(0.4375, 0.75, 0.625).setMax( 0.5625, 0.9375, 0.875);
			renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
			bounds.setMin(0.4375, 0.375, 0.125).setMax( 0.5625, 0.5625, 0.375);
			renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
			bounds.setMin(0.4375, 0.75, 0.125).setMax( 0.5625, 0.9375, 0.375);
			return renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
		}
	}

	private boolean renderOpenGate(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource, @NotNull TilePosc tilePos, int direction, AABBd bounds) {
		switch (direction) {
			case 0:
				bounds.setMin(0.0, 0.375, 0.8125).setMax( 0.125, 0.9375, 0.9375);
				renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
				bounds.setMin(0.875, 0.375, 0.8125).setMax( 1.0, 0.9375, 0.9375);
				renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
				bounds.setMin(0.0, 0.375, 0.5625).setMax( 0.125, 0.5625, 0.8125);
				renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
				bounds.setMin(0.875, 0.375, 0.5625).setMax( 1.0, 0.5625, 0.8125);
				renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
				bounds.setMin(0.0, 0.75, 0.5625).setMax( 0.125, 0.9375, 0.8125);
				renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
				bounds.setMin(0.875, 0.75, 0.5625).setMax( 1.0, 0.9375, 0.8125);
				return renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
			case 1:
				bounds.setMin(0.0625, 0.375, 0.0).setMax( 0.1875, 0.9375, 0.125);
				renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
				bounds.setMin(0.0625, 0.375, 0.875).setMax( 0.1875, 0.9375, 1.0);
				renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
				bounds.setMin(0.1875, 0.375, 0.0).setMax( 0.4375, 0.5625, 0.125);
				renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
				bounds.setMin(0.1875, 0.375, 0.875).setMax( 0.4375, 0.5625, 1.0);
				renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
				bounds.setMin(0.1875, 0.75, 0.0).setMax( 0.4375, 0.9375, 0.125);
				renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
				bounds.setMin(0.1875, 0.75, 0.875).setMax( 0.4375, 0.9375, 1.0);
				return renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
			case 2:
				bounds.setMin(0.0, 0.375, 0.0625).setMax( 0.125, 0.9375, 0.1875);
				renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
				bounds.setMin(0.875, 0.375, 0.0625).setMax( 1.0, 0.9375, 0.1875);
				renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
				bounds.setMin(0.0, 0.375, 0.1875).setMax( 0.125, 0.5625, 0.4375);
				renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
				bounds.setMin(0.875, 0.375, 0.1875).setMax( 1.0, 0.5625, 0.4375);
				renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
				bounds.setMin(0.0, 0.75, 0.1875).setMax( 0.125, 0.9375, 0.4375);
				renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
				bounds.setMin(0.875, 0.75, 0.1875).setMax( 1.0, 0.9375, 0.4375);
				return renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
			case 3:
			default:
				bounds.setMin(0.8125, 0.375, 0.0).setMax( 0.9375, 0.9375, 0.125);
				renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
				bounds.setMin(0.8125, 0.375, 0.875).setMax( 0.9375, 0.9375, 1.0);
				renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
				bounds.setMin(0.5625, 0.375, 0.0).setMax( 0.8125, 0.5625, 0.125);
				renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
				bounds.setMin(0.5625, 0.375, 0.875).setMax( 0.8125, 0.5625, 1.0);
				renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
				bounds.setMin(0.5625, 0.75, 0.0).setMax( 0.8125, 0.9375, 0.125);
				renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
				bounds.setMin(0.5625, 0.75, 0.875).setMax( 0.8125, 0.9375, 1.0);
				return renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
		}
	}

	@Override
	public void renderLayerOnInventory(@NotNull TessellatorGeneral tessellator, int metadata, byte lightIndex) {
		tessellator.offsetTranslation(0.0F, -0.25F, 0.0f);
		this.layerModel.renderStandalone(tessellator, metadata, lightIndex);
		tessellator.offsetTranslation(0.0F, 0.25F, 0.0f);
	}
}
