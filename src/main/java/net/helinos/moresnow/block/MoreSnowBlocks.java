package net.helinos.moresnow.block;

import net.fabricmc.loader.api.FabricLoader;
import net.helinos.moresnow.MoreSnow;
import net.helinos.moresnow.block.logic.*;
import net.helinos.moresnow.mixins.mixin.accessor.BlockAccessor;
import net.minecraft.core.block.*;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.HardIllegalArgumentException;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.pos.ChunkTilePos;
import net.minecraft.core.world.pos.TilePos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import turniplabs.halplibe.helper.BlockBuilder;

import java.util.ArrayList;
import java.util.List;

import static net.helinos.moresnow.MoreSnow.*;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S2386", "java:S3008"})
public class MoreSnowBlocks {
	public static List<Block<? extends BlockLogicSnowy<?>>> SNOWY_FLOWERS = new ArrayList<>();
	public static List<Block<? extends BlockLogicSnowy<?>>> SNOWY_GRASS = new ArrayList<>();
	public static List<Block<? extends BlockLogicSnowy<?>>> SNOWY_SLAB = new ArrayList<>();
	public static List<Block<BlockLogicSnowySlabPainted<?>>> SNOWY_SLAB_PAINTED = new ArrayList<>();
	public static List<Block<? extends BlockLogicSnowy<?>>> SNOWY_STAIRS = new ArrayList<>();
	public static List<Block<BlockLogicSnowyStairsPainted<?>>> SNOWY_STAIRS_PAINTED = new ArrayList<>();
	public static List<Block<? extends BlockLogicSnowy<?>>> SNOWY_FENCE = new ArrayList<>();
	public static List<Block<BlockLogicSnowyFencePainted<?>>> SNOWY_FENCE_PAINTED = new ArrayList<>();
	public static List<Block<? extends BlockLogicSnowy<?>>> SNOWY_FENCE_GATE = new ArrayList<>();
	public static List<Block<BlockLogicSnowyFenceGatePainted<?>>> SNOWY_FENCE_GATES_PAINTED = new ArrayList<>();
	public static List<Block<? extends BlockLogicSnowy<?>>> SNOWY_FENCE_THIN = new ArrayList<>();

	public static Block<? extends BlockLogicSnowy<?>> SNOWY_FENCE_WALLPAPER;
	public static Block<? extends BlockLogicSnowy<?>> SNOWY_FENCE_STEEL;
	public static Block<? extends BlockLogicSnowy<?>> SNOWY_FENCE_CHAINLINK;

	public static Block<? extends BlockLogicSnowy<?>> LEAVY_FENCE_WALLPAPER;
	public static Block<? extends BlockLogicSnowy<?>> LEAVY_FENCE_STEEL;
	public static Block<? extends BlockLogicSnowy<?>> LEAVY_FENCE_CHAINLINK;

	public static Block<? extends BlockLogicSnowy<?>> SLATY_FENCE_WALLPAPER;
	public static Block<? extends BlockLogicSnowy<?>> SLATY_FENCE_STEEL;
	public static Block<? extends BlockLogicSnowy<?>> SLATY_FENCE_CHAINLINK;

	public static Block<? extends BlockLogicSnowy<?>> ASHY_FENCE_WALLPAPER;
	public static Block<? extends BlockLogicSnowy<?>> ASHY_FENCE_STEEL;
	public static Block<? extends BlockLogicSnowy<?>> ASHY_FENCE_CHAINLINK;

	private static boolean initialized = false;
	protected static int count = 0;
	private static final String UNFORMATTED_MESSAGE = "%6d \t %14s -> %s";
	private static final int STARTING_ID = 4500;
	private static int currentID = STARTING_ID;
	public static Tag<Block<?>> NOT_IN_CREATIVE_MENU = BlockTags.NOT_IN_CREATIVE_MENU;

	private MoreSnowBlocks() {
	}

