package net.helinos.moresnow.mixins.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.render.block.model.BlockModelLayer;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePos;
import org.joml.primitives.AABBdc;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = BlockModelLayer.class, remap = false)
public abstract class BlockModelLayerMixinFixSideRenderer {

	@SuppressWarnings("java:S107")
	@WrapMethod(method = "shouldSideBeRendered")
	public boolean adjustRenderingSide(WorldSource source, AABBdc bounds, TilePos tilePos, Side side, int meta, Operation<Boolean> original){
		BlockModelLayer<?> asThis = (BlockModelLayer<?>) (Object) this;
		boolean bSide = !side.isVertical();
		boolean bID = source.getBlockType(tilePos).id() == asThis.block.id();
		boolean bMeta = source.getBlockData(tilePos) == meta;
		if (bSide && bID && bMeta) {
			return true;
		} else {
			return original.call(source, bounds, tilePos, side, meta);
		}
	}
}
