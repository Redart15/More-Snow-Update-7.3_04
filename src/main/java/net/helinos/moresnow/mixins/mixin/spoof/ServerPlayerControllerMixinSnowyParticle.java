package net.helinos.moresnow.mixins.mixin.spoof;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.helinos.moresnow.mixins.interfaces.BlockReplacement;
import net.helinos.moresnow.mixins.mixin.accessor.ServerPlayerControllerAccessor;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import net.minecraft.server.world.ServerPlayerController;
import net.minecraft.server.world.WorldServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ServerPlayerController.class, remap = false)
public abstract class ServerPlayerControllerMixinSnowyParticle {

	@WrapOperation(method = "mineBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/WorldServer;playBlockEvent(Lnet/minecraft/core/entity/player/Player;IIIII)V"))
	public void spoofBlockIDs(WorldServer instance, Player player, int event, int x, int y, int z, int id, Operation<Void> original){
		Block<?> block = Blocks.getBlock(id);
		if(block.getLogic() instanceof BlockLogicSnowy<?> logic){
			id = logic.layerBlock().id();
		}
		original.call(instance, player, x, y, z, event, id);
	}


	@Inject(method = "mineBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/WorldServer;playBlockEvent(Lnet/minecraft/core/entity/player/Player;IIIII)V"))
	private void beforeDestroyedByPlayer(
		int x, int y, int z,
		Side side,
		CallbackInfoReturnable<Boolean> cir,
		@Local Block<?> block,
		@Local ItemStack itemStack,
		@Share("resultBlock") LocalRef<Block<?>> resultBlock
	){
		ServerPlayerController controller = (ServerPlayerController) (Object) this;
		Player player = controller.player;
		World world = ((ServerPlayerControllerAccessor) controller).getThisWorld();
		TilePosc tilePos = new TilePos(x, y, z);
		int data = world.getBlockData(tilePos);
		if(block.getLogic() instanceof BlockReplacement blockReplacement){
			resultBlock.set(blockReplacement.beforeDestroyedByPlayer(world, tilePos, side, data, player, itemStack));
		}
	}

	@WrapOperation(method = "removeBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/WorldServer;setBlockWithNotify(IIII)Z"))
	private boolean replaceBlock(
		WorldServer instance,
		int x, int y, int z,
		int blockID,
		Operation<Boolean> original,
		@Share("resultBlock") LocalRef<Block<?>> resultBlock
	){
		Block<?> block = resultBlock.get();
		if(block == null){
			return original.call(instance, x, y, z, blockID);
		}
		return original.call(instance, x, y, z, block.id());
	}
}
