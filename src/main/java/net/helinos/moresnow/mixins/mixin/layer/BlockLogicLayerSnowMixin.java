package net.helinos.moresnow.mixins.mixin.layer;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicLayerSnow;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = BlockLogicLayerSnow.class, remap = false)
public abstract class BlockLogicLayerSnowMixin {

	@WrapOperation(method = "accumulate", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;getBlockData(Lnet/minecraft/core/world/pos/TilePosc;)I"))
	private int metadata1(@NotNull World world, @NotNull TilePosc tilePos, Operation<Integer> original) {
		int metadata = original.call(world, tilePos);
		Block<?> block = world.getBlockType(tilePos);
		if (block.getLogic() instanceof BlockLogicSnowy) {
			return ((BlockLogicSnowy<?>) block.getLogic()).getRelativeLayers(metadata) - 1;
		}
		return metadata;
	}

	@WrapOperation(method = "accumulate", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;getBlockType(Lnet/minecraft/core/world/pos/TilePosc;)Lnet/minecraft/core/block/Block;"))
	private Block<?> blockId1(@NotNull World world, @NotNull TilePosc tilePos, Operation<Block<?>> original) {
		Block<?> block = original.call(world, tilePos);
		if (block.id() == Blocks.LAYER_SNOW.id() || (block.getLogic() instanceof BlockLogicSnowy)) {
			return Blocks.LAYER_SNOW;
		}
		return block;
	}
}
