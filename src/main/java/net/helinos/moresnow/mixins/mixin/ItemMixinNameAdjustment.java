package net.helinos.moresnow.mixins.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.helinos.moresnow.MoreSnow;
import net.helinos.moresnow.block.MoreSnowBlocks;
import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.helinos.moresnow.mixins.mixin.accessor.ItemBlockAccessor;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(value = Item.class, remap = false)
public abstract class ItemMixinNameAdjustment {
	@WrapMethod(method = "getTranslatedName")
	private String adjustName(ItemStack itemstack, Operation<String> original){
		String result = original.call(itemstack);
		Item asThis = (Item) (Object) this;
		if(asThis instanceof ItemBlock){
			Block<?> block = ((ItemBlockAccessor)asThis).getBlock();
			if(block != null && block.getLogic() instanceof BlockLogicSnowy){
				 result = MoreSnowBlocks.prePendName(block, itemstack, MoreSnow.LAYERS.getKey(((BlockLogicSnowy<?>)block.getLogic()).layerBlock)) + result;
			}
		}
		return result;
	}

	@WrapMethod(method = "getTranslatedDescription")
	private String adjustDesc(ItemStack itemstack, Operation<String> original){
		Item asThis = (Item) (Object) this;
		if(asThis instanceof ItemBlock){
			Block<?> block = ((ItemBlockAccessor)asThis).getBlock();
			if(block != null && block.getLogic() instanceof BlockLogicSnowy logicSnowy){
				return MoreSnowBlocks.prePendDesc(block, itemstack, MoreSnow.LAYERS.getKey(logicSnowy.layerBlock));
			}
		}
		return original.call(itemstack);
	}
}
