package net.helinos.moresnow.block;

import net.helinos.moresnow.MoreSnow;
import net.helinos.moresnow.block.logic.*;
import net.helinos.moresnow.block.logic.BlockLogicSnowyFenceGatePainted;
import net.helinos.moresnow.block.logic.BlockLogicSnowyFencePainted;
import net.helinos.moresnow.block.logic.BlockLogicSnowySlabPainted;
import net.helinos.moresnow.block.logic.BlockLogicSnowyStairsPainted;
import net.minecraft.core.block.*;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.util.HardIllegalArgumentException;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.util.helper.DyeColor;
import org.jspecify.annotations.NonNull;
import turniplabs.halplibe.helper.BlockBuilder;

import java.util.ArrayList;
import java.util.List;

import static net.helinos.moresnow.MoreSnow.MOD_ID;
import static net.helinos.moresnow.block.MoreSnowBlocks.*;

@SuppressWarnings({"java:S1144"})
public class MoreSnowBlockInitializer {
	private MoreSnowBlockInitializer() {
	}

	private static BlockBuilder addConnectTags(Block<? extends BlockLogic> currentBlock, BlockBuilder blockBuilder) {
		if (currentBlock.hasTag(BlockTags.FENCES_CONNECT)) {
			return blockBuilder.addTags(BlockTags.FENCES_CONNECT);
		}
		if (currentBlock.hasTag(BlockTags.CHAINLINK_FENCES_CONNECT)) {
			return blockBuilder.addTags(BlockTags.CHAINLINK_FENCES_CONNECT);
		}
		return blockBuilder;
	}

	private static Tag[] addTooling(Block<? extends BlockLogic> currentBlock) {
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

	public static @NonNull String getModID(BlockLogic logic) {
		return MOD_ID +  "." + logic.namespaceId().namespace();
	}

	public static String convertNameSpaceID(NamespaceID blockID, String prefix) {
		String[] splitstring = blockID.value().split("/");
		String result = String.format(prefix, splitstring[splitstring.length - 1]);
		try {
			if(Blocks.blockMap.containsKey(NamespaceID.getPermanent("moresnow:block/" + result))){
				result += "_" + blockID.namespace();
			}
		} catch (HardIllegalArgumentException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	/// BlockTags of Snow
	///	BlockTags.BROKEN_BY_FLUIDS, BlockTags.PLACE_OVERWRITES, BlockTags.MINEABLE_BY_SHOVEL, BlockTags.OVERRIDE_STEPSOUND


	///    BlockTags.BROKEN_BY_FLUIDS, BlockTags.PLANTABLE_IN_JAR, BlockTags.SHEEPS_FAVOURITE_BLOCK, BlockTags.SHEARS_DO_SILK_TOUCH
	public static void createFlowerStackable(Block<? extends BlockLogic> currentBlock, BlockLogic logic, String prefix) {
		if (logic instanceof BlockLogicFlowerStackable) {
			Block<?> layer = MoreSnow.LAYERS.getItem(prefix);
			Block<?> snowy = new BlockBuilder(getModID(logic))
				.setBlockSound(layer.getSound())
				.setHardness(layer.getHardness())
				.setUseInternalLight()
				.setVisualUpdateOnMetadata()
				.addTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.OVERRIDE_STEPSOUND, NOT_IN_CREATIVE_MENU)
				.build(convertNameSpaceID(currentBlock.namespaceId(), prefix + "_%s"), getNextID(), block -> new BlockLogicSnowyFlowerStackable<>(block, currentBlock));
			snowy.withTags(addTooling(layer));
			SNOWY_FLOWERS.add(snowy);
			printMessage(currentBlock.id(), "flower", logic.namespaceId());
			count++;
		}
	}

	public static void createFlower(Block<? extends BlockLogic> currentBlock, BlockLogic logic, String prefix) {
		if ((logic instanceof BlockLogicFlower || logic instanceof BlockLogicSugarcane)) {
			Block<?> layer = MoreSnow.LAYERS.getItem(prefix);
			Block<?> snowy = new BlockBuilder(getModID(logic))
				.setBlockSound(layer.getSound())
				.setHardness(layer.getHardness())
				.setUseInternalLight()
				.setVisualUpdateOnMetadata()
				.addTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.OVERRIDE_STEPSOUND, NOT_IN_CREATIVE_MENU)
				.build(convertNameSpaceID(currentBlock.namespaceId(), prefix + "_%s"), getNextID(), block -> new BlockLogicSnowyFlowerStackable<>(block, currentBlock));
			snowy.withTags(addTooling(layer));
			SNOWY_FLOWERS.add(snowy);
			printMessage(currentBlock.id(), "flower", logic.namespaceId());
			count++;
		}
	}

	/// BlockTags.SHEARS_DO_SILK_TOUCH, BlockTags.MINEABLE_BY_AXE, BlockTags.MINEABLE_BY_HOE, BlockTags.MINEABLE_BY_SWORD, BlockTags.MINEABLE_BY_SHEARS
	public static void createSapling(Block<? extends BlockLogic> currentBlock, BlockLogic logic, String prefix) {
		if (logic instanceof BlockLogicSaplingBase) {
			Block<?> layer = MoreSnow.LAYERS.getItem(prefix);
			Block<?> snowy = new BlockBuilder(getModID(logic))
				.setBlockSound(layer.getSound())
				.setHardness(layer.getHardness())
				.setUseInternalLight()
				.setVisualUpdateOnMetadata()
				.addTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.OVERRIDE_STEPSOUND, NOT_IN_CREATIVE_MENU)
				.build(convertNameSpaceID(currentBlock.namespaceId(), prefix + "_%s"), getNextID(), block -> new BlockLogicSnowyFlowerStackable<>(block, currentBlock));
			snowy.withTags(addTooling(layer));
			SNOWY_FLOWERS.add(snowy);
			printMessage(currentBlock.id(), "sapling", logic.namespaceId());
			count++;
		}
	}

