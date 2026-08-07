package net.helinos.moresnow.block.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicFenceChainlink;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

//Done
public class BlockLogicSnowyFenceChainlink<T extends BlockLogic> extends BlockLogicSnowyFenceThin<T, BlockLogicFenceChainlink> {
    public BlockLogicSnowyFenceChainlink(Block<T> block) {
       super(block, Blocks.FENCE_CHAINLINK, BlockLogicFenceChainlink.class);
    }

    @Override
    public boolean canConnectTo(WorldSource world, TilePosc tilePosc) {
       Block<?> block = world.getBlockType(tilePosc);
       return BlockTags.CHAINLINK_FENCES_CONNECT.appliesTo(block) || (block.getMaterial().isStone() || block.getMaterial().isMetal());
    }

	@Override
	public boolean isClimbable(@NotNull World world, @NotNull TilePosc tilePos) {
		return true;
	}
 }
