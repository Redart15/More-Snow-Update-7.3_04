package net.helinos.moresnow.block.logic;

import net.helinos.moresnow.util.BlockMetadata;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicTrapDoor;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.tag.ItemTags;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;

public class BlockLogicSnowyTrapDoor<T extends BlockLogic, F extends BlockLogicTrapDoor> extends BlockLogicSnowy<T> {

	public BlockLogicSnowyTrapDoor(Block<T> block, Block<?> storedBlock) {
		super(block, storedBlock, 8, 0);
	}

	@Override
	public AABB getBlockBoundsFromState(WorldSource world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y, z);
		int layers = this.getLayers(metadata);
		boolean isUp = BlockLogicTrapDoor.isTrapdoorOpen(metadata >> 4);
		double height = layers * 2 / 16.0;
		float initHeight = isUp ? 0.0f : 0.1875f;
		return AABB.getTemporaryBB(0.0, initHeight, 0.0, 1.0, initHeight + height, 1.0);
	}

	@SuppressWarnings("java:S1172")
	@Override
	public int getStoredBlockMetadata(int metadata) {
		return metadata >> 4;
	}

	@SuppressWarnings("java:S1172")
	@Override
	protected int blockToMetadata(int blockId, int metadata) {
		return metadata << 4;
	}

	@Override
	public void onBlockLeftClicked(World world, int x, int y, int z, Player player, Side side, double xHit, double yHit) {
		if (!Item.hasTag(player.getCurrentEquippedItem(), ItemTags.PREVENT_LEFT_CLICK_INTERACTIONS)) {
			this.onBlockRightClicked(world, x, y, z, player, null, 0.0F, 0.0F);
		}
	}

	@Override
	public boolean onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xPlaced, double yPlaced) {
		Material material = this.storedBlock.getLogic().getMaterial();
		if (material != Material.metal && material != Material.steel) {
			int metadata = world.getBlockMetadata(x, y, z);
			if(this.getRelativeLayers(metadata) <= 2){
				world.setBlockAndMetadataWithNotify(x,y,z, this.storedBlock.id(), metadata >> 4);
				return this.storedBlock.onBlockRightClicked(world, x, y, z, player, side, xPlaced, yPlaced);
			}
			world.setBlockMetadataWithNotify(x, y, z, BlockMetadata.setBitBlock(metadata, START_INDEX, END_INDEX, (metadata >> 4) ^ 4));
			world.playBlockEvent(player, 1003, x, y, z, 0);
			return true;
		} else {
			return false;
		}
	}
}
