package net.helinos.moresnow.model;

import net.helinos.moresnow.block.logic.PaintedBlock;
import net.helinos.moresnow.util.BlockMetadata;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import org.jetbrains.annotations.Nullable;

public class BlockModelSnowySlabPainted<T extends BlockLogic & PaintedBlock> extends BlockModelSnowySlab<T> {

	public BlockModelSnowySlabPainted(Block<T> block) {
		super(block);
	}

	@Override
	public void renderBlockOnInventory(Tessellator tessellator, int metadata, float brightness, float alpha, @Nullable Integer lightmapCoordinate) {
		T logic = this.block.getLogic();
		int newMetadata = BlockMetadata.setBitBlock(metadata >> 4, 4, 7, logic.getColor().blockMeta);
		super.renderBlockOnInventory(tessellator, newMetadata, brightness, alpha, lightmapCoordinate);
	}
}
