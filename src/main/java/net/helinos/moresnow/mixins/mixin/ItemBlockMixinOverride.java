package net.helinos.moresnow.mixins.mixin;

import net.minecraft.core.item.block.ItemBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ItemBlock.class, remap = false)
public abstract class ItemBlockMixinOverride {


}