	@SuppressWarnings("unchecked")
	public static void init() {
		if (initialized) return;
		initialized = true;
		LOGGER.info("Create Snowy variants of blocks.");
		if(FabricLoader.getInstance().isDevelopmentEnvironment()){
			NOT_IN_CREATIVE_MENU = BlockTags.OVERRIDE_STEPSOUND;
		}


		for (Block<? extends BlockLogic> block : Blocks.blocksList) {
			if (block == null) {
				continue;
			}
			BlockAccessor accessor = (BlockAccessor) (Object) block;
			BlockLogicSupplier supplier = accessor.getLogicSupplier();
			if (supplier == null || supplier.get(block) == null) {
				continue;
			}
			BlockLogic logic = supplier.get(block);
			for (Block<?> layerBlock : LAYERS) {
				String prefix = MoreSnow.LAYERS.getKey(layerBlock);
				createFlower(block, logic, prefix);
				createSlab(block, logic, prefix);
				createStairs(block, logic, prefix);
				createFence(block, logic, prefix);
				createFenceGate(block, logic, prefix);
				createFenceThinGeneral(block, logic, prefix);
			}
			createFenceThin(block, logic);
		}
		LOGGER.info("Finished creating snowy variants of blocks");
		LOGGER.info("Blocks created:{}", count);
	}

	public static int getNextID() {
		int current = currentID;
		currentID++;
		return current;
	}

	public static String convertNameSpaceID(NamespaceID blockID, String prefix) {
		String[] splitstring = blockID.value().split("/");
		return String.format(prefix, splitstring[splitstring.length - 1]);
	}

	public static void printMessage(int id, String blockType, @NotNull NamespaceID namespaceID) {
		if(FabricLoader.getInstance().isDevelopmentEnvironment()){
			String message = String.format(UNFORMATTED_MESSAGE, id, blockType, namespaceID);
			LOGGER.info(message);
		}
	}

	public static boolean convertBlock(World world, int id, int x, int y, int z, String prefix) {
		Block<?> replaceBlock = getBlock(id, world.getBlockData(new TilePos(x, y, z)), prefix);
		if (replaceBlock == null) return false;
		return ((BlockLogicSnowy<?>) replaceBlock.getLogic()).tryMakeSnowy(world, id, new TilePos(x, y, z));
	}

	public static boolean convertBlock(Chunk chunk, int id, int x, int y, int z, String prefix) {
		Block<?> replaceBlock = getBlock(id, chunk.getBlockData(new ChunkTilePos(x, y, z)), prefix);
		if (replaceBlock == null) return false;
		return ((BlockLogicSnowy<?>) replaceBlock.getLogic()).tryMakeSnowy(chunk, id, new TilePos(x, y, z));
	}

	public static @Nullable Block<?> getBlock(int id, int metadata, @Nullable String prefix) {
		if(prefix == null){
			return null;
		}
		Block<?> block = Blocks.getBlock(id);
		BlockLogic logic = block.getLogic();
		String name = "block/" + convertNameSpaceID(block.namespaceId(), prefix);
		if (logic instanceof IPainted paintedLogic) {
			DyeColor color = paintedLogic.fromMetadata(metadata);
			name = name + "_" + color.colorID;
		}
		NamespaceID namespaceID = NamespaceID.fromPool(getModID(logic), name);
		Block<?> replaceBlock = Blocks.blockMap.get(namespaceID);
		if (replaceBlock == null || !(replaceBlock.getLogic() instanceof BlockLogicSnowy)) {
			NamespaceID adjusted = NamespaceID.fromPool(MoreSnow.MOD_ID, name + "." + block.getLogic().namespaceId().namespace());
			Block<?> adjustedBlock = Blocks.blockMap.get(adjusted);
			if (adjustedBlock == null || !(adjustedBlock.getLogic() instanceof BlockLogicSnowy)) {
				return null;
			}
			replaceBlock = adjustedBlock;
		}
		return replaceBlock;
	}

	public static @NotNull String prePendName(Block<?> block, ItemStack itemStack, String prefix) {
		return LANGUAGE.translateKey(prefix + ".name") + " ";
	}

	public static @NotNull String prePendDesc(Block<?> block, ItemStack itemStack, String prefix) {
		String suffix = LANGUAGE.translateKey(block.getLogic().getLanguageKey(itemStack.getMetadata()) + ".name");
		return LANGUAGE.translateKey(prefix + ".desc") + " " + suffix.toLowerCase() + ".";
	}

	public static Block<?> getLayerBlock(Block<?> block) {
		NamespaceID namespaceId = block.namespaceId();
		String[] parts = namespaceId.value().split("[/_]");
		return parts.length > 2 ? LAYERS.getItem(parts[1]) : Blocks.LAYER_SNOW;
	}

	public static boolean tryMakeSnowy(World world, int id, int x, int y, int z, String prefix) {
		return convertBlock(world, id, x, y, z, prefix);
	}

