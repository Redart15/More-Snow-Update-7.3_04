package net.helinos.moresnow.mixins.mixin.dep;

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

import java.util.Random;

@Mixin(value = WeatherSnow.class, remap = false)
public abstract class WeatherSnowMixin {

	@WrapOperation(method = "doEnvironmentUpdate", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I"))
	private int spoblockSnow(Random instance, int i, Operation<Integer> original){
		return 0;
	}

	@WrapOperation(method = "doEnvironmentUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;findTopSolidBlock(II)I"))
	private int findTopSolidBlockThatIsntSnowy(
		World world, int x, int z,
		Operation<Integer> original,
		@Share("yLevel")LocalIntRef yLevel,
		@Share("tilePos") LocalRef<TilePos> tilePosLocalRef
		) {
		int y = original.call(world, x, z);
		TilePos tilePos = new TilePos(x, y - 1, z);
		Block<?> blockBelow = world.getBlockType(tilePos);

		while (y > 0 && advanceBelow(blockBelow.getLogic())) {
			tilePos.down();
			blockBelow = world.getBlockType(tilePos);
		}
		tilePos.up();
		tilePosLocalRef.set(new TilePos(tilePos));
		yLevel.set(tilePos.y());
		return tilePos.y();
	}

	@Expression("? == 0")
	@ModifyExpressionValue(method = "doEnvironmentUpdate", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean changeIDs(
		boolean expression,
		@Local(argsOnly = true) World world,
		@Share("tilePos") LocalRef<TilePos> tilePosLocalRef
	) {
		TilePos tilePos = tilePosLocalRef.get();
		if (tilePos == null) {
			return expression;
		}
		Block<?> block = world.getBlockType(tilePos);
		return !MoreSnowBlocks.tryMakeSnowy(world, block.id(), tilePos.x(), tilePos.y(), tilePos.z(), "snow_%s");
	}

	@WrapOperation(method = "doEnvironmentUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/Block;canPlaceBlockAt(Lnet/minecraft/core/world/World;III)Z"))
	private boolean canTurnSnowy(
		Block<?> snowLayer, World world,
		int x, int y, int z,
		Operation<Boolean> original
	) {
		TilePos tilePos = new TilePos(x, y, z);
		Block<?> block = world.getBlockType(tilePos);
		int metadata = world.getBlockData(tilePos.down());
		if (MoreSnowBlocks.canConvert(block, metadata, "snow_%s")) {
			return true;
		}
		return original.call(snowLayer, world, x, y, z);
	}


	@Unique
	private static boolean advanceBelow(BlockLogic blockBelowLogic) {
		return blockBelowLogic instanceof BlockLogicSnowy<?> blockLogicSnowy
			&& !(blockLogicSnowy.getSupportsOwnSnow())
			|| blockBelowLogic instanceof BlockLogicFence
			|| blockBelowLogic instanceof BlockLogicFenceThin
			|| blockBelowLogic instanceof BlockLogicSugarcane
			|| blockBelowLogic instanceof BlockLogicFenceGate;
	}

	@WrapOperation(method = "doEnvironmentUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;setBlockWithNotify(IIII)Z", ordinal = 0))
	private boolean setSnowyBlock(
		World world, int x, int y, int z, int id,
		Operation<Boolean> original
	) {
		boolean makeSnowy = MoreSnowBlocks.tryMakeSnowy(world, id, x, y + 1, z, "snow_%s");
		if (makeSnowy) {
			return true;
		}
		int blockIdBelow = world.getBlockType(new TilePos(x, y, z)).id();
		boolean makeBelowSnowy = MoreSnowBlocks.tryMakeSnowy(world, blockIdBelow, x, y, z, "snow_%s");
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
		@Share("yLevel")LocalIntRef yLevel,
		@Local(argsOnly = true) World world,
		@Local(argsOnly = true, ordinal = 0) int x,
		@Local(argsOnly = true, ordinal = 1) int z
	) {
		int y = yLevel.get();
		TilePos tilePos = new TilePos(x, y, z);
		Block<?> block = world.getBlockType(tilePos);
		if(original){
			return true;
		}
		BlockLogic logic = block.getLogic();
		if(logic instanceof BlockLogicSnowy<?> logicSnowy){
			if(logicSnowy.layerBlock().id() == Blocks.LAYER_SNOW.id()){
				logicSnowy.accumulate(world, new TilePos(x, y, z));
			}
			return false;
		}
		return true;
	}

	@WrapOperation(method = "doChunkLoadEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;findTopSolidBlock(II)I"))
	private int findTopSolidBlockThatIsntSnowy(
		World world, int x, int z,
		Operation<Integer> original
	) {
		int y = original.call(world, x, z);
		TilePos tilePos = new TilePos(x, y - 1, z);
		Block<?> blockBelow = world.getBlockType(tilePos);

		while (y > 0 && advanceBelow(blockBelow.getLogic())) {
			tilePos.down();
			blockBelow = world.getBlockType(tilePos);
		}
		tilePos.up();
		return tilePos.y();
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
