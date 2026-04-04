package net.helinos.moresnow.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentTypeInteger;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.builder.ArgumentBuilderRequired;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.arguments.ArgumentTypeVec3;
import net.minecraft.core.net.command.helpers.DoubleCoordinate;
import net.minecraft.core.net.command.helpers.DoubleCoordinates;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkSection;

public class CommandRemoveSnowy implements CommandManager.CommandRegistry {


	public void register(CommandDispatcher<CommandSource> dispatcher) {
		ArgumentBuilderLiteral<CommandSource> command =
			(ArgumentBuilderLiteral) ((ArgumentBuilderLiteral) ArgumentBuilderLiteral.literal("protect")
				.requires((t) -> ((CommandSource) t).hasAdmin())
				.then(ArgumentBuilderLiteral.literal("chunk")
					.executes(CommandRemoveSnowy::chunk)
					.then(ArgumentBuilderRequired.argument("point", ArgumentTypeVec3.vec3d())
						.executes(CommandRemoveSnowy::chunk))
					.then(ArgumentBuilderRequired.argument("radius", ArgumentTypeInteger.integer(1, 9))
						.executes(CommandRemoveSnowy::chunkRadius))));
		dispatcher.register(command);
	}

	private static int chunkRadius(CommandContext<Object> context) {
		int sum = 0;

		CommandSource source = (CommandSource) context.getSource();
		Player player = source.getSender();

		int radius = context.getArgument("radius", Integer.class);
		for (int x = -radius; x < radius; x++) {
			for (int z = -radius; z < radius; z++) {
				DoubleCoordinate dx = new DoubleCoordinate(false, player.x + x * 16.0F);
				DoubleCoordinate dy = new DoubleCoordinate(false, 0);
				DoubleCoordinate dz = new DoubleCoordinate(false, player.z + z * 16.0F);
				sum += CommandRemoveSnowy.chunk(source, new DoubleCoordinates(dx, dy, dz));
			}
		}
		return sum;
	}

	private static int chunk(CommandContext<Object> context) {
		CommandSource source = (CommandSource) context.getSource();
		DoubleCoordinates point;
		try {
			point = context.getArgument("point", DoubleCoordinates.class);
		} catch (IllegalArgumentException noargs) {
			Player player = source.getSender();
			DoubleCoordinate x = new DoubleCoordinate(false, player.x);
			DoubleCoordinate y = new DoubleCoordinate(false, player.y);
			DoubleCoordinate z = new DoubleCoordinate(false, player.z);
			point = new DoubleCoordinates(x, y, z);
		}
		return CommandRemoveSnowy.chunk(source, point);
	}

	private static int chunk(CommandSource source, DoubleCoordinates point) {
		int fx;
		int fz;
		try {
			fx = (int) Math.round(point.getX(source));
			fz = (int) Math.round(point.getZ(source));
		} catch (CommandSyntaxException e) {
			throw new RuntimeException(e);
		}
		World world = source.getWorld();
		Chunk chunk = world.getChunkFromChunkCoords(Math.floorDiv(fx, 16), Math.floorDiv(fz, 16));
		if(!chunk.isLoaded){
			return 0;
		}
		for (int i = 0; i < Chunk.CHUNK_SECTIONS; i++) {
			ChunkSection section = chunk.getSection(i);
			for (int x = 0; x < Chunk.CHUNK_SIZE_X; x++) {
				for (int z = 0; z < Chunk.CHUNK_SIZE_Z; z++) {
					for (int y = 0; y < ChunkSection.SECTION_SIZE_Y; y++) {
						CommandRemoveSnowy.removeSnowyBlocks(section, x, y, z);
					}
				}
			}
		}
		return 1;
	}

	private static void removeSnowyBlocks(ChunkSection section, int x, int y, int z) {
		int id = section.getBlock(x, y, z);
		if (id == 0) {
			return;
		}
		Block<?> block = Blocks.getBlock(id);
		if (block == null || block.getLogic() == null) {
			return;
		}
		BlockLogic logic = block.getLogic();
		if (!(logic instanceof BlockLogicSnowy)) {
			return;
		}
		BlockLogicSnowy snowy = (BlockLogicSnowy) logic;
		section.setBlock(x, y, z, (short) snowy.storedBlock.id());
		section.setData(x, y, z, snowy.getStoredBlockMetadata(section.getData(x, y, z)));
	}

}
