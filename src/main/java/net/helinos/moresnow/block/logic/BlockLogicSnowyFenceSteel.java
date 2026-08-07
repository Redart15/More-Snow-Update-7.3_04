package net.helinos.moresnow.block.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicFenceSteel;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;

public class BlockLogicSnowyFenceSteel<T extends BlockLogic> extends BlockLogicSnowyFenceThin<T, BlockLogicFenceSteel> {
	public BlockLogicSnowyFenceSteel(Block<T> block) {
		super(block, Blocks.FENCE_STEEL, BlockLogicFenceSteel.class);
	}

	@Override
	public boolean canConnectTo(WorldSource world, TilePosc tilePosc) {
		Block<?> block = world.getBlockType(tilePosc);
		return BlockTags.CHAINLINK_FENCES_CONNECT.appliesTo(block) || (block.getMaterial().isStone() || block.getMaterial().isMetal());
	}
}
