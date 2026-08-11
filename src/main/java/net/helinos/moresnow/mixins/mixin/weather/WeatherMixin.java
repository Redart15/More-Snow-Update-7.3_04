package net.helinos.moresnow.mixins.mixin.weather;

import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.BiomeTags;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.pos.ChunkPos;
import net.minecraft.core.world.pos.ChunkTilePos;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.weather.Weather;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(value = Weather.class, remap = false)
public abstract class WeatherMixin {
	@Inject(method = "doEnvironmentUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;getBlockId(III)I", shift = At.Shift.AFTER, ordinal = 1))
	private void doEnvironmentUpdate(World world, Random random, int x, int z, CallbackInfo ci) {
		int topY = world.getHeightValue(x, z);
		for (int y = topY; y >= topY - 1; y--) {
			TilePos tilePos = new TilePos(x, y, z);
			Block<?> block = world.getBlockType(tilePos);
			if (!(block.getLogic() instanceof BlockLogicSnowy<?> blockSnowy) || blockSnowy.layerBlock.id() != Blocks.LAYER_SNOW.id()) {
				continue;
			}
			int metadata = world.getBlockData(tilePos);
			int layers = blockSnowy.getLayers(metadata);
			if (layers > 1 && layers < blockSnowy.getRelativeLayers(metadata)) {
				world.setBlockData(tilePos, metadata - 1);
				world.markBlockNeedsUpdate(tilePos);
			} else if (!world.getBlockBiome(tilePos).hasTag(BiomeTags.HAS_SURFACE_SNOW)) {
				blockSnowy.removeSnow(world, metadata, tilePos);
			}
		}
	}

	@Inject(method = "doChunkLoadEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/chunk/Chunk;getBlockID(III)I", shift = At.Shift.AFTER, ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD)
	private void doChunkLoadEffect(World world, Chunk chunk, CallbackInfo callbackInfo, int x, int y, int z, int blockId) {
		Block<?> block = Blocks.getBlock(blockId);
		if (!(block.getLogic() instanceof BlockLogicSnowy<?> blockSnowy) || blockSnowy.layerBlock.id() != Blocks.LAYER_SNOW.id()) {
			return;
		}
		ChunkTilePos chunkTilePos = new ChunkTilePos(x, y, z);
		ChunkPos chunkPos = chunk.pos;
		int metadata = chunk.getBlockData(chunkTilePos);
		int layers = blockSnowy.getLayers(metadata);
		if (layers > 1 && world.getBlockBiome(new TilePos(chunkPos.x * 16 + x, y, chunkPos.z * 16 + z)).hasTag(BiomeTags.HAS_SURFACE_SNOW)) {
			chunk.setBlockData(chunkTilePos, metadata - (layers - 2));
		}
		blockSnowy.removeSnow(chunk, metadata, new TilePos(x, y, z));
	}
}
