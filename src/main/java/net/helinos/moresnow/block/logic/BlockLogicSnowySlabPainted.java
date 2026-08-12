package net.helinos.moresnow.block.logic;

import net.helinos.moresnow.block.interfaces.PaintedBlock;
import net.helinos.moresnow.util.BlockMetadata;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.helper.DyeColor;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

public class BlockLogicSnowySlabPainted<T extends BlockLogic> extends BlockLogicSnowy<T> implements PaintedBlock {
	private final DyeColor color;

	public BlockLogicSnowySlabPainted(Block<T> block, Block<?> storedBlock, DyeColor color) {
		super(block, storedBlock, 4, 4);
		this.color = color;
	}

	@Override
	public DyeColor getColor() {
		return this.color;
	}

	@Override
	public @NotNull String getLanguageKey(int meta) {
		return this.storedBlock().getLogic() instanceof BlockLogicSnowy ? "snowy" : this.storedBlock().getLogic().getLanguageKey(this.color.blockMeta << 4);
	}

	@Override
	public boolean canReplaceBlock(int id, int metadata) {
		return id == storedBlockId(metadata) && (metadata & 3) == 0;
	}

	@Override
	public @NotNull AABBdc getBoundsFromState(@NotNull WorldSource source, @NotNull TilePosc tilePos) {
		int l = this.getRelativeLayers(source.getBlockData(tilePos)) - 1;
		float f = (2 * (1 + l)) / 16.0F;
		return new AABBd(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
	}

	@Override
	public int storedBlockMetadata(int metadata) {
		return BlockMetadata.setBitBlock(metadata >> 4, START_INDEX, END_INDEX, this.color.blockMeta & 15);
	}

	@Override
	protected int blockToMetadata(int blockId, int metadata) {
		return (metadata) << 4;
	}

	@Override
	public boolean isSolidRender() {
		return false;
	}


}
