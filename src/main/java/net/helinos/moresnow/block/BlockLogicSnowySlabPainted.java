package net.helinos.moresnow.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.WorldSource;

public class BlockLogicSnowySlabPainted<T extends BlockLogic> extends BlockLogicSnowy<T> {
	public BlockLogicSnowySlabPainted(Block<T> block) {
		super(block, 4, 4, true);
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
	public int getStoredBlockId(int metadata) {
		return Blocks.SLAB_PLANKS_PAINTED.id();
	}

	@Override
	public int getStoredBlockMetadata(int metadata) {
		return metadata & 0b11110000;
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
