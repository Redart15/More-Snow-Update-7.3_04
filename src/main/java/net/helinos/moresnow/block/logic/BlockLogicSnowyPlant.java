package net.helinos.moresnow.block.logic;

import java.util.Random;

import net.helinos.moresnow.block.interfaces.IBlockLogicPlant;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicFlower;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.gamerule.GameRules;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

public class BlockLogicSnowyPlant<T extends BlockLogic, F extends BlockLogicFlower> extends BlockLogicSnowy<T> implements IBlockLogicPlant {

	public BlockLogicSnowyPlant(Block<T> block, Block<?> storedBlock) {
		super(block, storedBlock, 8, 0);
		block.setTicking(true);
	}

	@Override
	public @NotNull AABBdc getBoundsFromState(@NotNull WorldSource source, @NotNull TilePosc tilePos) {
		int metadata = source.getBlockData(tilePos);
		int layers = this.getLayers(metadata);
		double height = layers * 2 / 16.0;
		return new AABBd(0.0, 0.0, 0.0, 1.0, height, 1.0);
	}

	@Override
    public int getStoredBlockMetadata(int metadata) {
        return (metadata) & 0b10000000;
    }

	@SuppressWarnings({"java:S5411"})
	@Override
	public void updateTick(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Random random, boolean isRandomTick) {
		super.updateTick(world, tilePos, random, isRandomTick);
		int metadata = world.getBlockData(tilePos);
		if (
			!BlockLogicFlower.isPermanent(metadata) &&
				world.getGameRuleValue(GameRules.DO_SEASONAL_GROWTH) &&
				world.getSeasonManager().getCurrentSeason() != null &&
				world.getSeasonManager().getCurrentSeason().killFlowers &&
				this.getKilledByWeather(metadata) &&
				random.nextInt(256) == 0
		) {
			this.dropWithCause(world, EnumDropCause.WORLD, tilePos, world.getBlockData(tilePos), null, null);
			world.setBlockTypeDataNotify(tilePos, Blocks.LAYER_SNOW, this.getLayers(metadata) - 1);
		}
	}

	@Override
	public boolean getKilledByWeather(int metadata) {
		int blockID = this.getStoredBlockId(metadata);
		return doGetKilledByWeather(blockID);
	}

	public static boolean doGetKilledByWeather(int blockID) {
		Block<?> block = Blocks.getBlock(blockID);
		if (block.getLogic() instanceof BlockLogicFlower) {
			return ((BlockLogicFlower) block.getLogic()).killedByWeather;
		}

		return false;
	}

	@Override
	public boolean isSolidRender() {
		return false;
	}

	@Override
	public boolean isCubeShaped() {
		return false;
	}

	@Override
	public boolean getSupportsOwnSnow() {
		return false;
	}
}
