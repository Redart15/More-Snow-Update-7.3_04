package net.helinos.moresnow.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.minecraft.core.block.*;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;

public class MoreSnowMixin {

	private MoreSnowMixin(){}

	public static Block<?> materialSpoofing(WorldSource blockAccess, @NotNull TilePosc tilePos, Operation<Block<?>> original) {
		Block<?> block = blockAccess.getBlockType(tilePos);
		if (!(block.getLogic() instanceof BlockLogicSnowy<?> logic)) {
			return block;
		}
		if (logic.layerBlock.id() == Blocks.LAYER_SNOW.id() && !logic.getSupportsOwnSnow()) {
			return Blocks.LAYER_SNOW;
		} else {
			return original.call(blockAccess, tilePos);
		}
	}

    public static void spoofBlockIDs(World instance, int event, @NotNull TilePosc tilePos, int id, Operation<Void> original) {
        Block<?> block = Blocks.getBlock(id);
        if(block.getLogic() instanceof BlockLogicSnowy<?> logic){
			id = logic.layerBlock.id();
        }
        original.call(instance, tilePos, event, id);
    }
}
