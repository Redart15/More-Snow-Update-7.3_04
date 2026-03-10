package net.helinos.moresnow.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.helinos.moresnow.block.BlockLogicSnowy;
import net.helinos.moresnow.block.BlockLogicSnowyPlant;
import net.helinos.moresnow.block.IBlockLogicSnowyStairs;
import net.helinos.moresnow.block.MSBlocks;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlockLayer;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ItemBlockLayer.class, remap = false)
public abstract class ItemBlockLayerMixin {
	@WrapMethod(method = "onUseItemOnBlock")
	private boolean onUseItemOnBlock(ItemStack itemstack, Player player, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced, Operation<Boolean> original) {
		int blockId = world.getBlockId(blockX, blockY, blockZ);
		int metadata = world.getBlockMetadata(blockX, blockY, blockZ);
		Block<?> block = Blocks.getBlock(blockId);

		if (itemstack.stackSize <= 0) {
			return false;
		}
		if (blockY == world.getHeightBlocks() - 1 && itemstack.itemID == Blocks.LAYER_SNOW.id()) {
			return false;
		}

		// Incrementing layer count on snow covered blocks with the snow layer item
		if (itemstack.itemID == Blocks.LAYER_SNOW.id() && side == Side.TOP && block.getLogic() instanceof BlockLogicSnowy) {
			BlockLogicSnowy<?> blockSnowy = (BlockLogicSnowy<?>) block.getLogic();
			int newLayers = blockSnowy.getLayers(metadata) + 1;

			AABB bbBox = AABB.getTemporaryBB(blockX, blockY, blockZ, block.getBounds().maxX, block.getBounds().maxY + 0.125f, block.getBounds().maxZ);
			if (!world.checkIfAABBIsClear(bbBox)) {
				return false;
			}

			if (block.getLogic() instanceof BlockLogicSnowyPlant) {
				if (newLayers <= blockSnowy.getMaxLayers()) {
					world.setBlockAndMetadataWithNotify(blockX, blockY, blockZ, block.id(), (metadata & ~(blockSnowy.getMaxLayers() - 1)) | newLayers - 1);
				} else {
					int storedID = ((BlockLogicSnowyPlant<?, ?>) block.getLogic()).getStoredBlockId(metadata);
					block.getLogic().dropBlockWithCause(world, EnumDropCause.WORLD, blockX, blockY, blockZ, metadata, null, null);
					world.playBlockSoundEffect(player, blockX, blockY, blockZ, Blocks.getBlock(storedID), EnumBlockSoundEffectType.DIG);
					world.setBlockWithNotify(blockX, blockY, blockZ, Blocks.BLOCK_SNOW.id());
				}
			} else {
				if (newLayers <= blockSnowy.getMaxLayers()) {
					if (block.getLogic() instanceof IBlockLogicSnowyStairs && world.getBlock(blockX, blockY + 1, blockZ) == null) {
						world.setBlockAndMetadataWithNotify(blockX, blockY + 1, blockZ, MSBlocks.SNOWY_PARTIAL.id(), newLayers - 1);
					}
					world.setBlockAndMetadataWithNotify(blockX, blockY, blockZ, block.id(), (metadata & ~(blockSnowy.getMaxLayers() - 1)) | newLayers - 1);
				} else {
					return original.call(itemstack, player, world, blockX, blockY, blockZ, side, xPlaced, yPlaced);
				}
			}

			world.playBlockSoundEffect(player, blockX + 0.5d, blockY + 0.5d, blockZ + 0.5d, Blocks.LAYER_SNOW, EnumBlockSoundEffectType.PLACE);
			itemstack.consumeItem(player);
			return true;
		}

		// Cover blocks that can be covered
		if (itemstack.itemID == Blocks.LAYER_SNOW.id()) {
			if (!MSBlocks.tryMakeSnowy(world, blockId, blockX, blockY, blockZ)) {
				return original.call(itemstack, player, world, blockX, blockY, blockZ, side, xPlaced, yPlaced);
			}
			world.playBlockSoundEffect(player, blockX + 0.5d, blockY + 0.5d, blockZ + 0.5d, Blocks.LAYER_SNOW, EnumBlockSoundEffectType.PLACE);
			itemstack.consumeItem(player);
			return true;
		}
		return false;
	}
}
