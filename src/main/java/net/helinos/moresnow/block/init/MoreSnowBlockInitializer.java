package net.helinos.moresnow.block.init;

import net.helinos.moresnow.block.logic.*;
import net.helinos.moresnow.block.logic.BlockLogicSnowyFenceGatePainted;
import net.helinos.moresnow.block.logic.BlockLogicSnowyFencePainted;
import net.helinos.moresnow.block.logic.BlockLogicSnowySlabPainted;
import net.helinos.moresnow.block.logic.BlockLogicSnowyStairsPainted;
import net.minecraft.core.block.*;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.sound.BlockSounds;
import net.minecraft.core.util.helper.DyeColor;
import turniplabs.halplibe.helper.BlockBuilder;

import static net.helinos.moresnow.block.init.MoreSnowBlocks.*;
import static net.helinos.moresnow.block.init.MoreSnowBlocks.LEAVY_FENCE_CHAINLINK;
import static net.helinos.moresnow.block.init.MoreSnowBlocks.SLATY_FENCE_CHAINLINK;

@SuppressWarnings({"java:S1144"})
public class MoreSnowBlockInitializer {
	private MoreSnowBlockInitializer(){}
	/// BlockTags of Snow
	///	BlockTags.BROKEN_BY_FLUIDS, BlockTags.PLACE_OVERWRITES, BlockTags.MINEABLE_BY_SHOVEL, BlockTags.OVERRIDE_STEPSOUND


