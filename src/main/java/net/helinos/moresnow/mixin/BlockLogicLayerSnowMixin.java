package net.helinos.moresnow.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicLayerSnow;
import net.minecraft.core.block.BlockLogicSlab;
import net.minecraft.core.block.BlockLogicStairs;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = BlockLogicLayerSnow.class, remap = false)
public abstract class BlockLogicLayerSnowMixin {

	@WrapOperation(method = "accumulate", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;getBlockMetadata(III)I"))
	private int metadata1(World world, int x, int y, int z, Operation<Integer> original) {
		int metadata = original.call(world, x, y, z);
		Block<?> block = world.getBlock(x, y, z);
		if (block != null && block.getLogic() instanceof BlockLogicSnowy) {
			return ((BlockLogicSnowy<?>) block.getLogic()).getRelativeLayers(metadata) - 1;
		}
		return metadata;
	}

	@WrapOperation(method = "accumulate", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;getBlockId(III)I"))
	private int blockId1(World world, int x, int y, int z, Operation<Integer> original) {
		int id = original.call(world, x, y, z);
		if (id == Blocks.LAYER_SNOW.id()) {
			return Blocks.LAYER_SNOW.id();
		}
		return id;
	}

	@WrapOperation(method = "canPlaceBlockAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/Block;isSolidRender()Z"))
	private boolean topSlabAndUpsideDownStairsFix(Block<?> block, Operation<Boolean> original, World world, int x, int y, int z) {
		int metadata = world.getBlockMetadata(x, y - 1, z);
		if (block != null && block.getLogic() instanceof BlockLogicSlab) {
			return (metadata & 3) != 0;
		}
		if (block != null && block.getLogic() instanceof BlockLogicStairs) {
			return (metadata & 8) != 0;
		}
		if(block != null && block.getLogic() instanceof BlockLogicSnowy){
			BlockLogicSnowy<?> snowy = (BlockLogicSnowy<?>) block.getLogic();
			return snowy.getLayers(metadata) >= snowy.getMaxLayers();
		}
		return original.call(block);
	}
}
