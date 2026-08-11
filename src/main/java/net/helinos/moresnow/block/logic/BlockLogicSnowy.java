package net.helinos.moresnow.block.logic;

import java.util.Random;

import net.helinos.moresnow.block.MoreSnowBlocks;
import net.helinos.moresnow.util.BlockMetadata;
import net.minecraft.core.block.material.Materials;
import net.minecraft.core.block.support.FullSupport;
import net.minecraft.core.block.support.ISupport;
import net.minecraft.core.block.support.PartialSupport;
import net.minecraft.core.world.LevelListener;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicLeavesBase;
import net.minecraft.core.block.BlockLogicSlab;
import net.minecraft.core.block.BlockLogicStairs;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.enums.LightLayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.BiomeTags;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.pos.ChunkTilePos;
import net.minecraft.core.world.pos.ChunkTilePosc;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BlockLogicSnowy<T extends BlockLogic> extends BlockLogic {
	public static final int START_INDEX = 4;
	public static final int END_INDEX = 7;
	public static final int FULL_BLOCK = 8;
	public final Block<?> storedBlock;
	public final Block<?> layerBlock;
	private final int maxLayers;
	private final int lowestLayerHeight;

	protected BlockLogicSnowy(Block<T> block, Block<?> storedBlock, int maxLayers, int lowestLayerHeight) {
//		this(block, storedBlock, MoreSnowBlocks.getLayerBlock(block), maxLayers, lowestLayerHeight, storedBlock.getMaterial());
		// default because game wont let us reference logic yet, we will fix these blocks later.
		this(block, storedBlock, MoreSnowBlocks.getLayerBlock(block), maxLayers, lowestLayerHeight, Materials.TOP_SNOW);
	}

	protected BlockLogicSnowy(Block<T> block, Block<?> storedBlock, Block<?> layerBlock, int maxLayers, int lowestLayerHeight, Material material) {
		super(block, material);
		this.layerBlock = layerBlock;
		this.storedBlock = storedBlock;
		this.maxLayers = maxLayers;
		this.lowestLayerHeight = lowestLayerHeight;
	}

	/**
	 * Check a given block id with given metadata is capable of being replaced by a
	 * snow covered block.
	 */
	public boolean canReplaceBlock(int id, int metadata) {
		return id == this.getStoredBlockId(metadata);
	}

	public Block<?> getStoredBlock() {
		return storedBlock;
	}

	public Block<?> getStoredBlock(int metadata) {
		return storedBlock;
	}

	@SuppressWarnings("java:S1172")
	public int getStoredBlockId(int metadata) {
		return this.storedBlock.id();
	}

	@SuppressWarnings("java:S1172")
	public int getStoredBlockMetadata(int metadata) {
		return 0;
	}

	public int convertBlockToMetadata(int blockId, int metadata) {
		return this.blockToMetadata(blockId, metadata);
	}

	@SuppressWarnings("java:S1172")
	protected int blockToMetadata(int blockId, int metadata) {
		return 0;
	}

	@Override
	public @NotNull String getLanguageKey(int meta) {
		return storedBlock.getLogic() instanceof BlockLogicSnowy ? "bug" : storedBlock.getLogic().getLanguageKey(meta);
	}

	public int getLayers(int metadata) {
		return BlockMetadata.getLowerBlock(metadata) + 1;
	}

	public int getMaxLayers() {
		return this.maxLayers;
	}

	public int getRelativeLayers(int metadata) {
		return this.getLayers(metadata) + this.lowestLayerHeight;
	}

	public int getRelativeMaxLayer(int metadata) {
		return this.maxLayers + this.lowestLayerHeight;
	}

	public boolean canSupportSnow(World world, TilePosc tilePosc) {
		TilePosc posBelow = new TilePos(tilePosc.x(), tilePosc.y() - 1, tilePosc.z());
		Block<?> belowBlock = world.getBlockType(posBelow);
		int belowMetadata = world.getBlockData(posBelow);
		return this.canSupportSnow(belowBlock, belowMetadata);
	}

	public boolean canSupportSnow(Chunk chunk, TilePosc tilePosc) {
		ChunkTilePosc posBelow = new ChunkTilePos(tilePosc.x(), tilePosc.y() - 1, tilePosc.z());
		int belowID = chunk.getBlockId(posBelow);
		int belowMetadata = chunk.getBlockData(posBelow);
		Block<?> belowBlock = Blocks.getBlock(belowID);
		return this.canSupportSnow(belowBlock, belowMetadata);
	}

	private boolean canSupportSnow(@Nullable Block<?> belowBlock, int metadata) {
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

	public boolean tryMakeSnowyCheck(World world, int id, TilePosc tilePos) {
		int meta = world.getBlockData(tilePos);
		return this.canReplaceBlock(id, meta) && canSupportSnow(world, tilePos);
	}

	public boolean tryMakeSnowyCheck(Chunk chunk, int id, TilePosc tilePos) {
		int meta = chunk.getBlockData(new ChunkTilePos(tilePos));
		return this.canReplaceBlock(id, meta) && canSupportSnow(chunk, tilePos);
	}

	/**
	 * Place a snow covered variant of a block at the given coordinates.
	 *
	 * @param id The block id to be "stored" inside the snow covered block
	 * @return Whether the block was placed successfully
	 */
	public boolean tryMakeSnowy(World world, int id, TilePosc tilePos) {
		int meta = world.getBlockData(tilePos);
		if (!this.canReplaceBlock(id, meta) || !canSupportSnow(world, tilePos))
			return false;
		return world.setBlockTypeDataNotify(tilePos, this.block, this.blockToMetadata(id, meta));
	}

	/**
	 * Place a snow covered variant of a block at the given coordinates.
	 *
	 * @param id The block id to be "stored" inside the snow covered block
	 * @return Whether the block was placed successfully
	 */
	public boolean tryMakeSnowy(Chunk chunk, int id, TilePosc tilePos) {
		ChunkTilePos chunkTilePos = new ChunkTilePos(tilePos);
		int meta = chunk.getBlockData(chunkTilePos);
		if (!this.canReplaceBlock(id, meta) || !canSupportSnow(chunk, tilePos))
			return false;
		return chunk.setBlockIdData(chunkTilePos, this.block.id(), this.blockToMetadata(id, meta));
	}

	/**
	 * Remove the snow covered block and replace it with its actual block
	 *
	 * @param metadata The metadata of the snow covered block
	 * @see BlockLogicSnowy#removeSnow(World, int, TilePosc)
	 */
	public void removeSnow(World world, int metadata, TilePosc tilePos) {
		world.setBlockTypeDataNotify(tilePos, this.getStoredBlock(metadata), this.getStoredBlockMetadata(metadata));
	}

	/**
	 * Remove the snow covered block and replace it with its actual block
	 *
	 * @param metadata The metadata of the snow covered block
	 * @see BlockLogicSnowy#removeSnow(Chunk, int, TilePosc)
	 */
	public void removeSnow(Chunk chunk, int metadata, TilePosc tilePos) {
		chunk.setBlockIdData(new ChunkTilePos(tilePos), this.getStoredBlockId(metadata), this.getStoredBlockMetadata(metadata));
	}

	// Vanilla accumulate function but get layers from function rather than directly
	// from metadata
	public void accumulate(World world, TilePosc tilePos) {
		int x = tilePos.x();
		int y = tilePos.y();
		int z = tilePos.z();
		int metadata = world.getBlockData(new TilePos(x, y + 1, z));
		int layers = this.getLayers(metadata);
		if (layers >= this.getMaxLayers()) {
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


	private boolean isBlockValid(World world, TilePosc tilePosc, int relativeLayers) {
		return world.isBlockOpaqueCube(tilePosc) || this.isSnow(world, tilePosc) && this.getOthersLayers(world, tilePosc) >= relativeLayers;
	}

	/**
	 * @return True if the block at the given coordinates is either snow covered or
	 * a snow layer
	 */
	private boolean isSnow(World world, TilePosc tilePosc) {
		int id = world.getBlockType(tilePosc).id();
		return id == Blocks.LAYER_SNOW.id();
	}

	/**
	 * @return How many layers a block at the given coordinates has, presuming the
	 * block is snowy or a layer block
	 */
	private int getOthersLayers(World world, TilePosc tilePosc) {
		Block<?> block = world.getBlockType(tilePosc);
		int metadata = world.getBlockData(tilePosc);

		if (block.getLogic() instanceof BlockLogicSnowy) {
			return ((BlockLogicSnowy<?>) block.getLogic()).getRelativeLayers(metadata);
		}
		return metadata;
	}


	@Override
	public void onDestroyedByPlayer(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Side side, int metadata, @NotNull Player player, @Nullable Item item) {
		this.removeSnow(world, metadata, tilePos);
	}

	@Override
	public @NotNull ItemStack @Nullable [] getBreakResult(@NotNull World world, @NotNull EnumDropCause dropCause, @NotNull TilePosc tilePos, int metadata, @Nullable TileEntity tileEntity) {
		return switch (dropCause) {
			case SILK_TOUCH, PICK_BLOCK, PROPER_TOOL ->
				this.layerBlock.getBreakResult(world, dropCause, tilePos, this.getLayers(metadata) - 1, tileEntity);
			case IMPROPER_TOOL -> null;
			default -> // Drop the underlying block if it's destroyed by WORLD or EXPLOSION
				storedBlock.getBreakResult(world, dropCause, tilePos, this.getStoredBlockMetadata(metadata), tileEntity);
		};
	}

	@Override
	public void updateTick(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Random rand, boolean isRandomTick){
		if (world.getSavedLightValue(LightLayer.Block, tilePos) > 11) {
			this.removeSnow(world, world.getBlockData(tilePos), tilePos);
		}
		if (shouldSnowMelt(world, tilePos) && this.layerBlock.id() == Blocks.LAYER_SNOW.id()) {
			this.removeSnow(world, world.getBlockData(tilePos), tilePos);
		}
	}

	private static boolean shouldSnowMelt(World world, TilePosc tilePos) {
		return !world.getBlockBiome(tilePos).hasTag(BiomeTags.HAS_SURFACE_SNOW) &&
			world.getSeasonManager().getCurrentSeason() != null &&
			world.getSeasonManager().getCurrentSeason().letWeatherCleanUpSnow;
	}


	@Override
	public void onNeighborChanged(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Block<?> block) {
		if (!this.canSupportSnow(world, tilePos)) {
			this.removeSnow(world, world.getBlockData(tilePos), tilePos);
			world.playBlockEvent(null, new TilePos(tilePos.x(), tilePos.y() - 1, tilePos.z()), LevelListener.EVENT_BLOCK_BREAK, this.layerBlock.id());
		}
	}

	public boolean getSupportsOwnSnow() {
		return true;
	}

	public @NotNull ISupport getSupport(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Side side) {
		return this.getLayers(world.getBlockData(tilePos)) != this.getMaxLayers() ? PartialSupport.INSTANCE : FullSupport.INSTANCE;
	}
}
