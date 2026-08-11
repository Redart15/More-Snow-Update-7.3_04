package net.helinos.moresnow.model;

import net.helinos.moresnow.block.interfaces.PaintedBlock;
import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.helinos.moresnow.util.BlockMetadata;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockModelSnowyFencePainted<T extends BlockLogicSnowy<?> & PaintedBlock> extends BlockModelSnowyFence<T> {
    public BlockModelSnowyFencePainted(Block<T> block, BlockModel<?> layerModel) {
		super(block, layerModel);
    }

	@Override
	public void renderStandalone(@NotNull TessellatorGeneral tessellator, int metadata, byte lightIndex) {
		T logic = this.block.getLogic();
		int newMetadata = BlockMetadata.setBitBlock(metadata >> 4, 0, 3, logic.getColor().blockMeta);
		super.renderStandalone(tessellator, newMetadata, lightIndex);
	}
}
