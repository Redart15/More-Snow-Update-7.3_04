package net.helinos.moresnow.block;

import net.fabricmc.loader.api.FabricLoader;
import net.helinos.moresnow.MoreSnow;
import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.helinos.moresnow.block.logic.BlockLogicSnowyFencePainted;
import net.helinos.moresnow.mixins.mixin.accessor.BlockAccessor;
import net.minecraft.core.block.*;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.data.tag.Tag;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static net.helinos.moresnow.MoreSnow.*;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S2386", "java:S3008"})
public class MoreSnowBlocks {
	public static List<Block<?>> SNOWY_FLOWERS = new ArrayList<>();
	public static List<Block<?>> SNOWY_GRASS = new ArrayList<>();
	public static List<Block<?>> SNOWY_SLAB = new ArrayList<>();
	public static List<Block<?>> SNOWY_SLAB_PAINTED = new ArrayList<>();
	public static List<Block<?>> SNOWY_STAIRS = new ArrayList<>();
	public static List<Block<?>> SNOWY_STAIRS_PAINTED = new ArrayList<>();
	public static List<Block<?>> SNOWY_FENCE = new ArrayList<>();
	public static List<Block<BlockLogicSnowyFencePainted<?>>> SNOWY_FENCE_PAINTED = new ArrayList<>();
	public static List<Block<?>> SNOWY_FENCE_GATE = new ArrayList<>();
	public static List<Block<?>> SNOWY_FENCE_GATES_PAINTED = new ArrayList<>();
	public static List<Block<?>> SNOWY_FENCE_THIN = new ArrayList<>();

	public static Block<?> SNOWY_FENCE_WALLPAPER;
	public static Block<?> SNOWY_FENCE_STEEL;
	public static Block<?> SNOWY_FENCE_CHAINLINK;

	public static Block<?> LEAVY_FENCE_WALLPAPER;
	public static Block<?> LEAVY_FENCE_STEEL;
	public static Block<?> LEAVY_FENCE_CHAINLINK;

	public static Block<?> SLATY_FENCE_WALLPAPER;
	public static Block<?> SLATY_FENCE_STEEL;
	public static Block<?> SLATY_FENCE_CHAINLINK;

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
		LOGGER.info("Create Snowy variant of vanilla blocks.");
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
				MoreSnowBlockInitializer.createFlower(block, logic, prefix);
//				MoreSnowBlockInitializer.createFlowerStackable(block, logic, prefix);
//				MoreSnowBlockInitializer.createGrass(block, logic, prefix);
//				MoreSnowBlockInitializer.createSapling(block, logic, prefix);
//				MoreSnowBlockInitializer.createMushrooms(block, logic, prefix);
				MoreSnowBlockInitializer.createSlab(block, logic, prefix);
				MoreSnowBlockInitializer.createStairs(block, logic, prefix);
				MoreSnowBlockInitializer.createFence(block, logic, prefix);
				MoreSnowBlockInitializer.createFenceGate(block, logic, prefix);
				MoreSnowBlockInitializer.createFenceThinGeneral(block, logic, prefix);
			}
			MoreSnowBlockInitializer.createFenceThin(block, logic);
		}
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
		Block<?> block = Blocks.getBlock(id);
		if (block == null) {
			return false;
		}
		BlockLogic logic = block.getLogic();
		if (logic == null) {
			return false;
		}
		String name = "block/" + convertNameSpaceID(block.namespaceId(), prefix);
		if (logic instanceof IPainted) {
			DyeColor color = ((IPainted) logic).getColor(world, x, y, z);
			name = name + "_" + color.colorID;
		}
		NamespaceID namespaceID = NamespaceID.getPermanent(MoreSnowBlockInitializer.getModID(logic), name);
		Block<?> replaceBlock = Blocks.blockMap.get(namespaceID);
		if (replaceBlock == null || replaceBlock.getLogic() == null || !(replaceBlock.getLogic() instanceof BlockLogicSnowy)) {
			NamespaceID adjusted = NamespaceID.getPermanent(MoreSnow.MOD_ID, name + "." + block.getLogic().namespaceId().namespace());
			Block<?> adjustedBlock = Blocks.blockMap.get(adjusted);
			if (adjustedBlock == null || adjustedBlock.getLogic() == null || !(adjustedBlock.getLogic() instanceof BlockLogicSnowy)) {
				return false;
			}
			replaceBlock = adjustedBlock;
		}
		return ((BlockLogicSnowy<?>) replaceBlock.getLogic()).tryMakeSnowy(world, id, x, y, z);
	}


	public static boolean convertBlock(Chunk chunk, int id, int x, int y, int z, String prefix) {
		Block<?> block = Blocks.getBlock(id);
		if (block == null) {
			return false;
		}
		BlockLogic logic = block.getLogic();
		if (logic == null) {
			return false;
		}
		String name = "block/" + convertNameSpaceID(block.namespaceId(), prefix);
		if (logic instanceof IPainted) {
			int metadata = chunk.getBlockMetadata(x, y, z);
			DyeColor color = DyeColor.colorFromBlockMeta(metadata >> 4);
			name = name + "_" + color.colorID;
		}
		NamespaceID namespaceID = NamespaceID.getPermanent(MoreSnowBlockInitializer.getModID(logic), name);
		Block<?> replaceBlock = Blocks.blockMap.get(namespaceID);
		if (replaceBlock == null || replaceBlock.getLogic() == null || !(replaceBlock.getLogic() instanceof BlockLogicSnowy)) {
			NamespaceID adjusted = NamespaceID.getPermanent(MoreSnow.MOD_ID, name + "." + block.getLogic().namespaceId().namespace());
			Block<?> adjustedBlock = Blocks.blockMap.get(adjusted);
			if (adjustedBlock == null || adjustedBlock.getLogic() == null || !(adjustedBlock.getLogic() instanceof BlockLogicSnowy)) {
				return false;
			}
			replaceBlock = adjustedBlock;
		}
		return ((BlockLogicSnowy<?>) replaceBlock.getLogic()).tryMakeSnowy(chunk, id, x, y, z);
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
}
