package net.helinos.moresnow.mixins.mixin.layer;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.helinos.moresnow.MoreSnow;
import net.helinos.moresnow.block.MoreSnowBlocks;
import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicFire;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = BlockLogicFire.class, remap = false)
public abstract class BlockLogicFireMixinAccumulateAshe {

	@Shadow
	public abstract void initializeBlock();

	@WrapMethod(method = "tryPlaceAshLayer")
	private void tryPlaceSnowyAsheLayer(World world, TilePosc burntPos, Operation<Void> original) {
		TilePos probe = new TilePos(burntPos);
		for (int i = 0; i < 16 && probe.y > 0; --probe.y, ++i) {
			Block<?> blockAtProbe = world.getBlockType(probe);
			BlockLogic logic = blockAtProbe.getLogic();
			int data = world.getBlockData(probe);
			if (!(logic instanceof BlockLogicFire)) {
				if(
					logic instanceof BlockLogicSnowy<?> snowyLogic
					&& snowyLogic.layerBlock().id() == Blocks.LAYER_ASH.id()
				){
					int newLayers = snowyLogic.getLayers(data) + 1;
					if (newLayers <= snowyLogic.getMaxLayers()) {
						world.setBlockDataNotify(probe, data + 1);
					}
				}
				if (MoreSnowBlocks.tryMakeSnowy(world, blockAtProbe.id(), probe.x(), probe.y(), probe.z(), MoreSnow.LAYERS.getKey(Blocks.LAYER_ASH) + "_%s")) {
					return;
				}
			}
		}
		original.call(world, burntPos);
	}

	@WrapOperation(method = "isAshGround", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;isBlockOpaqueCube(Lnet/minecraft/core/world/pos/TilePosc;)Z"))
	private static boolean adjustIsAshGrounded(
		World world, TilePosc tilePos, Operation<Boolean> original
	){
		Block<?> block = world.getBlockType(tilePos);
		if(block.getLogic() instanceof BlockLogicSnowy<?> logicSnowy){
			return logicSnowy.getLayers(world.getBlockData(tilePos)) == logicSnowy.getMaxLayers();
		}
		return original.call(world, tilePos);
	}

}
