package net.helinos.moresnow.block.logic;

import net.helinos.moresnow.block.interfaces.PaintedBlock;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicFencePainted;
import net.minecraft.core.util.helper.DyeColor;
import org.jetbrains.annotations.NotNull;

public class BlockLogicSnowyFencePainted<T extends BlockLogic> extends BlockLogicSnowyFence<T, BlockLogicFencePainted> implements PaintedBlock {
	public final DyeColor color;

	public BlockLogicSnowyFencePainted(Block<T> block, Block<?> storedBlock, DyeColor color) {
		super(block, storedBlock);
		this.color = color;
	}

	@Override
	public DyeColor getColor() {
		return this.color;
	}

	@Override
	public @NotNull String getLanguageKey(int meta) {
		return this.storedBlock().getLogic() instanceof BlockLogicSnowy ? "snowy" : this.storedBlock().getLogic().getLanguageKey(meta) + "." + this.color.colorID;
	}

	@Override
	public int storedBlockMetadata(int metadata) {
		return this.color.blockMeta;
	}

	@Override
	protected int blockToMetadata(int blockId, int metadata) {
		return (metadata << 4);
	}
}
