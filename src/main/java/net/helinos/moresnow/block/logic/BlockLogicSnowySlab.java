package net.helinos.moresnow.block.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicSlab;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.joml.primitives.AABBd;
import org.joml.primitives.AABBdc;

public class BlockLogicSnowySlab<T extends BlockLogic, S extends BlockLogicSlab> extends BlockLogicSnowy<T> {
	public BlockLogicSnowySlab(Block<T> block, Block<?> storedBlock) {
		super(block, storedBlock, 4, 4);
		this.setBlockBounds(0.0, 0.0, 0.0, 1.0, 0.625, 1.0);
	}

	@Override
	public boolean canReplaceBlock(int id, int metadata) {
		if (super.canReplaceBlock(id, metadata)) {
			return (metadata & 0b11) == 0;
		}
		return false;
	}
	@Override
	public @NotNull AABBdc getBoundsFromState(@NotNull WorldSource source, @NotNull TilePosc tilePos) {
		int x = tilePos.x();
		int y = tilePos.y();
		int z = tilePos.z();
		int l = this.getRelativeLayers(source.getBlockMetadata(x, y, z)) - 1;
		float f = (2 * (1 + l)) / 16.0F;
		return new AABBd(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
	}

	@Override
	public boolean isSolidRender() {
		return false;
	}
}
