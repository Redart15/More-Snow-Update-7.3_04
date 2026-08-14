package net.helinos.moresnow.mixins.mixin.worldfeature;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.helinos.moresnow.MoreSnow;
import net.helinos.moresnow.block.MoreSnowBlocks;
import net.helinos.moresnow.mixins.mixin.accessor.WorldFeatureNetherScatterAccessor;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeatureNetherScatter;
import net.minecraft.core.world.generate.feature.WorldFeatureNetherScatterBonePile;
import net.minecraft.core.world.pos.TilePos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = WorldFeatureNetherScatterBonePile.class, remap = false)
public abstract class WorldFeatureBonePileMixinAshenPile {

	@WrapOperation(method = "place", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/generate/feature/WorldFeatureNetherScatterBonePile;placeIfCanStay(Lnet/minecraft/core/world/World;Lnet/minecraft/core/block/Block;Lnet/minecraft/core/world/pos/TilePos;)Z"))
	private boolean alterBlock(
		WorldFeatureNetherScatterBonePile instance,
		World world,
		Block<?> bonepile,
		TilePos tilePos,
		Operation<Boolean> original
	){
		WorldFeatureNetherScatterAccessor feature = (WorldFeatureNetherScatterAccessor) (WorldFeatureNetherScatter) instance;
		Block<?> surface = world.getBlockType(feature.getScratchGround());
		if(surface == Blocks.BLOCK_ASH || surface == Blocks.LAYER_ASH){
			return MoreSnowBlocks.tryMakeSnowy(world, bonepile.id(), tilePos.x(), tilePos.y(), tilePos.z(), MoreSnow.LAYERS.getKey(Blocks.LAYER_ASH) + "_%s");
		}
		return original.call(instance, world, bonepile, tilePos);
	}
}
