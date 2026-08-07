package net.helinos.moresnow.block.logic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicFenceThin;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

public class BlockLogicSnowyFenceThin<T extends BlockLogic, F extends BlockLogicFenceThin> extends BlockLogicSnowy<T> {
	private final Class<F> storedBlockLogic;

	public BlockLogicSnowyFenceThin(Block<T> block, Block<?> storedBlock, Class<F> storedBlockLogic) {
		super(block, storedBlock, 8, 0);
		this.setBlockBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
		this.storedBlockLogic = storedBlockLogic;
	}

	public boolean canConnectTo(WorldSource world, TilePosc tilePosc) {
		Block<?> block = world.getBlockType(tilePosc);
		return BlockTags.CHAINLINK_FENCES_CONNECT.appliesTo(block) || (block.getMaterial().isStone() || block.getMaterial().isMetal());
	}

	@Override
	public void getCollisionAABBs(@NotNull World world, @NotNull TilePosc tilePos, @NotNull AABBdc aabb, @NotNull List<@NotNull AABBdc> aabbList) {
		int metadata = world.getBlockData(tilePos);
		int layers = this.getLayers(metadata);
		double height = layers * 2 / 16.0;
		int x = tilePos.x();
		int y = tilePos.y();
		int z = tilePos.z();
		this.addIntersectingBoundingBox(aabb, new AABBd(0.0, 0.0, 0.0, 1.0, height, 1.0).translate(x, y, z), aabbList);

		boolean connectXPos = this.canConnectTo(world, new TilePos(x + 1, y, z));
		boolean connectXNeg = this.canConnectTo(world, new TilePos(x - 1, y, z));
		boolean connectZPos = this.canConnectTo(world, new TilePos(x, y, z + 1));
		boolean connectZNeg = this.canConnectTo(world, new TilePos(x, y, z - 1));

		AABBd bounds =  new AABBd(
			0.0 + (connectXNeg ? 0.0 : 0.375),
			0.0,
			0.0 + (connectZNeg ? 0.0 : 0.375),
			1.0 - (connectXPos ? 0.0 : 0.375),
			1.0,
			1.0 - (connectZPos ? 0.0 : 0.375)
		).translate(x, y, z);
		this.addIntersectingBoundingBox(aabb, bounds, aabbList);
	}

	public boolean shouldDrawColumn(WorldSource world, TilePosc tilePosc) {
		int x = tilePosc.x();
		int y = tilePosc.y();
		int z = tilePosc.z();
		if (this.shouldDrawColumnDo(world, tilePosc)) {
			return true;
		}

		int offsetY = 0;
		while (true) {
			offsetY++;
			Block<?> block = world.getBlockType(new TilePos(x, y + offsetY, z));
			if (!storedBlockLogic.isInstance(block.getLogic())) {
				break;
			}
		}
		offsetY -= 1;
		boolean drawColumnFromOther = false;
		while (storedBlockLogic.isInstance(world.getBlockType(new TilePos(x, y + offsetY, z)).getLogic())) {
			if (this.shouldDrawColumnDo(world, new TilePos(x, y + offsetY, z))) {
				drawColumnFromOther = true;
				break;
			}
			offsetY--;
		}
		return drawColumnFromOther;
	}

	private boolean shouldDrawColumnDo(WorldSource world, TilePosc tilePosc) {
		int x = tilePosc.x();
		int y = tilePosc.y();
		int z = tilePosc.z();
		boolean connectNorth = this.canConnectTo(world, new TilePos(x + Direction.NORTH.offsetX(), 	y, 	z + Direction.NORTH.offsetZ()));
		boolean connectSouth = this.canConnectTo(world, new TilePos(x + Direction.SOUTH.offsetX(), 	y, 	z + Direction.SOUTH.offsetZ()));
		boolean connectEast =  this.canConnectTo(world, new TilePos(x + Direction.EAST.offsetX(), 	y, 	z + Direction.EAST.offsetZ()));
		boolean connectWest =  this.canConnectTo(world, new TilePos(x + Direction.WEST.offsetX(), 	y, 	z + Direction.WEST.offsetZ()));
		boolean hasNorthOrSouth = connectNorth || connectSouth;
		boolean hasEastOrWest = connectEast || connectWest;
		if (hasNorthOrSouth && hasEastOrWest) {
			return true;
		}
		boolean lineNorthSouth = connectNorth && connectSouth;
		boolean lineEastWest = connectEast && connectWest;
		return !lineNorthSouth && !lineEastWest;
	}

	@Override
	public boolean canReplaceBlock(int id, int metadata) {
		return id == this.storedBlock.id();
	}

	@Override
	public boolean canPlaceOnSurface() {
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
