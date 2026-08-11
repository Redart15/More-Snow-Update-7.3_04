package net.helinos.moresnow.mixins.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.helinos.moresnow.block.MoreSnowBlocks;
import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.helinos.moresnow.mixins.mixin.accessor.BlockLogicAccessor;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Blocks.class, remap = false, priority = 2000)
public abstract class BlocksMixinAfterAllBlocks {

	@Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/Blocks;resetCaches()V", shift = At.Shift.AFTER))
	private static void initBlocks(CallbackInfo ci){
		MoreSnowBlocks.init();
	}

	@WrapOperation(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/BlockLogic;initializeBlock()V"))
	private static void fixMaterial(BlockLogic instance, Operation<Void> original){
		original.call(instance);
		if(instance instanceof BlockLogicSnowy<?> snowy && instance instanceof BlockLogicAccessor accessor){
			accessor.setMaterial(snowy.storedBlock.getMaterial());
		}
	}
}
