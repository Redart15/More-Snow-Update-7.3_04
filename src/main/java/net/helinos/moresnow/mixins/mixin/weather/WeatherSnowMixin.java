package net.helinos.moresnow.mixins.mixin.weather;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.helinos.moresnow.block.MoreSnowBlocks;
import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.minecraft.core.block.*;
import net.minecraft.core.enums.LightLayer;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.pos.ChunkTilePos;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.weather.WeatherSnow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = WeatherSnow.class, remap = false)
public abstract class WeatherSnowMixin {

	@WrapOperation(method = "doEnvironmentUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;findTopSolidBlock(II)I"))
	private int findTopSolidBlockThatIsntSnowy(
		World world, int x, int z,
		Operation<Integer> original,
		@Share("block") LocalRef<Block<?>> blockLocalRef,
		@Share("blockBelow") LocalRef<Block<?>> blockBelowRef,
		@Share("yLevel")LocalIntRef yLevel
		) {
		int y = original.call(world, x, z);
		TilePos tilePos = new TilePos(x, y - 1, z);
		Block<?> blockBelow = world.getBlockType(tilePos);

		while (y < 0 && advanceBelow(blockBelow.getLogic())) {
			tilePos.down();
			blockBelow = world.getBlockType(tilePos);
		}
		blockBelowRef.set(world.getBlockType(tilePos));
		blockLocalRef.set(world.getBlockType(tilePos.up()));
		yLevel.set(tilePos.y());
		return tilePos.y();
	}

	@Unique
	private static boolean advanceBelow(BlockLogic blockBelowLogic) {
		return blockBelowLogic instanceof BlockLogicSnowy<?> blockLogicSnowy
			&& !(blockLogicSnowy.getSupportsOwnSnow())
			|| blockBelowLogic instanceof BlockLogicFence
			|| blockBelowLogic instanceof BlockLogicFenceThin
			|| blockBelowLogic instanceof BlockLogicSugarcane;
	}

	@WrapOperation(method = "doEnvironmentUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;setBlockWithNotify(IIII)Z", ordinal = 0))
	private boolean setSnowyBlock(World world, int x, int y, int z, int id, Operation<Boolean> original) {
		boolean makeSnowy = MoreSnowBlocks.tryMakeSnowy(world, id, x, y, z, "snow_%s");
		if (makeSnowy) {
			return true;
		}
		int blockIdBelow = world.getBlockType(new TilePos(x, y - 1, z)).id();
		boolean makeBelowSnowy = MoreSnowBlocks.tryMakeSnowy(world, blockIdBelow, x, y - 1, z, "snow_%s");
		if (makeBelowSnowy) {
			return true;
		}
		return original.call(world, x, y, z, Blocks.LAYER_SNOW.id());
	}

	@Definition(id = "snow", local = @Local(type = boolean.class))
	@Expression("snow")
	@ModifyExpressionValue(method = "doEnvironmentUpdate", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean modifyAccumulateLogic(
		boolean original,
		@Share("blockLocalRef") LocalRef<Block<?>> blockLocalRef,
		@Share("yLevel") LocalIntRef yLevel,
		@Local(argsOnly = true) World world,
		@Local(argsOnly = true, ordinal = 0) int x,
		@Local(argsOnly = true, ordinal = 1) int z
	) {
		if(!original){
			return false;
		}
		Block<?> block = blockLocalRef.get();
		if (block.id() == Blocks.LAYER_SNOW.id()) {
			return true;
		}
		BlockLogic logic = block.getLogic();
		if(logic instanceof BlockLogicSnowy<?> logicSnowy){
			if(logicSnowy.layerBlock.id() == Blocks.LAYER_SNOW.id()){
				logicSnowy.accumulate(world, new TilePos(x, yLevel.get() - 1, z));
			}
			return false;
		}
		return true;
	}


	@Inject(method = "doChunkLoadEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/chunk/Chunk;getBlockID(III)I", shift = At.Shift.AFTER, ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD)
	private void doChunkLoadEffect(
		World world, Chunk chunk, CallbackInfo callbackInfo,
		int x, int worldX, int z, int worldZ, int y, Biome biome, int blockId
	) {
		ChunkTilePos chunkTilePos = new ChunkTilePos(x, y, z);
		if (y < 0 || y >= world.getHeightBlocks()
			|| chunk.getLightLevel(LightLayer.Block, chunkTilePos) >= 10
			|| MoreSnowBlocks.tryMakeSnowy(chunk, blockId, x, y, z, "snow_%s")) {
			return;
		}
		int blockIDBelow = chunk.getBlockId(chunkTilePos);
		MoreSnowBlocks.tryMakeSnowy(chunk, blockIDBelow, x, y, z, "snow_%s");
	}
}
