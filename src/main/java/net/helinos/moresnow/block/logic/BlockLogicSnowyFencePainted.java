package net.helinos.moresnow.block.logic;

import net.helinos.moresnow.block.interfaces.PaintedBlock;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicFencePainted;
import net.minecraft.core.util.helper.DyeColor;

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
	public String getLanguageKey(int meta) {
		return storedBlock.getLogic() instanceof BlockLogicSnowy ? "snowy" : storedBlock.getLogic().getLanguageKey(meta) + "." + this.color.colorID;
	}

	@Override
	public int getStoredBlockMetadata(int metadata) {
		return this.color.blockMeta;
	}

	@Override
	protected int blockToMetadata(int blockId, int metadata) {
		return (metadata << 4);
	}
}
