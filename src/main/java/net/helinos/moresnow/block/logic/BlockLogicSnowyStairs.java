package net.helinos.moresnow.block.logic;

import net.helinos.moresnow.block.interfaces.IBlockLogicSnowyRotation;
import net.helinos.moresnow.util.BlockMetadata;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicStairs;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.pos.ChunkTilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

import java.util.List;

public class BlockLogicSnowyStairs<T extends BlockLogic, S extends BlockLogicStairs> extends BlockLogicSnowy<T> implements IBlockLogicSnowyRotation {
	public BlockLogicSnowyStairs(Block<T> block, Block<?> storedBlock) {
		super(block, storedBlock, 4, 4);
		this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
	}

	@Override
	public boolean canReplaceBlock(int id, int metadata) {
		if (super.canReplaceBlock(id, metadata)) {
			return (metadata & 0b1000) == 0;
		}

		return false;
	}


	public static boolean tryMakeSnowyDo(BlockLogicSnowy<?> logic, World world, int id, int meta, TilePosc tilePosc) {
		if (!logic.canReplaceBlock(id, meta)) {
			return false;
		}
		return world.setBlockTypeDataNotify(tilePosc, logic.block, logic.blockToMetadata(id, meta));
	}


	public boolean tryMakeSnowy(Chunk chunk, int id, int meta, TilePosc tilePosc) {
		return tryMakeSnowyDo(this, chunk, id, meta, tilePosc);
	}

	public static boolean tryMakeSnowyDo(BlockLogicSnowy<?> logic, Chunk chunk, int id, int meta, TilePosc tilePosc) {
		if (!logic.canReplaceBlock(id, meta)) {
			return false;
		}
		return chunk.setBlockIdData(new ChunkTilePos(tilePosc), logic.block.id(), logic.blockToMetadata(id, meta));
	}

	@Override
	public void getCollisionAABBs(@NotNull World world, @NotNull TilePosc tilePos, @NotNull AABBdc aabb, @NotNull List<@NotNull AABBdc> aabbList) {
		int metadata = world.getBlockData(tilePos);
		int rotation = this.getRotation(metadata);
		int layers = this.getLayers(metadata);
		double heightFromSnow = layers * 2 / 16.0;
		int x = tilePos.x();
		int y = tilePos.y();
		int z = tilePos.z();
		if (rotation == 0) {
			//fucked
			this.addIntersectingBoundingBox(aabb, new AABBd(0.0, 0.0, 0.0, 0.5, 0.5 + heightFromSnow, 1.0).translate(x, y, z), aabbList);
			this.addIntersectingBoundingBox(aabb, new AABBd(0.5, 0.0, 0.0, 1.0, heightFromSnow, 1.0).translate(x, y + 1.0f, z), aabbList);
			this.addIntersectingBoundingBox(aabb, new AABBd(0.5, 0.0, 0.0, 1.0, 1.0, 1.0).translate(x, y, z), aabbList);
		} else if (rotation == 1) {
			this.addIntersectingBoundingBox(aabb, new AABBd(0.0, 0.0, 0.0, 0.5, 1.0, 1.0).translate(x, y, z), aabbList);
			this.addIntersectingBoundingBox(aabb, new AABBd(0.5, 0.0, 0.0, 1.0, 0.5 + heightFromSnow, 1.0).translate(x, y, z), aabbList);
			this.addIntersectingBoundingBox(aabb, new AABBd(0.0, 0.0, 0.0, 0.5, heightFromSnow, 1.0).translate(x, y + 1.0f, z), aabbList);
		} else if (rotation == 2) {
			this.addIntersectingBoundingBox(aabb, new AABBd(0.0, 0.0, 0.0, 1.0, 0.5 + heightFromSnow, 0.5).translate(x, y, z), aabbList);
			this.addIntersectingBoundingBox(aabb, new AABBd(0.0, 0.0, 0.5, 1.0, heightFromSnow, 1.0).translate(x, y + 1.0f, z), aabbList);
			this.addIntersectingBoundingBox(aabb, new AABBd(0.0, 0.0, 0.5, 1.0, 1.0, 1.0).translate(x, y, z), aabbList);
		} else {
			this.addIntersectingBoundingBox(aabb, new AABBd(0.0, 0.0, 0.0, 1.0, 1.0, 0.5).translate(x, y, z), aabbList);
			this.addIntersectingBoundingBox(aabb, new AABBd(0.0, 0.0, 0.5, 1.0, 0.5 + heightFromSnow, 1.0).translate(x, y, z), aabbList);
			this.addIntersectingBoundingBox(aabb, new AABBd(0.0, 0.0, 0.0, 1.0, heightFromSnow, 0.5).translate(x, y + 1.0f, z), aabbList);
		}
	}

	@Override
	public void onNeighborChanged(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Block<?> block) {
		onNeighborBlockChangeDo(this, world, tilePos, block);
	}


	public static void onNeighborBlockChangeDo(BlockLogicSnowy<?> logic, World world, TilePosc tilePos, Block<?> block) {
	}

	@Override
	public int getStoredBlockMetadata(int metadata) {
		return BlockMetadata.setBitBlock(metadata >> 4, START_INDEX, END_INDEX, 0);
	}


	@Override
	protected int blockToMetadata(int blockId, int metadata) {
		return metadata << 4;
	}

	@Override
	public int getRotation(int metadata) {
		return (metadata >> 4) & 0b11;
	}

	@Override
	public boolean isSolidRender() {
		return false;
	}

	@Override
	public boolean isCubeShaped() {
		return false;
	}
}
