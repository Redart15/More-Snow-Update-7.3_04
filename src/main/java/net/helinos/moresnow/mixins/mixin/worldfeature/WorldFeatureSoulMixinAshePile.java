package net.helinos.moresnow.mixins.mixin.worldfeature;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.helinos.moresnow.MoreSnow;
import net.helinos.moresnow.block.MoreSnowBlocks;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeatureSoulCatcherPatch;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = WorldFeatureSoulCatcherPatch.class, remap = false)
public class WorldFeatureSoulMixinAshePile {


	@WrapOperation(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;setBlockType(Lnet/minecraft/core/world/pos/TilePosc;Lnet/minecraft/core/block/Block;)Z"))
	private boolean placeAshen(
		World world,
		@NotNull TilePosc tilePosc,
		@NotNull Block<?> soulcatcher,
		Operation<Boolean> original,
		@Local Block<?> surface
	){
		if(surface == Blocks.BLOCK_ASH || surface == Blocks.LAYER_ASH){
			return MoreSnowBlocks.tryMakeSnowy(world, Blocks.SOUL_CATCHER.id(), tilePosc.x(), tilePosc.y(), tilePosc.z(), MoreSnow.LAYERS.getKey(Blocks.LAYER_ASH) + "_%s");
		}
		return original.call(world, tilePosc, soulcatcher);
	}


}