	public static void createMushrooms(Block<? extends BlockLogic> currentBlock, BlockLogic logic, String prefix) {
		if (logic instanceof BlockLogicMushroom) {
			Block<?> layer = MoreSnow.LAYERS.getItem(prefix);
			Block<?> snowy = new BlockBuilder(getModID(logic))
				.setBlockSound(layer.getSound())
				.setHardness(layer.getHardness())
				.setUseInternalLight()
				.setVisualUpdateOnMetadata()
				.addTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.OVERRIDE_STEPSOUND, NOT_IN_CREATIVE_MENU)
				.build(convertNameSpaceID(currentBlock.namespaceId(), prefix + "_%s"), getNextID(), block -> new BlockLogicSnowyFlowerStackable<>(block, currentBlock));
			snowy.withTags(addTooling(layer));
			SNOWY_FLOWERS.add(snowy);
			printMessage(currentBlock.id(), "mushroom", logic.namespaceId());
			count++;
		}
	}

	public static void createSlab(Block<? extends BlockLogic> currentBlock, BlockLogic logic, String prefix) {
		if (logic instanceof BlockLogicSlab) {
			Block<?> layer = MoreSnow.LAYERS.getItem(prefix);
			Block<?> snowy;
			printMessage(currentBlock.id(), "slab", logic.namespaceId());
			BlockBuilder blockBuilder = new BlockBuilder(getModID(logic))
				.setBlockSound(layer.getSound())
				.setHardness(layer.getHardness())
				.setUseInternalLight()
				.setLightOpacity(1)
				.setVisualUpdateOnMetadata()
				.addTags(NOT_IN_CREATIVE_MENU);
			if (logic instanceof IPainted) {
				for (DyeColor color : DyeColor.values()) {
					String key = convertNameSpaceID(currentBlock.namespaceId(), prefix + "_%s") + "_" + color.colorID;
					snowy = blockBuilder.build(key, getNextID(), block -> new BlockLogicSnowySlabPainted<>(block, currentBlock, color));
					snowy.withTags(addTooling(layer));
					SNOWY_SLAB_PAINTED.add(snowy);
					count++;
				}
				return;
			}
			snowy = blockBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), prefix + "_%s"), getNextID(), block -> new BlockLogicSnowySlab<>(block, currentBlock));
			snowy.withTags(addTooling(layer));
			SNOWY_SLAB.add(snowy);
			count++;
		}
	}

	public static void createStairs(Block<? extends BlockLogic> currentBlock, BlockLogic logic, String prefix) {
		if (logic instanceof BlockLogicStairs) {
			Block<?> layer = MoreSnow.LAYERS.getItem(prefix);
			Block<?> snowy;
			printMessage(currentBlock.id(), "stairs", logic.namespaceId());
			BlockBuilder blockBuilder = new BlockBuilder(getModID(logic))
				.setBlockSound(layer.getSound())
				.setHardness(layer.getHardness())
				.setUseInternalLight()
				.setLightOpacity(15)
				.setVisualUpdateOnMetadata()
				.addTags(NOT_IN_CREATIVE_MENU);
			if (logic instanceof IPainted) {
				for (DyeColor color : DyeColor.values()) {
					String key = convertNameSpaceID(currentBlock.namespaceId(), prefix + "_%s") + "_" + color.colorID;
					snowy = blockBuilder.build(key, getNextID(), block -> new BlockLogicSnowyStairsPainted<>(block, currentBlock, color));
					snowy.withTags(addTooling(layer));
					SNOWY_STAIRS_PAINTED.add(snowy);
					count++;
				}
				return;
			}
			snowy = blockBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), prefix + "_%s"), getNextID(), block -> new BlockLogicSnowyStairs<>(block, currentBlock));
			snowy.withTags(addTooling(layer));
			SNOWY_STAIRS.add(snowy);
			count++;
		}
	}

	/// BlockTags.FENCES_CONNECT, BlockTags.MINEABLE_BY_AXE, BlockTags.CAN_HANG_OFF
	public static void createFence(Block<? extends BlockLogic> currentBlock, BlockLogic logic, String prefix) {
		if (logic instanceof BlockLogicFence) {
			Block<?> layer = MoreSnow.LAYERS.getItem(prefix);
			Block<?> snowy;
			printMessage(currentBlock.id(), "fence", logic.namespaceId());
			BlockBuilder blockBuilder = new BlockBuilder(getModID(logic))
				.setBlockSound(layer.getSound())
				.setHardness(layer.getHardness())
				.setUseInternalLight()
				.setLightOpacity(15)
				.setVisualUpdateOnMetadata()
				.addTags(NOT_IN_CREATIVE_MENU);
			blockBuilder = addConnectTags(currentBlock, blockBuilder);
			if (logic instanceof IPainted) {
				for (DyeColor color : DyeColor.values()) {
					String key = convertNameSpaceID(currentBlock.namespaceId(), prefix + "_%s") + "_" + color.colorID;
					Block<BlockLogicSnowyFencePainted<?>> fence = blockBuilder.build(key, getNextID(), block -> new BlockLogicSnowyFencePainted<>(block, currentBlock, color));
					fence.withTags(addTooling(layer));
					SNOWY_FENCE_PAINTED.add(fence);
					count++;
				}
				return;
			}
			snowy = blockBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), prefix + "_%s"), getNextID(), block -> new BlockLogicSnowyFence<>(block, currentBlock));
			snowy.withTags(addTooling(layer));
			SNOWY_FENCE.add(snowy);
			count++;
		}
	}

	public static void createFenceThinGeneral(Block<? extends BlockLogic> currentBlock, BlockLogic logic, String prefix) {
		if (logic instanceof BlockLogicFenceThin) {
			printMessage(currentBlock.id(), "thin-fence", logic.namespaceId());
			if (currentBlock.id() == Blocks.FENCE_CHAINLINK.id() || currentBlock.id() == Blocks.FENCE_STEEL.id() || currentBlock.id() == Blocks.FENCE_PAPER_WALL.id()) {
				return;
			}
			Block<?> layer = MoreSnow.LAYERS.getItem(prefix);
			Block<?> snowy;
			BlockBuilder blockBuilder = new BlockBuilder(getModID(logic))
				.setHardness(layer.getHardness())
				.setUseInternalLight()
				.setVisualUpdateOnMetadata();
			blockBuilder = blockBuilder.addTags(BlockTags.CHAINLINK_FENCES_CONNECT);
			snowy = blockBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), prefix + "_%s"), getNextID(), (block) -> new BlockLogicSnowyFenceThin(block, currentBlock, BlockLogicFenceSteel.class));
			snowy.withTags(addTooling(layer));
			SNOWY_FENCE_THIN.add(snowy);
			count++;
		}
	}

	public static void createFenceThin(Block<? extends BlockLogic> currentBlock, BlockLogic logic) {
		if (logic instanceof BlockLogicFenceThin) {
			printMessage(currentBlock.id(), "thin-fence", logic.namespaceId());
			BlockBuilder blockBuilder = new BlockBuilder(getModID(logic))
				.setUseInternalLight()
				.setVisualUpdateOnMetadata();
			blockBuilder = addConnectTags(currentBlock, blockBuilder);
			if (currentBlock.id() == Blocks.FENCE_STEEL.id()) {
				BlockBuilder fenceSteelBuilder = blockBuilder.addTags(BlockTags.OVERRIDE_STEPSOUND, BlockTags.CHAINLINK_FENCES_CONNECT, NOT_IN_CREATIVE_MENU);
				SNOWY_FENCE_STEEL = fenceSteelBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), "snow_%s"), getNextID(), BlockLogicSnowyFenceSteel::new)
					.withTags(addTooling(Blocks.LAYER_SNOW))
					.withSound(Blocks.LAYER_SNOW.getSound())
					.withHardness(Blocks.LAYER_SNOW.getHardness());
				LEAVY_FENCE_STEEL = fenceSteelBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), "leaves_%s"), getNextID(), BlockLogicSnowyFenceSteel::new)
					.withTags(addTooling(Blocks.LAYER_LEAVES_OAK))
					.withSound(Blocks.LAYER_LEAVES_OAK.getSound())
					.withHardness(Blocks.LAYER_LEAVES_OAK.getHardness());
				SLATY_FENCE_STEEL = fenceSteelBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), "slate_%s"), getNextID(), BlockLogicSnowyFenceSteel::new)
					.withTags(addTooling(Blocks.LAYER_SLATE))
					.withSound(Blocks.LAYER_SLATE.getSound())
					.withHardness(Blocks.LAYER_SLATE.getHardness());
				count += 3;
				return;
			}
			if (currentBlock.id() == Blocks.FENCE_CHAINLINK.id()) {
				BlockBuilder fenceChainBuilder = blockBuilder.addTags(BlockTags.OVERRIDE_STEPSOUND, BlockTags.CHAINLINK_FENCES_CONNECT, NOT_IN_CREATIVE_MENU);
				SNOWY_FENCE_CHAINLINK = fenceChainBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), "snow_%s"), getNextID(), BlockLogicSnowyFenceChainlink::new)
					.withTags(addTooling(Blocks.LAYER_SNOW))
					.withSound(Blocks.LAYER_SNOW.getSound())
					.withHardness(Blocks.LAYER_SNOW.getHardness());
				LEAVY_FENCE_CHAINLINK = fenceChainBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), "leaves_%s"), getNextID(), BlockLogicSnowyFenceChainlink::new)
					.withTags(addTooling(Blocks.LAYER_LEAVES_OAK))
					.withSound(Blocks.LAYER_LEAVES_OAK.getSound())
					.withHardness(Blocks.LAYER_LEAVES_OAK.getHardness());
				SLATY_FENCE_CHAINLINK = fenceChainBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), "slate_%s"), getNextID(), BlockLogicSnowyFenceChainlink::new)
					.withTags(addTooling(Blocks.LAYER_SLATE))
					.withSound(Blocks.LAYER_SLATE.getSound())
					.withHardness(Blocks.LAYER_SLATE.getHardness());
				count += 3;
				return;
			}
			if (currentBlock.id() == Blocks.FENCE_PAPER_WALL.id()) {
				BlockBuilder fencePaperWallBuilder = blockBuilder.addTags(BlockTags.OVERRIDE_STEPSOUND, BlockTags.FENCES_CONNECT, NOT_IN_CREATIVE_MENU);
				SNOWY_FENCE_WALLPAPER = fencePaperWallBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), "snow_%s"), getNextID(), BlockLogicSnowyFenceWallPaper::new)
					.withTags(addTooling(Blocks.LAYER_SNOW))
					.withSound(Blocks.LAYER_SNOW.getSound())
					.withHardness(Blocks.LAYER_SNOW.getHardness());
				LEAVY_FENCE_WALLPAPER = fencePaperWallBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), "leaves_%s"), getNextID(), BlockLogicSnowyFenceWallPaper::new)
					.withTags(addTooling(Blocks.LAYER_LEAVES_OAK))
					.withSound(Blocks.LAYER_LEAVES_OAK.getSound())
					.withHardness(Blocks.LAYER_LEAVES_OAK.getHardness());
				SLATY_FENCE_WALLPAPER = fencePaperWallBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), "slate_%s"), getNextID(), BlockLogicSnowyFenceWallPaper::new)
					.withTags(addTooling(Blocks.LAYER_SLATE))
					.withSound(Blocks.LAYER_SLATE.getSound())
					.withHardness(Blocks.LAYER_SLATE.getHardness());
				count += 3;
			}
		}
	}

	///  BlockTags.FENCES_CONNECT, BlockTags.MINEABLE_BY_AXE
	public static void createFenceGate(Block<? extends BlockLogic> currentBlock, BlockLogic logic, String prefix) {
		if (logic instanceof BlockLogicFenceGate) {
			Block<?> layer = MoreSnow.LAYERS.getItem(prefix);
			Block<?> snowy;
			printMessage(currentBlock.id(), "fence-gate", logic.namespaceId());
			BlockBuilder blockBuilder = new BlockBuilder(getModID(logic))
				.setBlockSound(layer.getSound())
				.setHardness(layer.getHardness())
				.setUseInternalLight()
				.setVisualUpdateOnMetadata()
				.setTags(NOT_IN_CREATIVE_MENU);
			blockBuilder = addConnectTags(currentBlock, blockBuilder);
			if (logic instanceof IPainted) {
				for (DyeColor color : DyeColor.values()) {
					String key = convertNameSpaceID(currentBlock.namespaceId(), prefix + "_%s") + "_" + color.colorID;
					snowy = blockBuilder.build(key, getNextID(), block -> new BlockLogicSnowyFenceGatePainted(block, currentBlock, color));
					snowy.withTags(addTooling(layer));
					SNOWY_FENCE_GATES_PAINTED.add(snowy);
					count ++;
				}
				return;
			}
			snowy = blockBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), prefix + "_%s"), getNextID(), block -> new BlockLogicSnowyFenceGate<>(block, currentBlock));
			snowy.withTags(addTooling(currentBlock));
			SNOWY_FENCE_GATE.add(snowy);
			count ++;
		}
	}

	public static void createGrass(Block<? extends BlockLogic> currentBlock, BlockLogic logic, String prefix) {
		if (logic instanceof BlockLogicTallGrass) {
			Block<?> layer = MoreSnow.LAYERS.getItem(prefix);
			Block<?> snowy = new BlockBuilder(getModID(logic))
				.setBlockSound(layer.getSound())
				.setHardness(layer.getHardness())
				.setUseInternalLight()
				.setVisualUpdateOnMetadata()
				.addTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.OVERRIDE_STEPSOUND, NOT_IN_CREATIVE_MENU)
				.build(convertNameSpaceID(currentBlock.namespaceId(), prefix + "_%s"), getNextID(), block -> new BlockLogicSnowyFlowerStackable<>(block, currentBlock));
			snowy.withTags(addTooling(layer));
			SNOWY_GRASS.add(snowy);
			printMessage(currentBlock.id(), "grass", logic.namespaceId());
			count ++;
		}
	}
}
