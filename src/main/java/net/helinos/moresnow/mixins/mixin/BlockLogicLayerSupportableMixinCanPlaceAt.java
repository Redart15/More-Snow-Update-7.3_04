package net.helinos.moresnow.mixins.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicLayerSupportable;
import net.minecraft.core.block.BlockLogicSlab;
import net.minecraft.core.block.BlockLogicStairs;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = BlockLogicLayerSupportable.class)
public class BlockLogicLayerSupportableMixinCanPlaceAt {
//
//	@WrapOperation(method = "canPlaceAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/Block;isSolidRender()Z"))
//	private boolean topSlabAndUpsideDownStairsFix(Block<?> block, Operation<Boolean> original, World world, int x, int y, int z) {
//		int metadata = world.getBlockMetadata(x, y - 1, z);
//		if (block != null && block.getLogic() instanceof BlockLogicSlab) {
//			return (metadata & 3) != 0;
//		}
//		if (block != null && block.getLogic() instanceof BlockLogicStairs) {
//			return (metadata & 8) != 0;
//		}
//		if(block != null && block.getLogic() instanceof BlockLogicSnowy<?> snowy){
//			return snowy.getLayers(metadata) >= snowy.getMaxLayers();
//		}
//		return original.call(block);
//	}
}
