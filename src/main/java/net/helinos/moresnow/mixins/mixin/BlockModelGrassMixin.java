package net.helinos.moresnow.mixins.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.helinos.moresnow.mixins.MoreSnowMixin;
import net.minecraft.client.render.block.model.BlockModelGrass;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.WorldSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = BlockModelGrass.class, remap = false)
public abstract class BlockModelGrassMixin {
	@WrapOperation(method = "getBlockTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/WorldSource;getBlockMaterial(III)Lnet/minecraft/core/block/material/Material;"))
	private Material correctSnowTexture(WorldSource blockAccess, int x, int y, int z, Operation<Material> original) {
		return MoreSnowMixin.materialSpoofing(blockAccess, x, y, z, original);
	}

	@WrapOperation(method = "shouldSideBeColored", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/WorldSource;getBlockMaterial(III)Lnet/minecraft/core/block/material/Material;"))
	public Material correctColor(WorldSource blockAccess, int x, int y, int z, Operation<Material> original){
		return MoreSnowMixin.materialSpoofing(blockAccess, x, y, z, original);
	}
}
