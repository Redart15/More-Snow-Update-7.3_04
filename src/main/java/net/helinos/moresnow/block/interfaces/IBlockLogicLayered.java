package net.helinos.moresnow.block.interfaces;

import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.helinos.moresnow.util.BlockMetadata;
import net.minecraft.core.block.*;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.block.support.FullSupport;
import net.minecraft.core.block.support.ISupport;
import net.minecraft.core.block.support.PartialSupport;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.enums.LightLayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.LevelListener;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.BiomeTags;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.pos.ChunkTilePos;
import net.minecraft.core.world.pos.ChunkTilePosc;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

/**
 * For Future use.
 * */
public interface IBlockLogicLayered {

	@NotNull Block<?> block();
	Block<?> layerBlock();
	Block<?> storedBlock();
	Block<?> storedBlock(int metadata);
	int storedBlockId(int metadata);
	int storedBlockMetadata(int metadata);

	default int blockToMetadata(int blockId, int metadata) {
		return 0;
	}
	default boolean canReplaceBlock(int id, int metadata) {
		return id == this.storedBlockId(metadata);
	}



	int maxLayers();
	int lowestLayerHeight();
	default int getLayers(int metadata) {
		return BlockMetadata.getLowerBlock(metadata) + 1;
	}
	default int getRelativeLayers(int metadata) {
		return this.getLayers(metadata) + this.lowestLayerHeight();
	}
	default int getRelativeMaxLayer(int metadata) {
		return this.maxLayers() + this.lowestLayerHeight();
	}
	/**
	 * @return How many layers a block at the given coordinates has, presuming the
	 * block is snowy or a layer block
	 */
	default int getOthersLayers(World world, TilePosc tilePosc) {
		Block<?> block = world.getBlockType(tilePosc);
		int metadata = world.getBlockData(tilePosc);

		if (block.getLogic() instanceof BlockLogicSnowy) {
			return ((BlockLogicSnowy<?>) block.getLogic()).getRelativeLayers(metadata);
		}
		return metadata;
	}

	default int convertBlockToMetadata(int blockId, int metadata) {
		return this.blockToMetadata(blockId, metadata);
	}
	default @NotNull String getLanguageKey(int meta) {
		return this.storedBlock().getLogic() instanceof BlockLogicSnowy ? "bug" : this.storedBlock().getLogic().getLanguageKey(meta);
	}
	default boolean canSupportSnow(World world, TilePosc tilePosc) {
		TilePosc posBelow = new TilePos(tilePosc.x(), tilePosc.y() - 1, tilePosc.z());
		Block<?> belowBlock = world.getBlockType(posBelow);
		int belowMetadata = world.getBlockData(posBelow);
		return this.canSupportSnow(belowBlock, belowMetadata);
	}
	default boolean canSupportSnow(Chunk chunk, TilePosc tilePosc) {
		ChunkTilePosc posBelow = new ChunkTilePos(tilePosc.x(), tilePosc.y() - 1, tilePosc.z());
		int belowID = chunk.getBlockId(posBelow);
		int belowMetadata = chunk.getBlockData(posBelow);
		Block<?> belowBlock = Blocks.getBlock(belowID);
		return this.canSupportSnow(belowBlock, belowMetadata);
	}

	default boolean canSupportSnow(@Nullable Block<?> belowBlock, int metadata) {
		if(this.getSupportsOwnSnow()){
			return true;
		}
		if (belowBlock == null) {
			return false;
		}
		Material belowMaterial = belowBlock.getMaterial();
		BlockLogic logic = belowBlock.getLogic();
		if (logic instanceof BlockLogicSnowy<?> snowyLogic && snowyLogic.getRelativeLayers(metadata) <= snowyLogic.getMaxLayers()) {
			return true;
		}

		if ((logic instanceof BlockLogicSlab && (metadata & 0b11) != 0) || (logic instanceof BlockLogicStairs && (metadata & 0b1000) != 0)) {
			return true;
		}
		if (belowBlock == Blocks.ICE || (!belowBlock.isSolidRender() && !(logic instanceof BlockLogicLeavesBase))) {
			return false;
		}
		return belowMaterial == Materials.LEAVES || belowMaterial.blocksMotion();
	}
	default boolean tryMakeSnowyCheck(World world, int id, TilePosc tilePos) {
		int meta = world.getBlockData(tilePos);
		return this.canReplaceBlock(id, meta) && canSupportSnow(world, tilePos);
	}
	default boolean tryMakeSnowyCheck(Chunk chunk, int id, TilePosc tilePos) {
		int meta = chunk.getBlockData(new ChunkTilePos(tilePos));
		return this.canReplaceBlock(id, meta) && canSupportSnow(chunk, tilePos);
	}
	/**
	 * Place a snow covered variant of a block at the given coordinates.
	 *
	 * @param id The block id to be "stored" inside the snow covered block
	 * @return Whether the block was placed successfully
	 */
	default boolean tryMakeSnowy(World world, int id, TilePosc tilePos) {
		int meta = world.getBlockData(tilePos);
		if (!this.canReplaceBlock(id, meta) || !canSupportSnow(world, tilePos))
			return false;
		return world.setBlockTypeDataNotify(tilePos, this.block(), this.blockToMetadata(id, meta));
	}

