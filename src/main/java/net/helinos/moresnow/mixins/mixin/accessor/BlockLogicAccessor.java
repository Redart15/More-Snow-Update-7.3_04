package net.helinos.moresnow.mixins.mixin.accessor;

import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BlockLogic.class)
public interface BlockLogicAccessor {
	@Accessor
	void setMaterial(Material material);
}
