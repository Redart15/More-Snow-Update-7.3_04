package net.helinos.moresnow.mixin.accessor;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Block.class)
public interface BlockAccessor {
	@Accessor("logicSupplier")
	BlockLogicSupplier<?> getLogicSupplier();
}
