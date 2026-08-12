package net.helinos.moresnow.mixins.mixin.blocks;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.minecraft.client.render.block.model.generic.BlockModelGenericGrass;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = BlockModelGenericGrass.class, remap = false)
public abstract class BlockModelGrassMixin {

	@WrapOperation(method = "getModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/WorldSource;getBlockType(Lnet/minecraft/core/world/pos/TilePosc;)Lnet/minecraft/core/block/Block;"))
	public @NotNull Block<?> correctColor(@NotNull WorldSource source, @NotNull TilePosc tilePosc, Operation<Block<?>> original){
		Block<?> block = source.getBlockType(tilePosc);
		if (!(block.getLogic() instanceof BlockLogicSnowy<?> logic)) {
			return block;
		}
		if (logic.layerBlock().id() == Blocks.LAYER_SNOW.id() && !logic.getSupportsOwnSnow()) {
			return Blocks.LAYER_SNOW;
		} else {
			return original.call(source, tilePosc);
		}
	}
}
