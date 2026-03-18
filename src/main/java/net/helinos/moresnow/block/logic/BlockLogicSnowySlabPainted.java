package net.helinos.moresnow.block.logic;

import net.helinos.moresnow.util.BlockMetadata;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.WorldSource;

public class BlockLogicSnowySlabPainted<T extends BlockLogic> extends BlockLogicSnowy<T> implements PaintedBlock {
	private final DyeColor color;

	public BlockLogicSnowySlabPainted(Block<T> block, Block<?> storedBlock, DyeColor color) {
		super(block, storedBlock, 4, 4);
		this.color = color;
	}

	@Override
	public DyeColor getColor() {
		return this.color;
	}

	@Override
	public String getLanguageKey(int meta) {
		return storedBlock.getLogic() instanceof BlockLogicSnowy ? "snowy" : storedBlock.getLogic().getLanguageKey(this.color.blockMeta << 4);
	}

	@Override
	public boolean canReplaceBlock(int id, int metadata) {
		return id == getStoredBlockId(metadata) && (metadata & 3) == 0;
	}

	@Override
	public AABB getBlockBoundsFromState(WorldSource world, int x, int y, int z) {
		int l = this.getRelativeLayers(world.getBlockMetadata(x, y, z)) - 1;
		float f = (2 * (1 + l)) / 16.0F;
		return AABB.getTemporaryBB(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
	}

	@Override
	public int getStoredBlockMetadata(int metadata) {
		return BlockMetadata.setBitBlock(metadata >> 4, START_INDEX, END_INDEX, this.color.blockMeta & 15);
	}

	@Override
	protected int blockToMetadata(int blockId, int metadata) {
		return metadata;
	}

	@Override
	public boolean isSolidRender() {
		return false;
	}
}
