package net.helinos.moresnow.mixins.mixin.layer;

import com.llamalad7.mixinextras.sugar.Local;
import net.helinos.moresnow.MoreSnow;
import net.helinos.moresnow.block.MoreSnowBlocks;
import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicLeavesOak;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(value = BlockLogicLeavesOak.class, remap = false)
public class BlockLogicLeavesOakMixinAccumulateLeaves {

	@Inject(method = "updateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;getBlockType(Lnet/minecraft/core/world/pos/TilePosc;)Lnet/minecraft/core/block/Block;", shift = At.Shift.AFTER))
	private void depositeLeaves(
		World world,
		TilePosc tilePos,
		Random rand,
		boolean isRandomTick,
		CallbackInfo ci,
		@Local TilePos queryPos,
		@Local int q
	){
		Block<?> block = world.getBlockType(queryPos);
		int data = world.getBlockData(queryPos);
		if(
			block.getLogic() instanceof BlockLogicSnowy<?> snowyLogic
			&& snowyLogic.layerBlock().id() == Blocks.LAYER_LEAVES_OAK.id()
		){
			int newLayers = snowyLogic.getLayers(data) + 1;
			if (newLayers <= snowyLogic.getMaxLayers()) {
				world.setBlockDataNotify(queryPos, data + 1);
			}
			return;
		}
		MoreSnowBlocks.tryMakeSnowy(world, block.id(), queryPos.x(), queryPos.y(), queryPos.z(), MoreSnow.LAYERS.getKey(Blocks.LAYER_LEAVES_OAK) + "_%s");
	}

}
