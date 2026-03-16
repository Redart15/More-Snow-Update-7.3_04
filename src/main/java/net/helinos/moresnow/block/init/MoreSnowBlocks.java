package net.helinos.moresnow.block.init;

import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.helinos.moresnow.block.logic.BlockLogicSnowyFencePainted;
import net.helinos.moresnow.mixin.accessor.BlockAccessor;
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

@SuppressWarnings({"java:S1104","java:S1444","java:S2386","java:S3008"})
public class MoreSnowBlocks {
	public static List<Block<?>> SNOWY_FLOWERS = new ArrayList<>();
	public static List<Block<?>> SNOWY_SLAB = new ArrayList<>();
	public static List<Block<?>> SNOWY_SLAB_PAINTED = new ArrayList<>();
	public static List<Block<?>> SNOWY_STAIRS = new ArrayList<>();
	public static List<Block<?>> SNOWY_STAIRS_PAINTED = new ArrayList<>();
	public static List<Block<?>> SNOWY_FENCE = new ArrayList<>();
	public static List<Block<BlockLogicSnowyFencePainted<?>>> SNOWY_FENCE_PAINTED = new ArrayList<>();
	public static List<Block<?>> SNOWY_FENCE_GATE = new ArrayList<>();
	public static List<Block<?>> SNOWY_FENCE_GATES_PAINTED = new ArrayList<>();
	public static Block<?> SNOWY_FENCE_WALLPAPER;
	public static Block<?> SNOWY_FENCE_STEEL;
	public static Block<?> SNOWY_FENCE_CHAINLINK;

	private static boolean initialized = false;
	private static int count = 0;
	private static final String UNFORMATTED_MESSAGE = "%6d \t %14s -> %s";
	private static final int STARTING_ID = 4500;
	private static int currentID = STARTING_ID;
//	public static final Tag<Block<?>> NOT_IN_CREATIVE_MENU = BlockTags.NOT_IN_CREATIVE_MENU;
	public static final Tag<Block<?>> NOT_IN_CREATIVE_MENU = BlockTags.OVERRIDE_STEPSOUND;
	public static final String MOD_ID = "test";

	private MoreSnowBlocks() {
	}

	@SuppressWarnings("unchecked")
	public static void init() {
		if (initialized) return;
		initialized = true;
		LOGGER.info("Create Snowy variant of vanilla blocks.");
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
			MoreSnowBlockInitializer.createFlower(block, logic);
			MoreSnowBlockInitializer.createSapling(block, logic);
			MoreSnowBlockInitializer.createMushrooms(block, logic);
			MoreSnowBlockInitializer.createSlab(block, logic);
			MoreSnowBlockInitializer.createStairs(block, logic);
			MoreSnowBlockInitializer.createFence(block, logic);
			MoreSnowBlockInitializer.createFenceThin(block, logic);
			MoreSnowBlockInitializer.createFenceGate(block, logic);
			/// TODO: Implement them later on once everything is fixed
//			createSign(block, logic);
//			createRail(block, logic);
//			createButton(block, logic);
//			createPressurePlate(block, logic);
//			createDoor(block, logic);
//			createBrazier(block, logic);
//			createFlag(block, logic);
//			createBasket(block, logic);
			/// TODO: repeat it with leaves layers
			/// TODO: repeat it with slate layers
			///	TODO: prep for ash layers
		}
		LOGGER.info("Blocks created:{}", count);
	}

	public static int getNextID() {
		int current = currentID;
		currentID++;
		return current;
	}

	public static String convertNameSpaceID(NamespaceID blockID) {
		String[] splitstring = blockID.value().split("/");
		return String.format("snowy_%s", splitstring[1]);
	}

	public static void printMessage(int id, String blockType, @NotNull NamespaceID namespaceID) {
		String message = String.format(UNFORMATTED_MESSAGE, id, blockType, namespaceID);
		LOGGER.info(message);
		count++;
	}

	public static boolean convertBlock(World world, int id, int x, int y, int z){
		Block<?> block = Blocks.getBlock(id);
		if(block == null){
			return false;
		}
		BlockLogic logic = block.getLogic();
		if(logic == null){
			return false;
		}
		String name = "block/" + convertNameSpaceID(block.namespaceId());
		if(logic instanceof IPainted){
			DyeColor color = ((IPainted)logic).getColor(world, x, y, z);
			name = name + "_" + color.colorID;
		}
		NamespaceID namespaceID = NamespaceID.getPermanent(MOD_ID, name);
		Block<?> replaceBlock = Blocks.blockMap.get(namespaceID);
		if(replaceBlock == null || replaceBlock.getLogic() == null || !(replaceBlock.getLogic() instanceof BlockLogicSnowy) ){
			return false;
		}
		return ((BlockLogicSnowy<?>)replaceBlock.getLogic()).tryMakeSnowy(world, id, x, y, z);
	}


	public static boolean convertBlock(Chunk chunk, int id, int x, int y, int z){
		Block<?> block = Blocks.getBlock(id);
		if(block == null){
			return false;
		}
		BlockLogic logic = block.getLogic();
		if(logic == null){
			return false;
		}
		String name = "block/" + convertNameSpaceID(block.namespaceId());
		if(logic instanceof IPainted){
			int metadata = chunk.getBlockMetadata(x, y, z);
			DyeColor color = DyeColor.colorFromBlockMeta(metadata >> 4);
			name = name + "_" + color.colorID;
		}
		NamespaceID namespaceID = NamespaceID.getPermanent(MOD_ID, name);
		Block<?> replaceBlock = Blocks.blockMap.get(namespaceID);
		if(replaceBlock == null || replaceBlock.getLogic() == null || !(replaceBlock.getLogic() instanceof BlockLogicSnowy) ){
			return false;
		}
		return ((BlockLogicSnowy<?>)replaceBlock.getLogic()).tryMakeSnowy(chunk, replaceBlock.id(), x, y, z);
	}

	public static @NotNull String prePendName(Block<?> block, ItemStack itemStack) {
		return LANGUAGE.translateKey("snow.name") + " ";
	}

	public static @NotNull String prePendDesc(Block<?> block, ItemStack itemStack) {
		String suffix = LANGUAGE.translateKey(block.getLogic().getLanguageKey(itemStack.getMetadata()) + ".name");
		return LANGUAGE.translateKey("snow.desc") + " " + suffix.toLowerCase() + ".";
	}
}
