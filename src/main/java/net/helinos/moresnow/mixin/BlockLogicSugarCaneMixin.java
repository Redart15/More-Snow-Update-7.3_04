package net.helinos.moresnow.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicSugarcane;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = BlockLogicSugarcane.class, remap = false)
public class BlockLogicSugarCaneMixin {

	@WrapMethod(method = "canPlaceBlockAt")
	private boolean canSnowy(World world, int x, int y, int z, Operation<Boolean> original) {
		Block<?> block = world.getBlock(x, y - 1, z);
		if (block != null && block.getLogic() instanceof BlockLogicSnowy) {
			BlockLogicSugarcane asThis = (BlockLogicSugarcane) (Object) this;
			Block<?> storedBlock = ((BlockLogicSnowy) block.getLogic()).getStoredBlock();
			if (storedBlock.id() == asThis.id()) {
				return true;
			}
		}
		return original.call(world, x, y, z);
	}
}
