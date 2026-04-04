package net.helinos.moresnow.block.logic;

import java.util.ArrayList;

import net.helinos.moresnow.block.interfaces.IBlockLogicSnowyRotation;
import net.helinos.moresnow.block.interfaces.PaintedBlock;
import net.helinos.moresnow.util.BlockMetadata;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;

public class BlockLogicSnowyStairsPainted<T extends BlockLogic> extends BlockLogicSnowy<T> implements IBlockLogicSnowyRotation, PaintedBlock {
	protected DyeColor color;

	public BlockLogicSnowyStairsPainted(Block<T> block, Block<?> storedBlock, DyeColor color) {
		super(block, storedBlock, 4, 4);
		this.color = color;
		this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
	}

	@Override
	public DyeColor getColor() {
		return this.color;
	}

	@Override
	public String getLanguageKey(int meta) {
		return storedBlock.getLogic() instanceof BlockLogicSnowy ? "snowy" : storedBlock.getLogic().getLanguageKey(meta) + "." + this.color.colorID;
	}

	@Override
	public boolean canReplaceBlock(int id, int metadata) {
		return id == getStoredBlockId(metadata) && (metadata & 8) == 0;
	}

	public boolean tryMakeSnowy(World world, int id, int meta, int x, int y, int z) {
		return BlockLogicSnowyStairs.tryMakeSnowyDo(this, world, id, meta, x, y, z);
	}

	public boolean tryMakeSnowy(Chunk chunk, int id, int meta, int x, int y, int z) {
		return BlockLogicSnowyStairs.tryMakeSnowyDo(this, chunk, id, meta, x, y, z);
	}

	@Override
	public void accumulate(World world, int x, int y, int z) {
		super.accumulate(world, x, y, z);
	}

	@Override
	@SuppressWarnings(value = { "unchecked", "rawtypes" })
	public void getCollidingBoundingBoxes(World world, int x, int y, int z, AABB aabb, ArrayList aabbList) {
		int metadata = world.getBlockMetadata(x, y, z);
		int rotation = this.getRotation(metadata);
		int layers = this.getLayers(metadata);
		double heightFromSnow = layers * 2 / 16.0;
		if (rotation == 0) {
			this.addIntersectingBoundingBox(aabb, AABB.getTemporaryBB(0.0, 0.0, 0.0, 0.5, 0.5 + heightFromSnow, 1.0).move(x, y, z), aabbList);
			this.addIntersectingBoundingBox(aabb, AABB.getTemporaryBB(0.5, 0.0, 0.0, 1.0, 1.0, 1.0).move(x, y, z), aabbList);
		} else if (rotation == 1) {
			this.addIntersectingBoundingBox(aabb, AABB.getTemporaryBB(0.0, 0.0, 0.0, 0.5, 1.0, 1.0).move(x, y, z), aabbList);
			this.addIntersectingBoundingBox(aabb, AABB.getTemporaryBB(0.5, 0.0, 0.0, 1.0, 0.5 + heightFromSnow, 1.0).move(x, y, z), aabbList);
		} else if (rotation == 2) {
			this.addIntersectingBoundingBox(aabb, AABB.getTemporaryBB(0.0, 0.0, 0.0, 1.0, 0.5 + heightFromSnow, 0.5).move(x, y, z), aabbList);
			this.addIntersectingBoundingBox(aabb, AABB.getTemporaryBB(0.0, 0.0, 0.5, 1.0, 1.0, 1.0).move(x, y, z), aabbList);
		} else {
			this.addIntersectingBoundingBox(aabb, AABB.getTemporaryBB(0.0, 0.0, 0.0, 1.0, 1.0, 0.5).move(x, y, z), aabbList);
			this.addIntersectingBoundingBox(aabb, AABB.getTemporaryBB(0.0, 0.0, 0.5, 1.0, 0.5 + heightFromSnow, 1.0).move(x, y, z), aabbList);
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
		BlockLogicSnowyStairs.onNeighborBlockChangeDo(this, world, x, y, z, blockId);
	}

	@Override
	public int getStoredBlockMetadata(int metadata) {
		return BlockMetadata.setBitBlock(metadata >> 4, START_INDEX, END_INDEX, this.color.blockMeta & 15);
	}

	@Override
	protected int blockToMetadata(int blockId, int metadata) {
		return (metadata & 0b11) << 4;
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
