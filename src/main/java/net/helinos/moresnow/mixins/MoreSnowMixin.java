package net.helinos.moresnow.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.minecraft.core.block.*;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;

import java.util.Random;

public class MoreSnowMixin {

	private MoreSnowMixin(){}

	public static Material materialSpoofing(WorldSource blockAccess, int x, int y, int z, Operation<Material> original) {
		Block<?> block = blockAccess.getBlock(x, y, z);
		Material material = original.call(blockAccess, x, y, z);
		if (block == null || !(block.getLogic() instanceof BlockLogicSnowy)) {
			return material;
		}
		BlockLogicSnowy<?> logic = (BlockLogicSnowy<?>) block.getLogic();
		if (logic.layerBlock.id() == Blocks.LAYER_SNOW.id() && !logic.getSupportsOwnSnow()) {
			return Material.topSnow;
		} else {
			return Material.stone;
		}
	}

    public static void spoofBlockIDs(World instance, int event, int x, int y, int z, int id, Operation<Void> original) {
        Block<?> block = Blocks.getBlock(id);
        if(block != null && block.getLogic() instanceof BlockLogicSnowy){
            BlockLogicSnowy logic = (BlockLogicSnowy) block.getLogic();
            id = logic.layerBlock.id();
        }
        original.call(instance, event, x, y, z, id);
    }

	public static boolean advanceBelow(World world, Random random, Block<?> blockBelow, BlockLogic blockBelowLogic, int x, int y, int z) {
		if(y < 0){
			return false;
		}

		// --- PASS THROUGH ---
		if (nonRekursiveCall(blockBelow, blockBelowLogic)) {
			return true;
		}

		// --- SNOW LAYER ---
//		if (blockBelow.id() == Blocks.LAYER_SNOW.id()) {
//			Block<?> furtherBelow = world.getBlock(x, y - 1, z);
//
//			if (furtherBelow == null) {
//				return true;
//			}
//			BlockLogic logic = furtherBelow.getLogic();
//			return logic instanceof BlockLogicLeavesBase || nonRekursiveCall(furtherBelow, logic);
//		}
//		// --- LEAVES ---
//		if (blockBelowLogic instanceof BlockLogicLeavesBase) {
//			Block<?> furtherUp = world.getBlock(x, y + 1, z);
//			if (furtherUp == null) {
//				return false;
//			}
//			BlockLogic logic = furtherUp.getLogic();
//			// unstable snowy
//			return furtherUp.id() == Blocks.LAYER_SNOW.id()            // already deposited
//				|| logic instanceof BlockLogicLeavesBase               // still in canopy
//				|| nonRekursiveCall(furtherUp, logic);                  // pass-through above
//		}

		return false;
	}

	private static boolean nonRekursiveCall(Block<?> blockBelow, BlockLogic blockBelowLogic){
		if(blockBelow == null){
			return true;
		}
		if((blockBelowLogic instanceof BlockLogicSnowy && !((BlockLogicSnowy) blockBelowLogic).getSupportsOwnSnow())){
			return true;
		}
		if(blockBelowLogic instanceof BlockLogicFence || blockBelowLogic instanceof BlockLogicFenceThin){
			return true;
		}
		return blockBelowLogic instanceof BlockLogicSugarcane;
	}
}
