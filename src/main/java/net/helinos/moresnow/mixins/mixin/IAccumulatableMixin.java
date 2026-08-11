package net.helinos.moresnow.mixins.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.helinos.moresnow.MoreSnow;
import net.helinos.moresnow.block.MoreSnowBlocks;
import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.item.IAccumulatable;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = IAccumulatable.class, remap = false)
public interface IAccumulatableMixin {


	@WrapOperation(method = "placeOnBlockAccumulatable", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/item/IAccumulatable;canAccumulateInto(Lnet/minecraft/core/item/ItemStack;Lnet/minecraft/core/block/Block;ILnet/minecraft/core/util/helper/Side;DD)Z"))
	private boolean canSnowAccumulateInto(
		IAccumulatable<BlockLogic> instance,
		@NotNull ItemStack itemStack, @NotNull Block<?> blockBelow, int data,
		@NotNull Side side, double xHit, double yHit,
		Operation<Boolean> original,
		@Share("snowyBlock") LocalRef<@Nullable Block<?>> replacementBlock
	) {
		boolean originalResult = original.call(instance, itemStack, blockBelow, data, side, xHit, yHit);
		replacementBlock.set(null); // no block yet
		BlockLogic logic = blockBelow.getLogic();
		if (
			logic instanceof BlockLogicSnowy<?> snowyLogic
				&& itemStack.itemID == snowyLogic.layerBlock.id()
				&& side == Side.TOP
		) {
			int newLayers = snowyLogic.getLayers(data) + 1;
			if (newLayers <= snowyLogic.getMaxLayers()) {
				return true;
			}
		}
		Block<?> layerBlock = Blocks.getBlock(itemStack.itemID);
		if (MoreSnowBlocks.canConvert(blockBelow, data, MoreSnow.LAYERS.getKey(layerBlock) + "_%s")) {
			replacementBlock.set(MoreSnowBlocks.getBlock(blockBelow.id(), data, MoreSnow.LAYERS.getKey(layerBlock) + "_%s"));
			return true;
		}
		return originalResult;
	}

	@WrapOperation(method = "placeOnBlockAccumulatable", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/item/IAccumulatable;getAccumulationResult(Lnet/minecraft/core/item/ItemStack;Lnet/minecraft/core/world/World;Lnet/minecraft/core/entity/player/Player;Lnet/minecraft/core/world/pos/TilePosc;Lnet/minecraft/core/util/helper/Side;DDLnet/minecraft/core/item/IAccumulatable$BlockDataResult;)Lnet/minecraft/core/item/IAccumulatable$BlockDataResult;"))
	private IAccumulatable.BlockDataResult getBlockResultDataSnowy(
		IAccumulatable<?> instance,
		ItemStack itemStack,
		World world,
		Player player,
		TilePosc tilePosc,
		Side side, double xHit, double yHit,
		IAccumulatable.BlockDataResult blockDataResult,
		Operation<IAccumulatable.BlockDataResult> original,
		@Share("snowyBlock") LocalRef<Block<?>> blockLocalRef
	) {
		Block<?> accumulator = blockDataResult.block;
		if(accumulator.getLogic() instanceof BlockLogicSnowy<?> snowyAccumulatorLogic){
			int newLayers = snowyAccumulatorLogic.getLayers(blockDataResult.data) + 1;
			if (newLayers <= snowyAccumulatorLogic.getMaxLayers()) {
				blockDataResult.data = blockDataResult.data + 1;
			}
			return blockDataResult;
		}
		Block<?> block = blockLocalRef.get();
		if (block == null) {
			return original.call(instance, itemStack, world, player, tilePosc, side, xHit, yHit, blockDataResult);
		}
		BlockLogic logic = block.getLogic();
		if (logic instanceof BlockLogicSnowy<?> snowyLogic && snowyLogic.tryMakeSnowyCheck(world, blockDataResult.block.id(), tilePosc)) {
			blockDataResult.block = block;
			blockDataResult.data = snowyLogic.convertBlockToMetadata(block.id(), world.getBlockData(tilePosc));
			return blockDataResult;
		}
		return original.call(instance, itemStack, world, player, tilePosc, side, xHit, yHit, blockDataResult);
	}


	@WrapOperation(method = "placeOnBlockAccumulatable", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;notifyBlockChange(Lnet/minecraft/core/world/pos/TilePosc;Lnet/minecraft/core/block/Block;)V"))
	private void correctNotify(
		World instance, @NotNull TilePosc tilePos, @NotNull Block<?> block,
		Operation<Void> original,
		@Local(ordinal = 1) IAccumulatable.BlockDataResult result
	){
		original.call(instance, tilePos, result.block);
	}


	@WrapOperation(method = "placeOnBlockAccumulatable", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;playBlockSoundEffect(Lnet/minecraft/core/entity/Entity;DDDLnet/minecraft/core/block/Block;Lnet/minecraft/core/enums/EnumBlockSoundEffectType;)V"))
	private void correctSound(
		World instance,
		@Nullable Entity entity,
		double x, double y, double z,
		@NotNull Block<?> block,
		EnumBlockSoundEffectType soundEffectType,
		Operation<Void> original,
		@Local(ordinal = 1) IAccumulatable.BlockDataResult result
	){
		original.call(instance, entity, x, y, z, result.block, soundEffectType);
	}

}
