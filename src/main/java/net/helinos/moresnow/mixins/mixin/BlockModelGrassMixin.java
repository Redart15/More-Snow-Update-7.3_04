package net.helinos.moresnow.mixins.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.helinos.moresnow.mixins.MoreSnowMixin;
import net.minecraft.client.render.block.model.generic.BlockModelGenericGrass;
import net.minecraft.core.block.Block;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = BlockModelGenericGrass.class, remap = false)
public abstract class BlockModelGrassMixin {

	@WrapOperation(method = "getModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/WorldSource;getBlockType(Lnet/minecraft/core/world/pos/TilePosc;)Lnet/minecraft/core/block/Block;"))
	public @NotNull Block<?> correctColor(@NotNull WorldSource source, @NotNull TilePosc tilePosc, Operation<Block<?>> original){
		return MoreSnowMixin.materialSpoofing(source, tilePosc, original);
	}
}
