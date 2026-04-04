package net.helinos.moresnow.mixins.mixin.accessor;

import net.minecraft.core.block.Block;
import net.minecraft.core.item.block.ItemBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ItemBlock.class, remap = false)
public interface ItemBlockAccessor {
	@Accessor
	Block<?> getBlock();
}
