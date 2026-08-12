package net.helinos.moresnow.mixins.mixin.sugarcane;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicSugarcane;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = BlockLogicSugarcane.class, remap = false)
public class BlockLogicSugarCaneMixin {

	@WrapMethod(method = "canPlaceAt")
	private boolean canSnowy(World world, TilePosc tilePos, Operation<Boolean> original) {
		TilePos queryPos = new TilePos();
		Block<?> block = world.getBlockType(tilePos.down(queryPos));
		BlockLogic logic = block.getLogic();
		if (logic instanceof BlockLogicSnowy<?> snowy) {
			BlockLogicSugarcane asThis = (BlockLogicSugarcane) (Object) this;
			if (snowy.storedBlock().id() == asThis.id()) {
				return true;
			}
		}
		return original.call(world, tilePos);
	}
}
