package net.helinos.moresnow.compat.aether.mixins.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.helinos.moresnow.mixins.MoreSnowMixin;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.WorldSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import teamport.aether.models.BlockModelGrassAether;

@Environment(EnvType.CLIENT)
@Mixin(value = BlockModelGrassAether.class, remap = false)
public class BlockModelFixingAetherGrass {

	@WrapOperation(method = "getBlockTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/WorldSource;getBlockMaterial(III)Lnet/minecraft/core/block/material/Material;"))
	private Material correctSnowTexture(WorldSource blockAccess, int x, int y, int z, Operation<Material> original) {
		return MoreSnowMixin.materialSpoofing(blockAccess, x, y, z, original);
	}

	@WrapOperation(method = "shouldSideBeColored", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/WorldSource;getBlockMaterial(III)Lnet/minecraft/core/block/material/Material;"))
	public Material correctColor(WorldSource blockAccess, int x, int y, int z, Operation<Material> original){
		return MoreSnowMixin.materialSpoofing(blockAccess, x, y, z, original);
	}
}
