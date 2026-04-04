package net.helinos.moresnow.mixins.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.render.block.model.BlockModelLayer;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.WorldSource;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = BlockModelLayer.class, remap = false)
public abstract class BlockModelLayerMixinFixSideRenderer {

	@SuppressWarnings("java:S107")
	@WrapMethod(method = "shouldSideBeRendered")
	public boolean adjustRenderingSide(WorldSource blockAccess, AABB bounds, int x, int y, int z, int side, int meta, Operation<Boolean> original){
		BlockModelLayer<?> asThis = (BlockModelLayer<?>) (Object) this;
		boolean bSide = side > 1;
		boolean bID = blockAccess.getBlockId(x, y, z) == asThis.block.id();
		boolean bMeta = blockAccess.getBlockMetadata(x, y, z) == meta;
		if (bSide && bID && bMeta) {
			return true;
		} else {
			return original.call(blockAccess, bounds, x, y, z, side, meta);
		}
	}
}
