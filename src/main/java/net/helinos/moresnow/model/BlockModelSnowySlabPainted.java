package net.helinos.moresnow.model;

import net.helinos.moresnow.block.interfaces.PaintedBlock;
import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.helinos.moresnow.util.BlockMetadata;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.block.Block;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

public class BlockModelSnowySlabPainted<T extends BlockLogicSnowy<?> & PaintedBlock> extends BlockModelSnowySlab<T> {

	public BlockModelSnowySlabPainted(Block<T> block, BlockModel<?> layerModel) {
		super(block, layerModel);
	}

	@Override
	public boolean render(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource, @NotNull TilePosc tilePos) {
		return super.render(tessellator, worldSource,tilePos);
	}

	@Override
	public void renderStandalone(@NotNull TessellatorGeneral tessellator, int metadata, byte lightIndex) {
		T logic = this.block.getLogic();
		int newMetadata = BlockMetadata.setBitBlock(metadata >> 4, 4, 7, logic.getColor().blockMeta);
		super.renderStandalone(tessellator, newMetadata, lightIndex);
	}
}