	public static boolean tryMakeSnowy(Chunk chunk, int id, int x, int y, int z, String prefix) {
		return convertBlock(chunk, id, x, y, z, prefix);
	}

	public static boolean canConvert(Block<?> blockBelow, int metadata, String prefix) {
		return MoreSnowBlocks.getBlock(blockBelow.id(), metadata, prefix) != null;
	}

	public static String convertNameSpaceIDForBlockBuilder(NamespaceID blockID, String prefix) {
		String[] splitstring = blockID.value().split("/");
		String result = String.format(prefix, splitstring[splitstring.length - 1]);
		try {
			if(Blocks.blockMap.containsKey(NamespaceID.fromPool("moresnow:block/" + result))){
				result += "_" + blockID.namespace();
			}
		} catch (HardIllegalArgumentException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	public static @NotNull String getModID(BlockLogic logic) {
		return MOD_ID +  "." + logic.namespaceId().namespace();
	}

	static Tag<Block<?>>[] addTooling(Block<? extends BlockLogic> currentBlock) {
		List<Tag<Block<?>>> tag = new ArrayList<>();
		if (currentBlock.hasTag(BlockTags.MINEABLE_BY_PICKAXE)) {
			tag.add(BlockTags.MINEABLE_BY_PICKAXE);
		}
		if (currentBlock.hasTag(BlockTags.MINEABLE_BY_SHOVEL)) {
			tag.add(BlockTags.MINEABLE_BY_SHOVEL);
		}
		if (currentBlock.hasTag(BlockTags.MINEABLE_BY_AXE)) {
			tag.add(BlockTags.MINEABLE_BY_AXE);
		}
		if (currentBlock.hasTag(BlockTags.MINEABLE_BY_SWORD)) {
			tag.add(BlockTags.MINEABLE_BY_SWORD);
		}
		if (currentBlock.hasTag(BlockTags.MINEABLE_BY_SHEARS)) {
			tag.add(BlockTags.MINEABLE_BY_SHEARS);
		}
		if (currentBlock.hasTag(BlockTags.MINEABLE_BY_HOE)) {
			tag.add(BlockTags.MINEABLE_BY_HOE);
		}
		return tag.toArray(new Tag[0]);
	}

	static BlockBuilder addConnectTags(Block<? extends BlockLogic> currentBlock, BlockBuilder blockBuilder) {
		if (currentBlock.hasTag(BlockTags.FENCES_CONNECT)) {
			return blockBuilder.addTags(BlockTags.FENCES_CONNECT);
		}
		if (currentBlock.hasTag(BlockTags.CHAINLINK_FENCES_CONNECT)) {
			return blockBuilder.addTags(BlockTags.CHAINLINK_FENCES_CONNECT);
		}
		return blockBuilder;
	}

	/// BlockTags of Snow
	///	BlockTags.BROKEN_BY_FLUIDS, BlockTags.PLACE_OVERWRITES, BlockTags.MINEABLE_BY_SHOVEL, BlockTags.OVERRIDE_STEPSOUND
	/// BlockTags.BROKEN_BY_FLUIDS, BlockTags.PLANTABLE_IN_JAR, BlockTags.SHEEPS_FAVOURITE_BLOCK, BlockTags.SHEARS_DO_SILK_TOUCH
	public static void createFlowerStackable(Block<? extends BlockLogic> currentBlock, BlockLogic logic, String prefix) {
		if (logic instanceof BlockLogicFlowerStackable) {
			Block<?> layer = LAYERS.getItem(prefix);
			if(layer == null) return;
			Block<? extends BlockLogicSnowy<?>> snowy = new BlockBuilder(getModID(logic))
				.setBlockSound(layer.getSound())
				.setHardness(layer.getHardness())
				.setUseInternalLight()
				.addTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.OVERRIDE_STEPSOUND, NOT_IN_CREATIVE_MENU)
				.build(convertNameSpaceIDForBlockBuilder(currentBlock.namespaceId(), prefix + "_%s"), getNextID(), block -> new BlockLogicSnowyFlowerStackable<>(block, currentBlock));
			snowy.withTags(addTooling(layer));
			SNOWY_FLOWERS.add(snowy);
			printMessage(currentBlock.id(), "flower", snowy.namespaceId());
			count++;
		}
	}

	public static void createFlower(Block<? extends BlockLogic> currentBlock, BlockLogic logic, String prefix) {
		if ((logic instanceof BlockLogicFlower || logic instanceof BlockLogicSugarcane)) {
			Block<?> layer = LAYERS.getItem(prefix);
			if(layer == null) return;
			Block<? extends BlockLogicSnowy<?>> snowy = new BlockBuilder(getModID(logic))
				.setBlockSound(layer.getSound())
				.setHardness(layer.getHardness())
				.setUseInternalLight()
				.addTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.OVERRIDE_STEPSOUND, NOT_IN_CREATIVE_MENU)
				.build(convertNameSpaceIDForBlockBuilder(currentBlock.namespaceId(), prefix + "_%s"), getNextID(), block -> new BlockLogicSnowyFlowerStackable<>(block, currentBlock));
			snowy.withTags(addTooling(layer));
			SNOWY_FLOWERS.add(snowy);
			printMessage(currentBlock.id(), "flower", snowy.namespaceId());
			count++;
		}
	}

