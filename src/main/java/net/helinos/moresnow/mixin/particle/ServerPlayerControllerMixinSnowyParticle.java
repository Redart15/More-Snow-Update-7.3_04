package net.helinos.moresnow.mixin.particle;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.helinos.moresnow.particle.SnowyParticleHelper;
import net.minecraft.core.entity.player.Player;
import net.minecraft.server.world.ServerPlayerController;
import net.minecraft.server.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ServerPlayerController.class, remap = false)
public abstract class ServerPlayerControllerMixinSnowyParticle {
	@WrapOperation(method = "mineBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/WorldServer;playBlockEvent(Lnet/minecraft/core/entity/player/Player;IIIII)V"))
	public void spoofBlockIDs(WorldServer instance, Player player, int event, int x, int y, int z, int id, Operation<Void> original){
		SnowyParticleHelper.spoofBlockIDs(instance, event, x, y, z, id, original);
	}
}
