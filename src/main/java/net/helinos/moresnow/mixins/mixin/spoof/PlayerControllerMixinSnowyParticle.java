package net.helinos.moresnow.mixins.mixin.spoof;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.helinos.moresnow.mixins.interfaces.BlockReplacement;
import net.minecraft.client.player.controller.PlayerController;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(value = PlayerController.class, remap = false)
public abstract class PlayerControllerMixinSnowyParticle {

	@WrapOperation(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;playBlockEvent(Lnet/minecraft/core/world/pos/TilePosc;II)V"))
	private void spoofBlockIDs(World instance, @NotNull TilePosc tilePos, int event, int id, Operation<Void> original){
		Block<?> block = Blocks.getBlock(id);
		if(block.getLogic() instanceof BlockLogicSnowy<?> logic){
			id = logic.layerBlock().id();
		}
		original.call(instance, tilePos, event, id);
	}

	@Inject(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;playBlockEvent(Lnet/minecraft/core/world/pos/TilePosc;II)V"))
	private void beforeDestroyedByPlayer(
		TilePosc tilePos,
		Side side,
		CallbackInfoReturnable<Boolean> cir,
		@Local World world,
		@Local Block<?> block,
		@Local int data,
		@Local Player player,
		@Local ItemStack itemStack,
		@Share("resultBlock")LocalRef<Block<?>> resultBlock
		){
		if(block.getLogic() instanceof BlockReplacement blockReplacement){
			resultBlock.set(blockReplacement.beforeDestroyedByPlayer(world, tilePos, side, data, player, itemStack));
		}
	}

	@WrapOperation(method = "destroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;setBlockTypeNotify(Lnet/minecraft/core/world/pos/TilePosc;Lnet/minecraft/core/block/Block;)Z"))
	private boolean replaceNotificationEvent(
		World instance,
		@NotNull TilePosc tilePosc,
		@NotNull Block<?> notifyBlock,
		Operation<Boolean> original,
		@Share("resultBlock")LocalRef<Block<?>> resultBlock
	){
		return original.call(instance, tilePosc, Objects.requireNonNullElse(resultBlock.get(), notifyBlock));
	}
}
