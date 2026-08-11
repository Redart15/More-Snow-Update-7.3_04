package net.helinos.moresnow.model;

import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.helinos.moresnow.block.logic.BlockLogicSnowyFence;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.block.Block;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

import static net.helinos.moresnow.model.MoreSnowModels.zFactor;

public class BlockModelSnowyFence<T extends BlockLogicSnowy<?>> extends BlockModelSnowy<T> {
	public BlockModelSnowyFence(Block<T> block, BlockModel<?> layerModel) {
		super(block, layerModel);
	}

	@Override
	public boolean render(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource, @NotNull TilePosc tilePos) {
		BlockLogicSnowyFence<?, ?> logic = (BlockLogicSnowyFence<?, ?>) this.block.getLogic();
		int metadata = worldSource.getBlockData(tilePos);
		boolean somethingRendered = false;
		somethingRendered |= this.renderSnowLayers(tessellator, worldSource, tilePos, logic);
		int layers = logic.getLayers(metadata);
		double height = layers * 2 / 16.0;
		AABBdc bounds = new AABBd(-zFactor, 0.0, -zFactor, 1.0f + zFactor, height + zFactor, 1.0f + zFactor);
		somethingRendered |= renderBlocks.renderStandardBlock(tessellator, worldSource, this.layerModel, bounds, tilePos);
		return somethingRendered;
	}


	private boolean renderSnowLayers(TessellatorGeneral tessellator, @NotNull WorldSource worldSource, @NotNull TilePosc tilePos, BlockLogicSnowyFence<?, ?> logic) {
		boolean somethingRendered = false;
		// Center post
		AABBd bounds = new AABBd(0.375, 0.0, 0.375, 0.625, 1.0, 0.625);
		int x = tilePos.x();
		int y = tilePos.y();
		int z = tilePos.z();
		renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
		boolean connectEast = logic.canConnectTo(worldSource,	 new TilePos(x - 1, y, z));
		boolean connectWest = logic.canConnectTo(worldSource,	 new TilePos(x + 1, y, z));
		boolean connectNorth = logic.canConnectTo(worldSource,	 new TilePos(x, y, z - 1));
		boolean connectSouth = logic.canConnectTo(worldSource,	 new TilePos(x, y, z + 1));
		boolean renderEastWest = connectEast || connectWest;
		boolean renderNorthSouth = connectNorth || connectSouth;

		float east = connectEast ? 0.0F : 0.4375F;
		float west = connectWest ? 1.0F : 0.5625F;
		float north = connectNorth ? 0.0F : 0.4375F;
		float south = connectSouth ? 1.0F : 0.5625F;

		// Upper connecting posts
		if (renderEastWest) {
			bounds.setMin(east, 0.75, 0.4375).setMax(west, 0.9375, 0.5625);
			somethingRendered |= renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
		}

		if (renderNorthSouth) {
			bounds.setMin(0.4375, 0.75, north).setMax(0.5625, 0.9375, south);
			somethingRendered |= renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
		}

		// Lower connecting posts
		if (renderEastWest) {
			bounds.setMin(east, 0.375, 0.4375).setMax(west, 0.5625, 0.5625);
			somethingRendered |= renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
		}

		if (renderNorthSouth) {
			bounds.setMin(0.4375, 0.375, north).setMax(0.5625, 0.5625, south);
			somethingRendered |= renderBlocks.renderStandardBlock(tessellator, worldSource, this, bounds, tilePos);
		}
		return somethingRendered;
	}


	public void renderLayerOnInventory(@NotNull TessellatorGeneral tessellator, int metadata, byte lightIndex) {
		tessellator.offsetTranslation(0.0F, -0.25F, 0.0F);
		this.layerModel.renderStandalone(tessellator, metadata, lightIndex);
		tessellator.offsetTranslation(0.0F, 0.25, 0.0F);
	}
}
