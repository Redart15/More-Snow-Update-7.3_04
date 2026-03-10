package net.helinos.moresnow.block;

import java.util.ArrayList;

import net.helinos.moresnow.util.BlockMetadata;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicFenceGate;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntityActivator;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.tag.ItemTags;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;

//Done
public class BlockLogicSnowyFenceGate<T extends BlockLogic> extends BlockLogicSnowy<T> {
	private final boolean isPainted;
	private final DyeColor color;

	public BlockLogicSnowyFenceGate(Block<T> block, @Nullable DyeColor color) {
		super(block, 8, 0, false);
		this.setBlockBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
		this.isPainted = color != null;
		this.color = color;
	}

	@Override
	public boolean canReplaceBlock(int id, int metadata) {
		return id == this.getStoredBlockId(metadata) && this.color == DyeColor.colorFromBlockMeta(metadata >> 4);
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void getCollidingBoundingBoxes(World world, int x, int y, int z, AABB aabb, ArrayList aabbList) {
		int metadata = world.getBlockMetadata(x, y, z);
		int layers = this.getLayers(metadata);
		double height = layers * 2 / 16.0;
		this.addIntersectingBoundingBox(aabb, AABB.getTemporaryBB(0.0, 0.0, 0.0, 1.0, height, 1.0).move(x, y, z), aabbList);
		if (!this.isOpen(metadata)) {
			if (this.getDirection(metadata) != 3 && this.getDirection(metadata) != 1) {
				this.addIntersectingBoundingBox(aabb, AABB.getTemporaryBB(0.0, 0.25, 0.375, 1.0, 1.50, 0.625).move(x, y, z), aabbList);
			} else {
				this.addIntersectingBoundingBox(aabb, AABB.getTemporaryBB(0.375, 0.25, 0.0, 0.625, 1.50, 1.0).move(x, y, z), aabbList);
			}
		}
	}

	@Override
	public void onBlockLeftClicked(World world, int x, int y, int z, Player player, Side side, double xHit, double yHit) {
		if (!Item.hasTag(player.getCurrentEquippedItem(), ItemTags.PREVENT_LEFT_CLICK_INTERACTIONS)) {
			this.onBlockRightClicked(world, x, y, z, player, null, 0.0, 0.0);
		}
	}

	@Override
	public boolean onBlockRightClicked(World world, int x, int y, int z, @Nullable Player player, Side side, double xPlaced, double yPlaced) {
		int metadata = world.getBlockMetadata(x, y, z);
		if (isOpen(metadata)) {
			world.setBlockMetadataWithNotify(x, y, z, BlockMetadata.flipBit(metadata, 6));
		} else {
			int newDirection = 0;
			if (player != null) {
				newDirection = (MathHelper.floor(player.yRot * 4.0 / 360.0 + 0.5) & 3) % 4;
			}
			int currentDirection = getDirection(metadata);
			if (currentDirection == ((newDirection + 2) % 4)) {
				metadata = BlockMetadata.setBitBlock(metadata, START_INDEX, START_INDEX + 1, newDirection);
			}
			world.setBlockMetadataWithNotify(x, y, z, BlockMetadata.setBit(metadata, START_INDEX + 2, 1));
		}
		if (Math.random() < 0.5) {
			world.playSoundEffect(player, SoundCategory.WORLD_SOUNDS, x, y, z, "random.door_open", 1.0F, 1.0F);
		} else {
			world.playSoundEffect(player, SoundCategory.WORLD_SOUNDS, x, y, z, "random.door_close", 1.0F, 1.0F);
		}

		return true;
	}

	@Override
	public void onActivatorInteract(World world, int x, int y, int z, TileEntityActivator activator, Direction direction) {
		this.onBlockRightClicked(world, x, y, z, null, direction.getSide(), 0.5, 0.5);
	}

	@Override
	public int getStoredBlockMetadata(int metadata) {
		return BlockMetadata.setBitBlock(metadata >> 4, START_INDEX, END_INDEX, this.isPainted ? this.color.blockMeta : 0);
	}

	@Override
	public int getStoredBlockId(int metadata) {
		if (this.isPainted) {
			return Blocks.FENCE_GATE_PLANKS_OAK_PAINTED.id();
		}
		return Blocks.FENCE_GATE_PLANKS_OAK.id();
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

	public DyeColor getColor() {
		if (this.isPainted) {
			return this.color;
		}
		return null;
	}
}
