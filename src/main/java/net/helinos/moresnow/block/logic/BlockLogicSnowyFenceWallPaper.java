package net.helinos.moresnow.block.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicFenceWallPaper;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;

public class BlockLogicSnowyFenceWallPaper<T extends BlockLogic> extends BlockLogicSnowyFenceThin<T, BlockLogicFenceWallPaper> {
	public BlockLogicSnowyFenceWallPaper(Block<T> block) {
		super(block, Blocks.FENCE_PAPER_WALL, BlockLogicFenceWallPaper.class);
	}

	@Override
	public boolean canConnectTo(WorldSource world, TilePosc tilePosc) {
		int blockID = world.getBlockType(tilePosc).id();
		return Blocks.hasTag(blockID, BlockTags.FENCES_CONNECT);
	}

	@Override
	public boolean getSupportsOwnSnow() {
		return false;
	}
}
