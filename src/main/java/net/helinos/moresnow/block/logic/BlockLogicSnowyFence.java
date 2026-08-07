package net.helinos.moresnow.block.logic;

import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicFence;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

//Done
public class BlockLogicSnowyFence<T extends BlockLogic, F extends BlockLogicFence> extends BlockLogicSnowy<T> {
    public BlockLogicSnowyFence(Block<T> block, Block<?> storedBlock) {
        super(block, storedBlock, 8, 0);
        this.setBlockBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
    }

    public boolean canConnectTo(WorldSource worldSource, TilePosc tilePosc) {
        int blockID = worldSource.getBlockType(tilePosc).id();
        return Blocks.hasTag(blockID, BlockTags.FENCES_CONNECT);
    }

	@Override
	public void getCollisionAABBs(@NotNull World world, @NotNull TilePosc tilePos, @NotNull AABBdc aabb, @NotNull List<@NotNull AABBdc> aabbList) {
		int x = tilePos.x();
		int y = tilePos.y();
		int z = tilePos.z();
		int metadata = world.getBlockData(tilePos);
		int layers = this.getLayers(metadata);
		double height = layers * 2 / 16.0;

		this.addIntersectingBoundingBox(aabb, new AABBd(0.0, 0.0, 0.0, 1.0, height, 1.0).translate(x, y, z), aabbList);

		boolean connectXPos = this.canConnectTo(world, new TilePos(x + 1, y, z));
		boolean connectXNeg = this.canConnectTo(world, new TilePos(x - 1, y, z));
		boolean connectZPos = this.canConnectTo(world, new TilePos(x, y, z + 1));
		boolean connectZNeg = this.canConnectTo(world, new TilePos(x, y, z - 1));

		AABBd bounds = new AABBd(
			0.0 + (connectXNeg ? 0.0 : 0.375),
			0.0,
			0.0 + (connectZNeg ? 0.0 : 0.375),
			1.0 - (connectXPos ? 0.0 : 0.375),
			1.5,
			1.0 - (connectZPos ? 0.0 : 0.375)
		).translate(x, y, z);
		this.addIntersectingBoundingBox(aabb, bounds, aabbList);
	}

	@Override
	public boolean canPlaceAt(@NotNull World world, @NotNull TilePosc tilePos) {
		return true;
	}

	@Override
    public boolean isSolidRender() {
        return false;
    }

    @Override
    public boolean isCubeShaped() {
        return false;
    }


	@Override
	public boolean getSupportsOwnSnow() {
		return false;
	}
}
