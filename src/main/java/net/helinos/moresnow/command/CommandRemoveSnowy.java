package net.helinos.moresnow.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentTypeInteger;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.builder.ArgumentBuilderRequired;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import net.minecraft.core.net.command.helpers.DoubleCoordinate;
import net.minecraft.core.net.command.helpers.DoubleCoordinates;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.pos.ChunkPos;
import net.minecraft.core.world.pos.ChunkTilePos;

public class CommandRemoveSnowy implements CommandManager.CommandRegistry {


	public void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(ArgumentBuilderLiteral.<CommandSource>literal("lesssnow")
			.requires((t) -> t.hasAdmin() && t.getSender() != null)
			.then(ArgumentBuilderRequired.<CommandSource, Integer>argument("radius", ArgumentTypeInteger.integer(1, 9))
				.executes(CommandRemoveSnowy::chunkRadius)
			)
		);
	}

	private static int chunkRadius(CommandContext<CommandSource> context) {
		int sum = 0;

		CommandSource source = context.getSource();
		Player player = source.getSender();
		if(player == null){
			return -1;
		}
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
		Chunk chunk = world.getChunk(new ChunkPos(fx, fz));
		if (!chunk.isLoaded) {
			return 0;
		}

		for (int x = 0; x < Chunk.CHUNK_SIZE_X; x++) {
			for (int z = 0; z < Chunk.CHUNK_SIZE_Z; z++) {
				for (int y = 0; y < World.HEIGHT_BLOCKS; y++) {
					CommandRemoveSnowy.removeSnowyBlocks(chunk, new ChunkTilePos(x, y, z));
				}
			}
		}

		return 1;
	}

	private static void removeSnowyBlocks(Chunk chunk, ChunkTilePos chunkTilePos) {
		Block<?> block = chunk.getBlock(chunkTilePos);
		if (!(block.getLogic() instanceof BlockLogicSnowy<?> logicSnowy)) {
			return;
		}
		chunk.setBlockIdDataRaw(chunkTilePos, logicSnowy.storedBlock().id(), logicSnowy.storedBlockMetadata(chunk.getBlockData(chunkTilePos)));
	}

}
