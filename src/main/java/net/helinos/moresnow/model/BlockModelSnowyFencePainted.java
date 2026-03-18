package net.helinos.moresnow.model;

import net.helinos.moresnow.block.logic.PaintedBlock;
import net.helinos.moresnow.util.BlockMetadata;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import org.jetbrains.annotations.Nullable;

public class BlockModelSnowyFencePainted<T extends BlockLogic & PaintedBlock> extends BlockModelSnowyFence<T> {
    public BlockModelSnowyFencePainted(Block<T> block, BlockModel<?> layerModel, String texID) {
		super(block, layerModel, texID);
    }

	@Override
	public void renderBlockOnInventory(Tessellator tessellator, int metadata, float brightness, float alpha, @Nullable Integer lightmapCoordinate) {
		T logic = this.block.getLogic();
		int newMetadata = BlockMetadata.setBitBlock(metadata >> 4, 0, 3, logic.getColor().blockMeta);
		super.renderBlockOnInventory(tessellator, newMetadata, brightness, alpha, lightmapCoordinate);
	}
}
