package net.helinos.moresnow.block.logic;

import java.util.List;

import net.helinos.moresnow.block.interfaces.IBlockLogicSnowyRotation;
import net.helinos.moresnow.block.interfaces.PaintedBlock;
import net.helinos.moresnow.util.BlockMetadata;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

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
	public @NotNull String getLanguageKey(int meta) {
		return this.storedBlock().getLogic() instanceof BlockLogicSnowy ? "snowy" : this.storedBlock().getLogic().getLanguageKey(meta) + "." + this.color.colorID;
	}

	@Override
	public boolean canReplaceBlock(int id, int metadata) {
		return id == storedBlockId(metadata) && (metadata & 8) == 0;
	}

	public boolean tryMakeSnowy(World world, int id, int meta, TilePosc tilePosc) {
		return BlockLogicSnowyStairs.tryMakeSnowyDo(this, world, id, meta, tilePosc);
	}

	public boolean tryMakeSnowy(Chunk chunk, int id, int meta, TilePosc tilePosc) {
		return BlockLogicSnowyStairs.tryMakeSnowyDo(this, chunk, id, meta, tilePosc);
	}

	@Override
	public void accumulate(World world, TilePosc tilePosc) {
		super.accumulate(world, tilePosc);
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
			this.addIntersectingBoundingBox(aabb, new AABBd(0.0, 0.0, 0.0, 0.5, 0.5 + heightFromSnow, 1.0).translate(x, y, z), aabbList);
			this.addIntersectingBoundingBox(aabb, new AABBd(0.5, 0.0, 0.0, 1.0, 1.0, 1.0).translate(x, y, z), aabbList);
		} else if (rotation == 1) {
			this.addIntersectingBoundingBox(aabb, new AABBd(0.0, 0.0, 0.0, 0.5, 1.0, 1.0).translate(x, y, z), aabbList);
			this.addIntersectingBoundingBox(aabb, new AABBd(0.5, 0.0, 0.0, 1.0, 0.5 + heightFromSnow, 1.0).translate(x, y, z), aabbList);
		} else if (rotation == 2) {
			this.addIntersectingBoundingBox(aabb, new AABBd(0.0, 0.0, 0.0, 1.0, 0.5 + heightFromSnow, 0.5).translate(x, y, z), aabbList);
			this.addIntersectingBoundingBox(aabb, new AABBd(0.0, 0.0, 0.5, 1.0, 1.0, 1.0).translate(x, y, z), aabbList);
		} else {
			this.addIntersectingBoundingBox(aabb, new AABBd(0.0, 0.0, 0.0, 1.0, 1.0, 0.5).translate(x, y, z), aabbList);
			this.addIntersectingBoundingBox(aabb, new AABBd(0.0, 0.0, 0.5, 1.0, 0.5 + heightFromSnow, 1.0).translate(x, y, z), aabbList);
		}
	}

	@Override
	public void onNeighborChanged(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Block<?> block) {
		BlockLogicSnowyStairs.onNeighborBlockChangeDo(this, world, tilePos, block);
	}

	@Override
	public int storedBlockMetadata(int metadata) {
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
