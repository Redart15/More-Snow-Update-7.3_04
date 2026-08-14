package net.helinos.moresnow.mixins.mixin.accessor;

import net.minecraft.core.world.generate.feature.WorldFeatureNetherScatter;
import net.minecraft.core.world.pos.TilePos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldFeatureNetherScatter.class)
public interface WorldFeatureNetherScatterAccessor {
	@Accessor
	TilePos getScratchGround();

	@Accessor
	TilePos getScratchPlace();
}
