package net.helinos.moresnow.block.logic;

import net.helinos.moresnow.block.interfaces.IBlockLogicSnowyRotation;
import net.helinos.moresnow.util.BlockMetadata;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicStairs;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;

import java.util.ArrayList;

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


	public static boolean tryMakeSnowyDo(BlockLogicSnowy<?> logic, World world, int id, int meta, int x, int y, int z) {
		if (!logic.canReplaceBlock(id, meta)) {
			return false;
		}
		return world.setBlockAndMetadataWithNotify(x, y, z, logic.id(), logic.blockToMetadata(id, meta));
	}


	public boolean tryMakeSnowy(Chunk chunk, int id, int meta, int x, int y, int z) {
		return tryMakeSnowyDo(this, chunk, id, meta, x, y, z);
	}

	public static boolean tryMakeSnowyDo(BlockLogicSnowy<?> logic, Chunk chunk, int id, int meta, int x, int y, int z) {
		if (!logic.canReplaceBlock(id, meta)) {
			return false;
		}
		return chunk.setBlockIDWithMetadata(x, y, z, logic.block.id(), logic.blockToMetadata(id, meta));
	}

	@Override
	@SuppressWarnings(value = { "unchecked", "rawtypes" })
	public void getCollidingBoundingBoxes(World world, int x, int y, int z, AABB aabb, ArrayList aabbList) {
		int metadata = world.getBlockMetadata(x, y, z);
		int rotation = this.getRotation(metadata);
		int layers = this.getLayers(metadata);
		double heightFromSnow = layers * 2 / 16.0;
		if (rotation == 0) {
			//fucked
			this.addIntersectingBoundingBox(aabb, AABB.getTemporaryBB(0.0, 0.0, 0.0, 0.5, 0.5 + heightFromSnow, 1.0).move(x, y, z), aabbList);
			this.addIntersectingBoundingBox(aabb, AABB.getTemporaryBB(0.5, 0.0, 0.0, 1.0, heightFromSnow, 1.0).move(x, y + 1.0f, z), aabbList);
			this.addIntersectingBoundingBox(aabb, AABB.getTemporaryBB(0.5, 0.0, 0.0, 1.0, 1.0, 1.0).move(x, y, z), aabbList);
		} else if (rotation == 1) {
			this.addIntersectingBoundingBox(aabb, AABB.getTemporaryBB(0.0, 0.0, 0.0, 0.5, 1.0, 1.0).move(x, y, z), aabbList);
			this.addIntersectingBoundingBox(aabb, AABB.getTemporaryBB(0.5, 0.0, 0.0, 1.0, 0.5 + heightFromSnow, 1.0).move(x, y, z), aabbList);
			this.addIntersectingBoundingBox(aabb, AABB.getTemporaryBB(0.0, 0.0, 0.0, 0.5, heightFromSnow, 1.0).move(x, y + 1.0f, z), aabbList);
		} else if (rotation == 2) {
			this.addIntersectingBoundingBox(aabb, AABB.getTemporaryBB(0.0, 0.0, 0.0, 1.0, 0.5 + heightFromSnow, 0.5).move(x, y, z), aabbList);
			this.addIntersectingBoundingBox(aabb, AABB.getTemporaryBB(0.0, 0.0, 0.5, 1.0, heightFromSnow, 1.0).move(x, y + 1.0f, z), aabbList);
			this.addIntersectingBoundingBox(aabb, AABB.getTemporaryBB(0.0, 0.0, 0.5, 1.0, 1.0, 1.0).move(x, y, z), aabbList);
		} else {
			this.addIntersectingBoundingBox(aabb, AABB.getTemporaryBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.5).move(x, y, z), aabbList);
			this.addIntersectingBoundingBox(aabb, AABB.getTemporaryBB(0.0, 0.0, 0.5, 1.0, 0.5 + heightFromSnow, 1.0).move(x, y, z), aabbList);
			this.addIntersectingBoundingBox(aabb, AABB.getTemporaryBB(0.0, 0.0, 0.0, 1.0, heightFromSnow, 0.5).move(x, y + 1.0f, z), aabbList);
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
		onNeighborBlockChangeDo(this, world, x, y, z, blockId);
	}

	public static void onNeighborBlockChangeDo(BlockLogicSnowy<?> logic, World world, int x, int y, int z, int blockId) {
//		Block<?> blockAbove = world.getBlock(x, y + 1, z);
//		int metadata = world.getBlockMetadata(x, y, z);
//
//		if (blockAbove != null && blockAbove.getLogic() instanceof BlockLogicSnowyPartial) {
//			BlockLogicSnowyPartial<?> blockSnowyPartial = (BlockLogicSnowyPartial<?>) blockAbove.getLogic();
//			int aboveMetadata = world.getBlockMetadata(x, y + 1, z);
//			int aboveLayers = blockSnowyPartial.getLayers(aboveMetadata);
//
//			if (aboveLayers != logic.getLayers(metadata)) {
//				world.setBlockMetadata(x, y, z, (metadata & 0b11111100) | aboveLayers - 1);
//			}
//		} else {
//			logic.removeSnow(world, metadata, x, y, z);
//		}
	}

	@Override
	public int getStoredBlockMetadata(int metadata) {
		return BlockMetadata.setBitBlock(metadata >> 4, START_INDEX, END_INDEX, 0);
	}

	@Override
	public int getStoredBlockId(int metadata) {
		return this.storedBlock.id();
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
