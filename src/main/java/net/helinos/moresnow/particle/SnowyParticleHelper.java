package net.helinos.moresnow.particle;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;

public class SnowyParticleHelper {
	private SnowyParticleHelper(){}

	public static void spoofBlockIDs(World instance, int event, int x, int y, int z, int id, Operation<Void> original) {
		Block<?> block = Blocks.getBlock(id);
		if(block != null && block.getLogic() instanceof BlockLogicSnowy){
			id = Blocks.BLOCK_SNOW.id();
		}
		original.call(instance, event, x, y, z, id);
	}
}