	///	BlockTags.BROKEN_BY_FLUIDS, BlockTags.PLANTABLE_IN_JAR, BlockTags.SHEEPS_FAVOURITE_BLOCK, BlockTags.SHEARS_DO_SILK_TOUCH
	public static void createFlower(Block<? extends BlockLogic> currentBlock, BlockLogic logic, String prefix) {
		if (logic instanceof BlockLogicFlowerStackable) {
			Block<?> snowy = new BlockBuilder(MOD_ID)
				.setBlockSound(Blocks.BLOCK_SNOW.getSound())
				.setHardness(Blocks.BLOCK_SNOW.getHardness())
				.setUseInternalLight()
				.setVisualUpdateOnMetadata()
				.addTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.MINEABLE_BY_SHOVEL, BlockTags.OVERRIDE_STEPSOUND, NOT_IN_CREATIVE_MENU)
				.build(convertNameSpaceID(currentBlock.namespaceId(), prefix), getNextID(), block -> new BlockLogicSnowyFlowerStackable<>(block, currentBlock));
			SNOWY_FLOWERS.add(snowy);
			printMessage(currentBlock.id(), "flower", logic.namespaceId());
		}
	}

	/// BlockTags.SHEARS_DO_SILK_TOUCH, BlockTags.MINEABLE_BY_AXE, BlockTags.MINEABLE_BY_HOE, BlockTags.MINEABLE_BY_SWORD, BlockTags.MINEABLE_BY_SHEARS
	public static void createSapling(Block<? extends BlockLogic> currentBlock, BlockLogic logic, String prefix) {
		if (logic instanceof BlockLogicSaplingBase) {
			Block<?> snowy = new BlockBuilder(MOD_ID)
				.setBlockSound(Blocks.BLOCK_SNOW.getSound())
				.setHardness(Blocks.BLOCK_SNOW.getHardness())
				.setUseInternalLight()
				.setVisualUpdateOnMetadata()
				.addTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.MINEABLE_BY_SHOVEL, BlockTags.OVERRIDE_STEPSOUND, NOT_IN_CREATIVE_MENU)
				.build(convertNameSpaceID(currentBlock.namespaceId(), prefix), getNextID(), block -> new BlockLogicSnowyFlowerStackable<>(block, currentBlock));
			SNOWY_FLOWERS.add(snowy);
			printMessage(currentBlock.id(), "sapling", logic.namespaceId());
		}
	}

	public static void createMushrooms(Block<? extends BlockLogic> currentBlock, BlockLogic logic, String prefix) {
		if (logic instanceof BlockLogicMushroom) {
			Block<?> snowy = new BlockBuilder(MOD_ID)
				.setBlockSound(Blocks.BLOCK_SNOW.getSound())
				.setHardness(Blocks.BLOCK_SNOW.getHardness())
				.setUseInternalLight()
				.setVisualUpdateOnMetadata()
				.addTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.MINEABLE_BY_SHOVEL, BlockTags.OVERRIDE_STEPSOUND, NOT_IN_CREATIVE_MENU)
				.build(convertNameSpaceID(currentBlock.namespaceId(), prefix), getNextID(), block -> new BlockLogicSnowyFlowerStackable<>(block, currentBlock));
			SNOWY_FLOWERS.add(snowy);
			printMessage(currentBlock.id(), "mushroom", logic.namespaceId());
		}
	}

	public static void createSlab(Block<? extends BlockLogic> currentBlock, BlockLogic logic, String prefix) {
		if (logic instanceof BlockLogicSlab) {
			Block<?> snowy;
			printMessage(currentBlock.id(), "slab", logic.namespaceId());
			BlockBuilder blockBuilder = new BlockBuilder(MOD_ID)
				.setBlockSound(Blocks.BLOCK_SNOW.getSound())
				.setHardness(Blocks.BLOCK_SNOW.getHardness())
				.setUseInternalLight()
				.setLightOpacity(1)
				.setVisualUpdateOnMetadata()
				.addTags(BlockTags.MINEABLE_BY_SHOVEL, NOT_IN_CREATIVE_MENU);
			if(logic instanceof IPainted){
				for(DyeColor color: DyeColor.values()) {
					String key = convertNameSpaceID(currentBlock.namespaceId(), prefix) + "_" + color.colorID;
					snowy = blockBuilder.build(key, getNextID(), block -> new BlockLogicSnowySlabPainted<>(block, currentBlock, color));
					SNOWY_SLAB_PAINTED.add(snowy);
				}
				return;
			}
			snowy = blockBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), prefix), getNextID(), block -> new BlockLogicSnowySlab<>(block, currentBlock));
			SNOWY_SLAB.add(snowy);
		}
	}

	public static void createStairs(Block<? extends BlockLogic> currentBlock, BlockLogic logic, String prefix) {
		if (logic instanceof BlockLogicStairs) {
			Block<?> snowy;
			printMessage(currentBlock.id(), "stairs", logic.namespaceId());
			BlockBuilder blockBuilder = new BlockBuilder(MOD_ID)
				.setBlockSound(Blocks.BLOCK_SNOW.getSound())
				.setHardness(Blocks.BLOCK_SNOW.getHardness())
				.setUseInternalLight()
				.setLightOpacity(15)
				.setVisualUpdateOnMetadata()
				.addTags(BlockTags.MINEABLE_BY_SHOVEL, NOT_IN_CREATIVE_MENU);
			if(logic instanceof IPainted) {
				for(DyeColor color: DyeColor.values()) {
					String key = convertNameSpaceID(currentBlock.namespaceId(), prefix) + "_" + color.colorID;
					snowy = blockBuilder.build(key, getNextID(), block -> new BlockLogicSnowyStairsPainted<>(block, currentBlock, color));
					SNOWY_STAIRS_PAINTED.add(snowy);
				}
				return;
			}
			snowy = blockBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), prefix), getNextID(), block -> new BlockLogicSnowyStairs<>(block, currentBlock));
			SNOWY_STAIRS.add(snowy);
		}
	}

	/// BlockTags.FENCES_CONNECT, BlockTags.MINEABLE_BY_AXE, BlockTags.CAN_HANG_OFF
	public static void createFence(Block<? extends BlockLogic> currentBlock, BlockLogic logic, String prefix) {
		if (logic instanceof BlockLogicFence) {
			Block<?> snowy;
			printMessage(currentBlock.id(), "fence", logic.namespaceId());
			BlockBuilder blockBuilder = new BlockBuilder(MOD_ID)
				.setBlockSound(Blocks.BLOCK_SNOW.getSound())
				.setHardness(Blocks.BLOCK_SNOW.getHardness())
				.setUseInternalLight()
				.setLightOpacity(15)
				.setVisualUpdateOnMetadata()
				.addTags(BlockTags.MINEABLE_BY_SHOVEL, NOT_IN_CREATIVE_MENU);
			blockBuilder = addConnectTags(currentBlock, blockBuilder);
			if(logic instanceof IPainted) {
				for(DyeColor color: DyeColor.values()) {
					String key = convertNameSpaceID(currentBlock.namespaceId(), prefix) + "_" + color.colorID;
					Block<BlockLogicSnowyFencePainted<?>> fence = blockBuilder.build(key, getNextID(), block -> new BlockLogicSnowyFencePainted<>(block, currentBlock, color));
					SNOWY_FENCE_PAINTED.add(fence);
				}
				return;
			}
			snowy = blockBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), prefix), getNextID(), block -> new BlockLogicSnowyFence<>(block, currentBlock));
			SNOWY_FENCE.add(snowy);
		}
	}

	public static void createFenceThin(Block<? extends BlockLogic> currentBlock, BlockLogic logic) {
		if (logic instanceof BlockLogicFenceThin) {
			printMessage(currentBlock.id(), "thin-fence", logic.namespaceId());
			BlockBuilder blockBuilder = new BlockBuilder(MOD_ID)
				.setBlockSound(BlockSounds.CLOTH)
				.setHardness(0.1f)
				.setUseInternalLight()
				.setVisualUpdateOnMetadata();
			blockBuilder = addConnectTags(currentBlock, blockBuilder);
			if(currentBlock.id() == Blocks.FENCE_STEEL.id()){
				BlockBuilder fenceSteelBuilder = blockBuilder.addTags(BlockTags.MINEABLE_BY_SHOVEL, BlockTags.OVERRIDE_STEPSOUND, BlockTags.CHAINLINK_FENCES_CONNECT, NOT_IN_CREATIVE_MENU);
				SNOWY_FENCE_STEEL = fenceSteelBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), "snow_%s"), getNextID(), BlockLogicSnowyFenceSteel::new);
				LEAVY_FENCE_STEEL = fenceSteelBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), "leaves_%s"), getNextID(), BlockLogicSnowyFenceSteel::new);
				SLATY_FENCE_STEEL = fenceSteelBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), "slate_%s"), getNextID(), BlockLogicSnowyFenceSteel::new);
				return;
			}
			if(currentBlock.id() == Blocks.FENCE_CHAINLINK.id()){
				BlockBuilder fenceChainBuilder = blockBuilder.addTags(BlockTags.MINEABLE_BY_SHOVEL, BlockTags.OVERRIDE_STEPSOUND, BlockTags.CHAINLINK_FENCES_CONNECT, NOT_IN_CREATIVE_MENU);
				SNOWY_FENCE_CHAINLINK = fenceChainBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), "snow_%s"), getNextID(), BlockLogicSnowyFenceChainlink::new);
				LEAVY_FENCE_CHAINLINK = fenceChainBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), "leaves_%s"), getNextID(), BlockLogicSnowyFenceChainlink::new);
				SLATY_FENCE_CHAINLINK = fenceChainBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), "slate_%s"), getNextID(), BlockLogicSnowyFenceChainlink::new);
				return;
			}
			if(currentBlock.id() == Blocks.FENCE_PAPER_WALL.id()){
				BlockBuilder fencePaperWallBuilder = blockBuilder.addTags(BlockTags.MINEABLE_BY_SHOVEL, BlockTags.OVERRIDE_STEPSOUND, BlockTags.FENCES_CONNECT, NOT_IN_CREATIVE_MENU);
				SNOWY_FENCE_WALLPAPER = fencePaperWallBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), "snow_%s"), getNextID(), BlockLogicSnowyFenceWallPaper::new);
				LEAVY_FENCE_WALLPAPER = fencePaperWallBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), "leaves_%s"), getNextID(), BlockLogicSnowyFenceWallPaper::new);
				SLATY_FENCE_WALLPAPER = fencePaperWallBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), "slate_%s"), getNextID(), BlockLogicSnowyFenceWallPaper::new);
			}
		}
	}

	///  BlockTags.FENCES_CONNECT, BlockTags.MINEABLE_BY_AXE
	public static void createFenceGate(Block<? extends BlockLogic> currentBlock, BlockLogic logic, String prefix) {
		if (logic instanceof BlockLogicFenceGate) {
			Block<?> snowy;
			printMessage(currentBlock.id(), "fence-gate", logic.namespaceId());
			BlockBuilder blockBuilder = new BlockBuilder(MOD_ID)
				.setBlockSound(Blocks.BLOCK_SNOW.getSound())
				.setHardness(Blocks.BLOCK_SNOW.getHardness())
				.setUseInternalLight()
				.setVisualUpdateOnMetadata()
				.setTags(BlockTags.MINEABLE_BY_SHOVEL, NOT_IN_CREATIVE_MENU);
			blockBuilder = addConnectTags(currentBlock, blockBuilder);
			if(logic instanceof IPainted) {
				for(DyeColor color: DyeColor.values()) {
					String key = convertNameSpaceID(currentBlock.namespaceId(), prefix) + "_" + color.colorID;
					snowy = blockBuilder.build(key, getNextID(), block -> new BlockLogicSnowyFenceGatePainted(block, currentBlock, color));
					SNOWY_FENCE_GATES_PAINTED.add(snowy);
				}
				return;
			}
			snowy = blockBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), prefix), getNextID(), block -> new BlockLogicSnowyFenceGate<>(block, currentBlock));
			SNOWY_FENCE_GATE.add(snowy);
		}
	}

	private static BlockBuilder addConnectTags(Block<? extends BlockLogic> currentBlock, BlockBuilder blockBuilder) {
		if(currentBlock.hasTag(BlockTags.FENCES_CONNECT)){
			return blockBuilder.addTags(BlockTags.FENCES_CONNECT);
		}
		if(currentBlock.hasTag(BlockTags.CHAINLINK_FENCES_CONNECT)){
			return blockBuilder.addTags(BlockTags.CHAINLINK_FENCES_CONNECT);
		}
		return blockBuilder;
	}

	public static void createTrapDoor(Block<? extends BlockLogic> currentBlock, BlockLogic logic) {
		if (logic instanceof BlockLogicTrapDoor) {
			Block<?> snowy;
			printMessage(currentBlock.id(), "trapdoor", logic.namespaceId());
			BlockBuilder blockBuilder = new BlockBuilder(MOD_ID)
				.setBlockSound(Blocks.BLOCK_SNOW.getSound())
				.setHardness(Blocks.BLOCK_SNOW.getHardness())
				.setUseInternalLight()
				.setVisualUpdateOnMetadata()
				.setTags(BlockTags.MINEABLE_BY_SHOVEL, NOT_IN_CREATIVE_MENU);
//			if(logic instanceof IPainted) {
//				for(DyeColor color: DyeColor.values()) {
//					String key = convertNameSpaceID(currentBlock.namespaceId()) + "_" + color.colorID;
//					snowy = blockBuilder.build(key, getNextID(), block -> new BlockLogicSnowyFenceGatePainted(block, currentBlock, color));
//					SNOWY_FENCE_GATES_PAINTED.add(snowy);
//				}
//				return;
//			}
			snowy = blockBuilder.build(convertNameSpaceID(currentBlock.namespaceId(), "snowy_%s"), getNextID(), block -> new BlockLogicSnowyTrapDoor<>(block, currentBlock));
			SNOWY_TRAPDOOR.add(snowy);
		}
	}

	private static void createSign(Block<? extends BlockLogic> currentBlock, BlockLogic logic) {
		if (logic instanceof BlockLogicSign) {
			printMessage(currentBlock.id(), "sign", logic.namespaceId());
		}
	}
	private static void createButton(Block<? extends BlockLogic> currentBlock, BlockLogic logic) {
		if (logic instanceof BlockLogicButton) {
			printMessage(currentBlock.id(), "button", logic.namespaceId());
		}
	}
	private static void createPressurePlate(Block<? extends BlockLogic> currentBlock, BlockLogic logic) {
		if (logic instanceof BlockLogicPressurePlate) {
			printMessage(currentBlock.id(), "pressure-plate", logic.namespaceId());
		}
	}
	private static void createDoor(Block<? extends BlockLogic> currentBlock, BlockLogic logic) {
		if (logic instanceof BlockLogicDoor) {
			printMessage(currentBlock.id(), "door", logic.namespaceId());
		}
	}
	private static void createRail(Block<? extends BlockLogic> currentBlock, BlockLogic logic) {
		if (logic instanceof BlockLogicRail) {
			printMessage(currentBlock.id(), "rail", logic.namespaceId());
		}
	}
	private static void createBrazier(Block<? extends BlockLogic> currentBlock, BlockLogic logic) {
		if (logic instanceof BlockLogicBrazier) {
			printMessage(currentBlock.id(), "brazier", logic.namespaceId());
		}
	}
	private static void createFlag(Block<? extends BlockLogic> currentBlock, BlockLogic logic) {
		if (logic instanceof BlockLogicFlag) {
			printMessage(currentBlock.id(), "flag", logic.namespaceId());
		}
	}
	private static void createBasket(Block<? extends BlockLogic> currentBlock, BlockLogic logic) {
		if (logic instanceof BlockLogicBasket) {
			printMessage(currentBlock.id(), "basket", logic.namespaceId());
		}
	}


}
