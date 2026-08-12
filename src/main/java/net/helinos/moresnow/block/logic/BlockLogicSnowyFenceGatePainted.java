package net.helinos.moresnow.block.logic;

import net.helinos.moresnow.block.interfaces.PaintedBlock;
import net.helinos.moresnow.util.BlockMetadata;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.DyeColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockLogicSnowyFenceGatePainted<T extends BlockLogicSnowy<?>> extends BlockLogicSnowyFenceGate<T>  implements PaintedBlock {
	private final DyeColor color;

	public BlockLogicSnowyFenceGatePainted(Block<T> block, Block<?> storedBlock, @Nullable DyeColor color) {
		super(block, storedBlock);
		this.color = color;
	}

	@Override
	public DyeColor getColor() {
		return this.color;
	}

	@Override
	public @NotNull String getLanguageKey(int meta) {
		return this.storedBlock().getLogic() instanceof BlockLogicSnowy<?> ? "snowy" : this.storedBlock().getLogic().getLanguageKey(meta) + "." + this.color.colorID;
	}

	@Override
	public int storedBlockMetadata(int metadata) {
		return BlockMetadata.setBitBlock(metadata >> 4, START_INDEX, END_INDEX, this.color.blockMeta & 15);
	}

	@Override
	protected int blockToMetadata(int blockId, int metadata) {
		return (metadata << 4);
	}
}
