package net.helinos.moresnow.mixin.weather;

import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.weather.Weather;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

import static net.helinos.moresnow.MoreSnow.LAYERS;

@Mixin(value = Weather.class, remap = false)
public abstract class WeatherMixin {
	@Inject(method = "doEnvironmentUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;getBlockId(III)I", shift = At.Shift.AFTER, ordinal = 1))
	private void doEnvironmentUpdate(World world, Random random, int x, int z, CallbackInfo ci) {
		int topY = world.getHeightValue(x, z);
		for (int y = topY; y >= topY - 1; y--) {
			Block<?> block = world.getBlock(x, y, z);
			if (block == null || !(block.getLogic() instanceof BlockLogicSnowy)) {
				continue;
			}
			BlockLogicSnowy<?> blockSnowy = (BlockLogicSnowy<?>) block.getLogic();
			if(blockSnowy.layerBlock.id() != Blocks.LAYER_SNOW.id()){
				continue;
			}
			int metadata = world.getBlockMetadata(x, y, z);
			int layers = blockSnowy.getLayers(metadata);
			if (layers > 1 && layers < blockSnowy.getRelativeLayers(metadata)) {
				world.setBlockMetadata(x, y, z, metadata - 1);
				world.markBlockNeedsUpdate(x, y, z);
			} else if (!world.getBlockBiome(x, y, z).hasSurfaceSnow()) {
				blockSnowy.removeSnow(world, metadata, x, y, z);
			}
		}
	}

	@Inject(method = "doChunkLoadEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/chunk/Chunk;getBlockID(III)I", shift = At.Shift.AFTER, ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD)
	private void doChunkLoadEffect(World world, Chunk chunk, CallbackInfo callbackInfo, int x, int z, int y, int blockId) {
		Block<?> block = Blocks.getBlock(blockId);
		if (block == null || !(block.getLogic() instanceof BlockLogicSnowy)) {
			return;
		}
		BlockLogicSnowy<?> blockSnowy = (BlockLogicSnowy<?>) block.getLogic();
		if(blockSnowy.layerBlock.id() != Blocks.LAYER_SNOW.id()){
			return;
		}
		int metadata = chunk.getBlockMetadata(x, y, z);
		int layers = blockSnowy.getLayers(metadata);
		if (layers > 1 && world.getBlockBiome(chunk.xPosition * 16 + x, y, chunk.zPosition * 16 + z).hasSurfaceSnow()) {
			chunk.setBlockMetadata(x, y, z, metadata - (layers - 2));
		}
		blockSnowy.removeSnow(chunk, metadata, x, y, z);
	}
}
