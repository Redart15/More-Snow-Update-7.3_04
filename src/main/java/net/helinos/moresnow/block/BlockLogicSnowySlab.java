package net.helinos.moresnow.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicSlab;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.WorldSource;

import java.util.List;

public class BlockLogicSnowySlab<T extends BlockLogic, S extends BlockLogicSlab> extends BlockLogicSnowyMultiple<T> {
	public BlockLogicSnowySlab(Block<T> block, Class<S> blockLogic, List<Integer> excludedIds) {
		super(block, blockLogic, excludedIds, 4, 4, true, 2, 0b00001111);
		this.setBlockBounds(0.0, 0.0, 0.0, 1.0, 0.625, 1.0);
	}

	@Override
	public boolean canReplaceBlock(int id, int metadata) {
		if (super.canReplaceBlock(id, metadata)) {
			return (metadata & 0b11) == 0;
		}
		return false;
	}

	@Override
	public AABB getBlockBoundsFromState(WorldSource world, int x, int y, int z) {
		int l = this.getRelativeLayers(world.getBlockMetadata(x, y, z)) - 1;
		float f = (2 * (1 + l)) / 16.0F;
		return AABB.getTemporaryBB(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
	}

	@Override
	public int getStoredBlockMetadata(int metadata) {
		return 0;
	};

	@Override
	public boolean isSolidRender() {
		return false;
	}
}
