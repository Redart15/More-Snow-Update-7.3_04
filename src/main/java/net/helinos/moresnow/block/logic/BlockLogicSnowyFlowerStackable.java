package net.helinos.moresnow.block.logic;

import java.util.Random;

import net.helinos.moresnow.block.interfaces.IBlockLogicPlant;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicFlower;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.data.gamerule.GameRules;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumBlockSoundEffectType;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

public class BlockLogicSnowyFlowerStackable<T extends BlockLogic> extends BlockLogicSnowy<T> implements IBlockLogicPlant {

	public BlockLogicSnowyFlowerStackable(Block<T> block, Block<?> storedBlock) {
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
			this.dropWithCause(world, EnumDropCause.WORLD, tilePos,world.getBlockData(tilePos), null, null);
			world.setBlockTypeDataNotify(tilePos, Blocks.LAYER_SNOW, this.getLayers(metadata) - 1);
		}
	}

	public boolean onInteracted(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Player player, @Nullable Side side, double xHit, double yHit) {
		int metadata = world.getBlockData(tilePos);
		int currentStackCount = getStackCount(metadata);
		if (currentStackCount >= 3) {
			return false;
		} else {
			ItemStack heldItem = player.getHeldItem();
			if (heldItem != null && heldItem.stackSize >= 1 && heldItem.getItem().id == this.storedBlock.id()) {
				int newMetadata = setPermanent(setStackCount(metadata, currentStackCount + 1), true);
				world.setBlockDataNotify(tilePos, newMetadata);
				world.playBlockSoundEffect(player, tilePos.x() + 0.5F, tilePos.y() + 0.5F, tilePos.z() + 0.5F, this.storedBlock, EnumBlockSoundEffectType.PLACE);
				heldItem.consumeItem(player);
				return true;
			} else {
				return false;
			}
		}
	}

	public static int getStackCount(int metadata) {
		return (metadata & 96) >> 5;
	}

	public static int setPermanent(int metadata, boolean permanent) {
		return metadata & -129 | (permanent ? 128 : 0);
	}

	public static int setStackCount(int metadata, int stackCount) {
		return metadata & -97 | stackCount << 5 & 96;
	}

	@Override
	public boolean getKilledByWeather(int metadata) {
		int blockID = this.getStoredBlockId(metadata);
		return BlockLogicSnowyPlant.doGetKilledByWeather(blockID);
	}

	@Override
	public boolean canReplaceBlock(int id, int metadata) {
		return id == storedBlock.id();
	}

	@Override
    public int getStoredBlockMetadata(int metadata) {
        return (metadata) & 0b11100000;
    }

	@Override
	protected int blockToMetadata(int blockId, int metadata) {
		return metadata;
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
