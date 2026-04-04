package net.helinos.moresnow.mixins.mixin.particle;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.helinos.moresnow.mixins.MoreSnowMixin;
import net.minecraft.client.player.controller.PlayerController;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = PlayerController.class, remap = false)
public abstract class PlayerControllerMixinSnowyParticle {

	@WrapOperation(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;playBlockEvent(IIIII)V"))
	public void spoofBlockIDs(World instance, int event, int x, int y, int z, int id, Operation<Void> original){
		MoreSnowMixin.spoofBlockIDs(instance, event, x, y, z, id, original);
	}
}
