package net.helinos.moresnow.mixins.mixin.weather;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.helinos.moresnow.block.MoreSnowBlocks;
import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.minecraft.core.block.*;
import net.minecraft.core.enums.LightLayer;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.pos.ChunkTilePos;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import net.minecraft.core.world.weather.WeatherSnow;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(value = WeatherSnow.class, remap = false)
public abstract class WeatherSnowMixin {

	@Definition(id = "rand", local = @Local(type = Random.class, argsOnly = true))
	@Definition(id = "nextInt", method = "Ljava/util/Random;nextInt(I)I")
	@Definition(id = "probability", local = @Local(type = int.class, ordinal = 2))
	@Expression("rand.nextInt(probability) == 0")
	@ModifyExpressionValue(method = "doEnvironmentUpdate", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean spoofed(boolean original){
		return true;
	}

	@WrapOperation(method = "doEnvironmentUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;findTopSolidBlock(II)I"))
	private int findTopSolidBlockThatIsntSnowyWorld(
		World world, int x, int z,
		Operation<Integer> original,
		@Share("yLevel")LocalIntRef yLevel
	) {
		int y = original.call(world, x, z);
		TilePos tilePos = new TilePos(x, y - 1, z);
		y = getTopSolidBlock(world, tilePos, y);
		yLevel.set(y);
		return y;
	}

	@Unique
	private static int getTopSolidBlock(@NotNull World world, TilePos tilePos, int y) {
		Block<?> blockBelow = world.getBlockType(tilePos);
		while (y > 0 && advanceBelow(blockBelow.getLogic())) {
			tilePos.down();
			blockBelow = world.getBlockType(tilePos);
		}
		return tilePos.up().y();
	}

	@Unique
	private static boolean advanceBelow(BlockLogic blockBelowLogic) {
		return blockBelowLogic instanceof BlockLogicSnowy<?> blockLogicSnowy
			&& !(blockLogicSnowy.getSupportsOwnSnow())
			|| blockBelowLogic instanceof BlockLogicFence
			|| blockBelowLogic instanceof BlockLogicFenceThin
			|| blockBelowLogic instanceof BlockLogicSugarcane
			|| blockBelowLogic instanceof BlockLogicFenceGate
			|| blockBelowLogic.block == Blocks.AIR;
	}

	@Inject(method = "doEnvironmentUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;getBlockId(III)I", shift = At.Shift.AFTER, ordinal = 1))
	private void doEnvironmentUpdate(
		World world, Random random,
		int x, int z, CallbackInfo ci,
		@Local boolean snow,
		@Share("yLevel")LocalIntRef yLevel
	) {
		int y = yLevel.get();
		TilePosc tilePosc = new TilePos(x, y, z);
		Biome biome = world.getBlockBiome(tilePosc);
		Block<?> blockBelow = world.getBlockType(new TilePos(tilePosc));
		WeatherSnow asThis = (WeatherSnow) (Object) this;
		if (biome.blockedWeathers.contains(asThis)
			|| world.getWeatherManager().getWeatherPower() <= 0.6F
			|| y < 0
			|| y >= world.getHeightBlocks()
			|| world.getSavedLightValue(LightLayer.Block, tilePosc) >= 10) {
			return;
		}
		if(snow){
			MoreSnowBlocks.tryMakeSnowy(world, blockBelow.id(), x, y, z, "snow_%s");
		}
	}

	@WrapOperation(method = "doChunkLoadEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;findTopSolidBlock(II)I"))
	private int findTopSolidBlockThatIsntSnowyChunk(
		World world, int x, int z,
		Operation<Integer> original,
		@Share("yLevel")LocalIntRef yLevel
	) {
		int y = original.call(world, x, z);
		TilePos tilePos = new TilePos(x, y - 1, z);
		y = getTopSolidBlock(world, tilePos, y);
		yLevel.set(y);
		return y;
	}

	@Inject(method = "doChunkLoadEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/chunk/Chunk;getBlockID(III)I", shift = At.Shift.AFTER, ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD)
	private void doChunkLoadEffect(
		World world, Chunk chunk, CallbackInfo callbackInfo,
		int x, int worldX, int z, int worldZ, int ny, Biome biome, int blockId,
		@Share("yLevel")LocalIntRef yLevel
	) {
		int y = yLevel.get();
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