	/// BlockTags.SHEARS_DO_SILK_TOUCH, BlockTags.MINEABLE_BY_AXE, BlockTags.MINEABLE_BY_HOE, BlockTags.MINEABLE_BY_SWORD, BlockTags.MINEABLE_BY_SHEARS
	public static void createSapling(Block<? extends BlockLogic> currentBlock, BlockLogic logic, String prefix) {
		if (logic instanceof BlockLogicSaplingBase) {
			Block<?> layer = LAYERS.getItem(prefix);
			if(layer == null) return;
			Block<? extends BlockLogicSnowy<?>> snowy = new BlockBuilder(getModID(logic))
				.setBlockSound(layer.getSound())
				.setHardness(layer.getHardness())
				.setUseInternalLight()
				.addTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.OVERRIDE_STEPSOUND, NOT_IN_CREATIVE_MENU)
				.build(convertNameSpaceIDForBlockBuilder(currentBlock.namespaceId(), prefix + "_%s"), getNextID(), block -> new BlockLogicSnowyFlowerStackable<>(block, currentBlock));
			snowy.withTags(addTooling(layer));
			SNOWY_FLOWERS.add(snowy);
			printMessage(currentBlock.id(), "sapling", snowy.namespaceId());
			count++;
		}
	}

	public static void createMushrooms(Block<? extends BlockLogic> currentBlock, BlockLogic logic, String prefix) {
		if (logic instanceof BlockLogicMushroom) {
			Block<?> layer = LAYERS.getItem(prefix);
			if(layer == null) return;
			Block<? extends BlockLogicSnowy<?>> snowy = new BlockBuilder(getModID(logic))
				.setBlockSound(layer.getSound())
				.setHardness(layer.getHardness())
				.setUseInternalLight()
				.addTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.OVERRIDE_STEPSOUND, NOT_IN_CREATIVE_MENU)
				.build(convertNameSpaceIDForBlockBuilder(currentBlock.namespaceId(), prefix + "_%s"), getNextID(), block -> new BlockLogicSnowyFlowerStackable<>(block, currentBlock));
			snowy.withTags(addTooling(layer));
			SNOWY_FLOWERS.add(snowy);
			printMessage(currentBlock.id(), "mushroom", snowy.namespaceId());
			count++;
		}
	}

	public static void createSlab(Block<? extends BlockLogic> currentBlock, BlockLogic logic, String prefix) {
		if (logic instanceof BlockLogicSlab) {
			Block<?> layer = LAYERS.getItem(prefix);
			if(layer == null) return;
			Block<? extends BlockLogicSnowy<?>> snowy;
			BlockBuilder blockBuilder = new BlockBuilder(getModID(logic))
				.setBlockSound(layer.getSound())
				.setHardness(layer.getHardness())
				.setUseInternalLight()
				.setLightOpacity(1)
				.addTags(NOT_IN_CREATIVE_MENU);
			if (logic instanceof IPainted) {
				for (DyeColor color : DyeColor.values()) {
					String key = convertNameSpaceIDForBlockBuilder(currentBlock.namespaceId(), prefix + "_%s") + "_" + color.colorID;
					snowy = blockBuilder.build(key, getNextID(), block -> new BlockLogicSnowySlabPainted<>(block, currentBlock, color));
					snowy.withTags(addTooling(layer));
					SNOWY_SLAB_PAINTED.add((Block<BlockLogicSnowySlabPainted<?>>) snowy);
					printMessage(currentBlock.id(), "slab", snowy.namespaceId());
					count++;
				}
				return;
			}
			snowy = blockBuilder.build(convertNameSpaceIDForBlockBuilder(currentBlock.namespaceId(), prefix + "_%s"), getNextID(), block -> new BlockLogicSnowySlab<>(block, currentBlock));
			snowy.withTags(addTooling(layer));
			SNOWY_SLAB.add(snowy);
			printMessage(currentBlock.id(), "slab", snowy.namespaceId());
			count++;
		}
	}

	public static void createStairs(Block<? extends BlockLogic> currentBlock, BlockLogic logic, String prefix) {
		if (logic instanceof BlockLogicStairs) {
			Block<?> layer = LAYERS.getItem(prefix);
			if(layer == null) return;
			Block<? extends BlockLogicSnowy<?>> snowy;
			BlockBuilder blockBuilder = new BlockBuilder(getModID(logic))
				.setBlockSound(layer.getSound())
				.setHardness(layer.getHardness())
				.setUseInternalLight()
				.setLightOpacity(15)
				.addTags(NOT_IN_CREATIVE_MENU);
			if (logic instanceof IPainted) {
				for (DyeColor color : DyeColor.values()) {
					String key = convertNameSpaceIDForBlockBuilder(currentBlock.namespaceId(), prefix + "_%s") + "_" + color.colorID;
					snowy = blockBuilder.build(key, getNextID(), block -> new BlockLogicSnowyStairsPainted<>(block, currentBlock, color));
					snowy.withTags(addTooling(layer));
					SNOWY_STAIRS_PAINTED.add((Block<BlockLogicSnowyStairsPainted<?>>)snowy);
					printMessage(currentBlock.id(), "stairs", snowy.namespaceId());
					count++;
				}
				return;
			}
			snowy = blockBuilder.build(convertNameSpaceIDForBlockBuilder(currentBlock.namespaceId(), prefix + "_%s"), getNextID(), block -> new BlockLogicSnowyStairs<>(block, currentBlock));
			snowy.withTags(addTooling(layer));
			SNOWY_STAIRS.add(snowy);
			printMessage(currentBlock.id(), "stairs", snowy.namespaceId());
			count++;
		}
	}

	/// BlockTags.FENCES_CONNECT, BlockTags.MINEABLE_BY_AXE, BlockTags.CAN_HANG_OFF
	public static void createFence(Block<? extends BlockLogic> currentBlock, BlockLogic logic, String prefix) {
		if (logic instanceof BlockLogicFence) {
			Block<?> layer = LAYERS.getItem(prefix);
			if(layer == null) return;
			Block<? extends BlockLogicSnowy<?>> snowy;
			BlockBuilder blockBuilder = new BlockBuilder(getModID(logic))
				.setBlockSound(layer.getSound())
				.setHardness(layer.getHardness())
				.setUseInternalLight()
				.setLightOpacity(15)
				.addTags(NOT_IN_CREATIVE_MENU);
			blockBuilder = addConnectTags(currentBlock, blockBuilder);
			if (logic instanceof IPainted) {
				for (DyeColor color : DyeColor.values()) {
					String key = convertNameSpaceIDForBlockBuilder(currentBlock.namespaceId(), prefix + "_%s") + "_" + color.colorID;
					Block<BlockLogicSnowyFencePainted<?>> fence = blockBuilder.build(key, getNextID(), block -> new BlockLogicSnowyFencePainted<>(block, currentBlock, color));
					fence.withTags(addTooling(layer));
					SNOWY_FENCE_PAINTED.add(fence);
					printMessage(currentBlock.id(), "fence", fence.namespaceId());
					count++;
				}
				return;
			}
			snowy = blockBuilder.build(convertNameSpaceIDForBlockBuilder(currentBlock.namespaceId(), prefix + "_%s"), getNextID(), block -> new BlockLogicSnowyFence<>(block, currentBlock));
			snowy.withTags(addTooling(layer));
			SNOWY_FENCE.add(snowy);
			printMessage(currentBlock.id(), "fence", snowy.namespaceId());
			count++;
		}
	}

	public static void createFenceThinGeneral(Block<? extends BlockLogic> currentBlock, BlockLogic logic, String prefix) {
		if (logic instanceof BlockLogicFenceThin) {
			if (currentBlock.id() == Blocks.FENCE_CHAINLINK.id() || currentBlock.id() == Blocks.FENCE_STEEL.id() || currentBlock.id() == Blocks.FENCE_PAPER_WALL.id()) {
				return;
			}
			Block<?> layer = LAYERS.getItem(prefix);
			if(layer == null) return;
			Block<? extends BlockLogicSnowy<?>> snowy;
			BlockBuilder blockBuilder = new BlockBuilder(getModID(logic))
				.setHardness(layer.getHardness())
				.setUseInternalLight();
			blockBuilder = blockBuilder.addTags(BlockTags.CHAINLINK_FENCES_CONNECT);
			snowy = blockBuilder.build(convertNameSpaceIDForBlockBuilder(currentBlock.namespaceId(), prefix + "_%s"), getNextID(), (block) -> new BlockLogicSnowyFenceThin<>(block, currentBlock, BlockLogicFenceSteel.class));
			snowy.withTags(addTooling(layer));
			SNOWY_FENCE_THIN.add(snowy);
			printMessage(currentBlock.id(), "thin-fence", snowy.namespaceId());
			count++;
		}
	}

	@SuppressWarnings({"unchecked", "java:S1905"})
	public static void createFenceThin(Block<? extends BlockLogic> currentBlock, BlockLogic logic) {
		if (logic instanceof BlockLogicFenceThin) {

			BlockBuilder blockBuilder = new BlockBuilder(getModID(logic))
				.setUseInternalLight();
			blockBuilder = addConnectTags(currentBlock, blockBuilder);
			if (currentBlock.id() == Blocks.FENCE_STEEL.id()) {
				BlockBuilder fenceSteelBuilder = blockBuilder.addTags(BlockTags.OVERRIDE_STEPSOUND, BlockTags.CHAINLINK_FENCES_CONNECT, NOT_IN_CREATIVE_MENU);
				SNOWY_FENCE_STEEL = ((Block<? extends BlockLogicSnowy<?>>) (Block<?>) fenceSteelBuilder.build(convertNameSpaceIDForBlockBuilder(currentBlock.namespaceId(), "snow_%s"), getNextID(), BlockLogicSnowyFenceSteel::new))
					.withTags(addTooling(Blocks.LAYER_SNOW))
					.withSound(Blocks.LAYER_SNOW.getSound())
					.withHardness(Blocks.LAYER_SNOW.getHardness());
				printMessage(currentBlock.id(), "thin-fence", SNOWY_FENCE_STEEL.namespaceId());
				LEAVY_FENCE_STEEL = ((Block<? extends BlockLogicSnowy<?>>) (Block<?>) fenceSteelBuilder.build(convertNameSpaceIDForBlockBuilder(currentBlock.namespaceId(), "leaves_%s"), getNextID(), BlockLogicSnowyFenceSteel::new))
					.withTags(addTooling(Blocks.LAYER_LEAVES_OAK))
					.withSound(Blocks.LAYER_LEAVES_OAK.getSound())
					.withHardness(Blocks.LAYER_LEAVES_OAK.getHardness());
				printMessage(currentBlock.id(), "thin-fence", LEAVY_FENCE_STEEL.namespaceId());
				SLATY_FENCE_STEEL = ((Block<? extends BlockLogicSnowy<?>>) (Block<?>) fenceSteelBuilder.build(convertNameSpaceIDForBlockBuilder(currentBlock.namespaceId(), "slate_%s"), getNextID(), BlockLogicSnowyFenceSteel::new))
					.withTags(addTooling(Blocks.LAYER_SLATE))
					.withSound(Blocks.LAYER_SLATE.getSound())
					.withHardness(Blocks.LAYER_SLATE.getHardness());
				printMessage(currentBlock.id(), "thin-fence", SLATY_FENCE_STEEL.namespaceId());
				ASHY_FENCE_STEEL = ((Block<? extends BlockLogicSnowy<?>>) (Block<?>) fenceSteelBuilder.build(convertNameSpaceIDForBlockBuilder(currentBlock.namespaceId(), "ashe_%s"), getNextID(), BlockLogicSnowyFenceSteel::new))
					.withTags(addTooling(Blocks.LAYER_ASH))
					.withSound(Blocks.LAYER_ASH.getSound())
					.withHardness(Blocks.LAYER_ASH.getHardness());
				printMessage(currentBlock.id(), "thin-fence", ASHY_FENCE_STEEL.namespaceId());
				count += 4;
				return;
			}
			if (currentBlock.id() == Blocks.FENCE_CHAINLINK.id()) {
				BlockBuilder fenceChainBuilder = blockBuilder.addTags(BlockTags.OVERRIDE_STEPSOUND, BlockTags.CHAINLINK_FENCES_CONNECT, NOT_IN_CREATIVE_MENU);
				SNOWY_FENCE_CHAINLINK = ((Block<? extends BlockLogicSnowy<?>>) (Block<?>) fenceChainBuilder.build(convertNameSpaceIDForBlockBuilder(currentBlock.namespaceId(), "snow_%s"), getNextID(), BlockLogicSnowyFenceChainlink::new))
					.withTags(addTooling(Blocks.LAYER_SNOW))
					.withSound(Blocks.LAYER_SNOW.getSound())
					.withHardness(Blocks.LAYER_SNOW.getHardness());
				printMessage(currentBlock.id(), "thin-fence", SNOWY_FENCE_CHAINLINK.namespaceId());
				LEAVY_FENCE_CHAINLINK = ((Block<? extends BlockLogicSnowy<?>>) (Block<?>) fenceChainBuilder.build(convertNameSpaceIDForBlockBuilder(currentBlock.namespaceId(), "leaves_%s"), getNextID(), BlockLogicSnowyFenceChainlink::new))
					.withTags(addTooling(Blocks.LAYER_LEAVES_OAK))
					.withSound(Blocks.LAYER_LEAVES_OAK.getSound())
					.withHardness(Blocks.LAYER_LEAVES_OAK.getHardness());
				printMessage(currentBlock.id(), "thin-fence", LEAVY_FENCE_CHAINLINK.namespaceId());
				SLATY_FENCE_CHAINLINK = ((Block<? extends BlockLogicSnowy<?>>) (Block<?>) fenceChainBuilder.build(convertNameSpaceIDForBlockBuilder(currentBlock.namespaceId(), "slate_%s"), getNextID(), BlockLogicSnowyFenceChainlink::new))
					.withTags(addTooling(Blocks.LAYER_SLATE))
					.withSound(Blocks.LAYER_SLATE.getSound())
					.withHardness(Blocks.LAYER_SLATE.getHardness());
				printMessage(currentBlock.id(), "thin-fence", SLATY_FENCE_CHAINLINK.namespaceId());
				ASHY_FENCE_CHAINLINK = ((Block<? extends BlockLogicSnowy<?>>) (Block<?>) fenceChainBuilder.build(convertNameSpaceIDForBlockBuilder(currentBlock.namespaceId(), "ashe_%s"), getNextID(), BlockLogicSnowyFenceChainlink::new))
					.withTags(addTooling(Blocks.LAYER_ASH))
					.withSound(Blocks.LAYER_ASH.getSound())
					.withHardness(Blocks.LAYER_ASH.getHardness());
				printMessage(currentBlock.id(), "thin-fence", ASHY_FENCE_CHAINLINK.namespaceId());
				count += 4;
				return;
			}
			if (currentBlock.id() == Blocks.FENCE_PAPER_WALL.id()) {
				BlockBuilder fencePaperWallBuilder = blockBuilder.addTags(BlockTags.OVERRIDE_STEPSOUND, BlockTags.FENCES_CONNECT, NOT_IN_CREATIVE_MENU);
				SNOWY_FENCE_WALLPAPER = ((Block<? extends BlockLogicSnowy<?>>) (Block<?>) fencePaperWallBuilder.build(convertNameSpaceIDForBlockBuilder(currentBlock.namespaceId(), "snow_%s"), getNextID(), BlockLogicSnowyFenceWallPaper::new))
					.withTags(addTooling(Blocks.LAYER_SNOW))
					.withSound(Blocks.LAYER_SNOW.getSound())
					.withHardness(Blocks.LAYER_SNOW.getHardness());
				printMessage(currentBlock.id(), "thin-fence", SNOWY_FENCE_WALLPAPER.namespaceId());
				LEAVY_FENCE_WALLPAPER = ((Block<? extends BlockLogicSnowy<?>>) (Block<?>) fencePaperWallBuilder.build(convertNameSpaceIDForBlockBuilder(currentBlock.namespaceId(), "leaves_%s"), getNextID(), BlockLogicSnowyFenceWallPaper::new))
					.withTags(addTooling(Blocks.LAYER_LEAVES_OAK))
					.withSound(Blocks.LAYER_LEAVES_OAK.getSound())
					.withHardness(Blocks.LAYER_LEAVES_OAK.getHardness());
				printMessage(currentBlock.id(), "thin-fence", LEAVY_FENCE_WALLPAPER.namespaceId());
				SLATY_FENCE_WALLPAPER = ((Block<? extends BlockLogicSnowy<?>>) (Block<?>) fencePaperWallBuilder.build(convertNameSpaceIDForBlockBuilder(currentBlock.namespaceId(), "slate_%s"), getNextID(), BlockLogicSnowyFenceWallPaper::new))
					.withTags(addTooling(Blocks.LAYER_SLATE))
					.withSound(Blocks.LAYER_SLATE.getSound())
					.withHardness(Blocks.LAYER_SLATE.getHardness());
				printMessage(currentBlock.id(), "thin-fence", SLATY_FENCE_WALLPAPER.namespaceId());
				ASHY_FENCE_WALLPAPER = ((Block<? extends BlockLogicSnowy<?>>) (Block<?>) fencePaperWallBuilder.build(convertNameSpaceIDForBlockBuilder(currentBlock.namespaceId(), "ashe_%s"), getNextID(), BlockLogicSnowyFenceWallPaper::new))
					.withTags(addTooling(Blocks.LAYER_ASH))
					.withSound(Blocks.LAYER_ASH.getSound())
					.withHardness(Blocks.LAYER_ASH.getHardness());
				printMessage(currentBlock.id(), "thin-fence", ASHY_FENCE_WALLPAPER.namespaceId());
				count += 4;
			}
		}
	}

	///  BlockTags.FENCES_CONNECT, BlockTags.MINEABLE_BY_AXE
	public static void createFenceGate(Block<? extends BlockLogic> currentBlock, BlockLogic logic, String prefix) {
		if (logic instanceof BlockLogicFenceGate) {
			Block<?> layer = LAYERS.getItem(prefix);
			if(layer == null) return;
			Block<? extends BlockLogicSnowy<?>> snowy;
			printMessage(currentBlock.id(), "fence-gate", logic.namespaceId());
			BlockBuilder blockBuilder = new BlockBuilder(getModID(logic))
				.setBlockSound(layer.getSound())
				.setHardness(layer.getHardness())
				.setUseInternalLight()
				.setTags(NOT_IN_CREATIVE_MENU);
			blockBuilder = addConnectTags(currentBlock, blockBuilder);
			if (logic instanceof IPainted) {
				for (DyeColor color : DyeColor.values()) {
					String key = convertNameSpaceIDForBlockBuilder(currentBlock.namespaceId(), prefix + "_%s") + "_" + color.colorID;
					snowy = blockBuilder.build(key, getNextID(), block -> new BlockLogicSnowyFenceGatePainted(block, currentBlock, color));
					snowy.withTags(addTooling(layer));
					SNOWY_FENCE_GATES_PAINTED.add((Block<BlockLogicSnowyFenceGatePainted<?>>) snowy);
					count ++;
				}
				return;
			}
			snowy = blockBuilder.build(convertNameSpaceIDForBlockBuilder(currentBlock.namespaceId(), prefix + "_%s"), getNextID(), block -> new BlockLogicSnowyFenceGate<>(block, currentBlock));
			snowy.withTags(addTooling(currentBlock));
			SNOWY_FENCE_GATE.add(snowy);
			count ++;
		}
	}

	public static void createGrass(Block<? extends BlockLogic> currentBlock, BlockLogic logic, String prefix) {
		if (logic instanceof BlockLogicTallGrass) {
			Block<?> layer = LAYERS.getItem(prefix);
			if(layer == null) return;
			Block<? extends BlockLogicSnowy<?>> snowy = new BlockBuilder(getModID(logic))
				.setBlockSound(layer.getSound())
				.setHardness(layer.getHardness())
				.setUseInternalLight()
				.addTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.OVERRIDE_STEPSOUND, NOT_IN_CREATIVE_MENU)
				.build(convertNameSpaceIDForBlockBuilder(currentBlock.namespaceId(), prefix + "_%s"), getNextID(), block -> new BlockLogicSnowyFlowerStackable<>(block, currentBlock));
			snowy.withTags(addTooling(layer));
			SNOWY_GRASS.add(snowy);
			printMessage(currentBlock.id(), "grass", logic.namespaceId());
			count ++;
		}
	}
}