	/**
	 * Place a snow covered variant of a block at the given coordinates.
	 *
	 * @param id The block id to be "stored" inside the snow covered block
	 * @return Whether the block was placed successfully
	 */
	default boolean tryMakeSnowy(Chunk chunk, int id, TilePosc tilePos) {
		ChunkTilePos chunkTilePos = new ChunkTilePos(tilePos);
		int meta = chunk.getBlockData(chunkTilePos);
		if (!this.canReplaceBlock(id, meta) || !canSupportSnow(chunk, tilePos))
			return false;
		return chunk.setBlockIdData(chunkTilePos, this.block().id(), this.blockToMetadata(id, meta));
	}


	/**
	 * Remove the snow covered block and replace it with its actual block
	 *
	 * @param metadata The metadata of the snow covered block
	 * @see BlockLogicSnowy#removeSnow(World, int, TilePosc)
	 */
	default void removeSnow(World world, int metadata, TilePosc tilePos) {
		world.setBlockTypeDataNotify(tilePos, this.storedBlock(metadata), this.storedBlockMetadata(metadata));
	}
	/**
	 * Remove the snow covered block and replace it with its actual block
	 *
	 * @param metadata The metadata of the snow covered block
	 * @see BlockLogicSnowy#removeSnow(Chunk, int, TilePosc)
	 */
	default void removeSnow(Chunk chunk, int metadata, TilePosc tilePos) {
		chunk.setBlockIdData(new ChunkTilePos(tilePos), this.storedBlockId(metadata), this.storedBlockMetadata(metadata));
	}
	// Vanilla accumulate function but get layers from function rather than directly
	// from metadata

	default void accumulate(World world, TilePosc tilePos) {
		int x = tilePos.x();
		int y = tilePos.y();
		int z = tilePos.z();
		int metadata = world.getBlockData(new TilePos(x, y + 1, z));
		int layers = this.getLayers(metadata);
		if (layers >= this.maxLayers()) {
			return;
		}
		int relativeLayers = this.getRelativeLayers(metadata);
		if (!this.isBlockValid(world, new TilePos(x + 1, y + 1, z), relativeLayers)) {
			return;
		}
		if (!this.isBlockValid(world, new TilePos(x, y + 1, z + 1), relativeLayers)) {
			return;
		}
		if (!this.isBlockValid(world, new TilePos(x - 1, y + 1, z), relativeLayers)) {
			return;
		}
		if (!this.isBlockValid(world, new TilePos(x, y + 1, z - 1), relativeLayers)) {
			return;
		}
		world.setBlockDataNotify(new TilePos(x, y + 1, z), metadata + 1);
	}

	default boolean isBlockValid(World world, TilePosc tilePosc, int relativeLayers) {
		return world.isBlockOpaqueCube(tilePosc) || this.isSnow(world, tilePosc) && this.getOthersLayers(world, tilePosc) >= relativeLayers;
	}

	/**
	 * @return True if the block at the given coordinates is either snow covered or
	 * a snow layer
	 */
	default boolean isSnow(World world, TilePosc tilePosc) {
		int id = world.getBlockType(tilePosc).id();
		return id == Blocks.LAYER_SNOW.id();
	}


	 default void onDestroyedByPlayer(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Side side, int metadata, @NotNull Player player, @Nullable Item item) {
		this.removeSnow(world, metadata, tilePos);
	}

	default @NotNull ItemStack @Nullable [] getBreakResult(@NotNull World world, @NotNull EnumDropCause dropCause, @NotNull TilePosc tilePos, int metadata, @Nullable TileEntity tileEntity) {
		return switch (dropCause) {
			case SILK_TOUCH, PICK_BLOCK, PROPER_TOOL ->
				this.layerBlock().getBreakResult(world, dropCause, tilePos, this.getLayers(metadata) - 1, tileEntity);
			case IMPROPER_TOOL -> null;
			default -> // Drop the underlying block if it's destroyed by WORLD or EXPLOSION
				this.storedBlock().getBreakResult(world, dropCause, tilePos, this.storedBlockMetadata(metadata), tileEntity);
		};
	}

	default void updateTick(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Random rand, boolean isRandomTick){
		if (world.getSavedLightValue(LightLayer.Block, tilePos) > 11) {
			this.removeSnow(world, world.getBlockData(tilePos), tilePos);
		}
		if (shouldSnowMelt(world, tilePos) && this.layerBlock().id() == Blocks.LAYER_SNOW.id()) {
			this.removeSnow(world, world.getBlockData(tilePos), tilePos);
		}
	}

	static boolean shouldSnowMelt(World world, TilePosc tilePos) {
		return !world.getBlockBiome(tilePos).hasTag(BiomeTags.HAS_SURFACE_SNOW) &&
			world.getSeasonManager().getCurrentSeason() != null &&
			world.getSeasonManager().getCurrentSeason().letWeatherCleanUpSnow;
	}


	default void onNeighborChanged(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Block<?> block) {
		if (!this.canSupportSnow(world, tilePos)) {
			this.removeSnow(world, world.getBlockData(tilePos), tilePos);
			world.playBlockEvent(null, new TilePos(tilePos.x(), tilePos.y() - 1, tilePos.z()), LevelListener.EVENT_BLOCK_BREAK, this.layerBlock().id());
		}
	}

	default boolean getSupportsOwnSnow() {
		return true;
	}

	default @NotNull ISupport getSupport(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Side side) {
		return this.getLayers(world.getBlockData(tilePos)) != this.maxLayers() ? PartialSupport.INSTANCE : FullSupport.INSTANCE;
	}



}
