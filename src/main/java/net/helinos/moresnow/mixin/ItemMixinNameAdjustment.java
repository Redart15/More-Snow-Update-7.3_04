package net.helinos.moresnow.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.helinos.moresnow.MoreSnow;
import net.helinos.moresnow.block.init.MoreSnowBlocks;
import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.helinos.moresnow.mixin.accessor.ItemBlockAccessor;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.block.ItemBlock;
import org.spongepowered.asm.mixin.Mixin;

import static net.helinos.moresnow.block.init.MoreSnowBlocks.MOD_ID;


@Mixin(value = Item.class, remap = false)
public abstract class ItemMixinNameAdjustment {
	@WrapMethod(method = "getTranslatedName")
	private String adjustName(ItemStack itemstack, Operation<String> original){
		String result = original.call(itemstack);
		Item asThis = (Item) (Object) this;
		if(asThis instanceof ItemBlock && MOD_ID.equals(asThis.namespaceID.namespace())){
			Block<?> block = ((ItemBlockAccessor)asThis).getBlock();
			if(block != null && block.getLogic() != null && block.getLogic() instanceof BlockLogicSnowy){
				 result = MoreSnowBlocks.prePendName(block, itemstack, MoreSnow.LAYERS.getKey(((BlockLogicSnowy)block.getLogic()).layerBlock)) + result;
			}
		}
		return result;
	}

	@WrapMethod(method = "getTranslatedDescription")
	private String adjustDesc(ItemStack itemstack, Operation<String> original){
		Item asThis = (Item) (Object) this;
		if(asThis instanceof ItemBlock && MOD_ID.equals(asThis.namespaceID.namespace())){
			Block<?> block = ((ItemBlockAccessor)asThis).getBlock();
			if(block != null && block.getLogic() != null && block.getLogic() instanceof BlockLogicSnowy){
				return MoreSnowBlocks.prePendDesc(block, itemstack, MoreSnow.LAYERS.getKey(block));
			}
		}
		return original.call(itemstack);
	}
}
