package net.helinos.moresnow.block.logic;

import java.util.List;

import net.helinos.moresnow.util.BlockMetadata;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicFenceGate;
import net.minecraft.core.block.entity.TileEntityActivator;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.tag.ItemTags;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

//Done
public class BlockLogicSnowyFenceGate<T extends BlockLogic> extends BlockLogicSnowy<T> {


	public BlockLogicSnowyFenceGate(Block<T> block, Block<?> storedBlock) {
		super(block, storedBlock, 8, 0);
		this.setBlockBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
	}

	@Override
	public boolean canReplaceBlock(int id, int metadata) {
		return id == this.getStoredBlockId(metadata);
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
		if (!this.isOpen(metadata)) {
			if (this.getDirection(metadata) != 3 && this.getDirection(metadata) != 1) {
				this.addIntersectingBoundingBox(aabb, new AABBd(0.0, 0.25, 0.375, 1.0, 1.50, 0.625).translate(x, y, z), aabbList);
			} else {
				this.addIntersectingBoundingBox(aabb, new AABBd(0.375, 0.25, 0.0, 0.625, 1.50, 1.0).translate(x, y, z), aabbList);
			}
		}
	}

	@Override
	public void onAttacked(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Player player, @NotNull Side side, double xHit, double yHit) {
		if (!Item.hasTag(player.getCurrentEquippedItem(), ItemTags.PREVENT_LEFT_CLICK_INTERACTIONS)) {
			this.onInteracted(world, tilePos, player, null, 0.0, 0.0);
		}
	}

	@Override
	public boolean onInteracted(@NotNull World world, @NotNull TilePosc tilePos, @Nullable Player player, @Nullable Side side, double xHit, double yHit) {
		int metadata = world.getBlockData(tilePos);
		int x = tilePos.x();
		int y  = tilePos.y();
		int z = tilePos.z();
		if(this.getLayers(metadata) >= this.getMaxLayers()){
			world.playSoundEffect(player, SoundCategory.WORLD_SOUNDS, x, y, z, "random.door_close", 1.0F, 1.0F);
			return false;
		}
		if (isOpen(metadata)) {
			world.setBlockDataNotify(tilePos, BlockMetadata.flipBit(metadata, 6));
		} else {
			int newDirection = 0;
			if (player != null) {
				newDirection = (MathHelper.floor(player.yRot * 4.0 / 360.0 + 0.5) & 3) % 4;
			}
			int currentDirection = getDirection(metadata);
			if (currentDirection == ((newDirection + 2) % 4)) {
				metadata = BlockMetadata.setBitBlock(metadata, START_INDEX, START_INDEX + 1, newDirection);
			}
			world.setBlockDataNotify(tilePos, BlockMetadata.setBit(metadata, START_INDEX + 2, 1));
		}
		if (Math.random() < 0.5) {
			world.playSoundEffect(player, SoundCategory.WORLD_SOUNDS, x, y, z, "random.door_open", 1.0F, 1.0F);
		} else {
			world.playSoundEffect(player, SoundCategory.WORLD_SOUNDS, x, y, z, "random.door_close", 1.0F, 1.0F);
		}

		return true;
	}

	@Override
	public void onActivatorInteracted(@NotNull World world, @NotNull TilePosc tilePos, @NotNull TileEntityActivator activator, @NotNull Direction direction) {
		this.onInteracted(world, tilePos, null, direction.side(), 0.5, 0.5);
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
	public boolean isSolidRender() {
		return false;
	}

	@Override
	public boolean isCubeShaped() {
		return false;
	}

	public boolean isOpen(int metadata) {
		return BlockLogicFenceGate.isOpen(BlockMetadata.getUpperBlock(metadata));
	}

	public int getDirection(int metadata) {
		return BlockLogicFenceGate.getDirection(BlockMetadata.getUpperBlock(metadata));
	}

	@Override
	public boolean getSupportsOwnSnow() {
		return false;
	}
}
