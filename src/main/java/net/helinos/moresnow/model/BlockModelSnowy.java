package net.helinos.moresnow.model;

import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BlockModelSnowy<T extends BlockLogic> extends BlockModelStandard<T> {
	protected final BlockModel<?> layerModel;
	protected final IconCoordinate iconCoordinate;
    protected boolean renderingSnow = false;

    protected BlockModelSnowy(Block<T> block, BlockModel<?> layerModel, String texID) {
        super(block);
		this.layerModel = layerModel;
		this.iconCoordinate = TextureRegistry.getTexture(texID);
	}

	@Override
	public @Nullable IconCoordinate getBlockTexture(@NotNull WorldSource source, @NotNull TilePosc tilePos, @NotNull Side side) {
		if (this.renderingSnow) {
			return this.iconCoordinate;
		}
		int metadata = source.getBlockData(tilePos);
		return this.getBlockTextureFromSideAndMetadata(side, metadata);
	}

	/// TODO: figure out if this is needed
    @Override
    public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int metadata) {
        BlockLogicSnowy<?> logic = (BlockLogicSnowy<?>) this.block.getLogic();
        int storedBlockID = logic.getStoredBlockId(metadata);
        Block<?> storedBlock = Blocks.getBlock(storedBlockID);
        int storedBlockMetadata = logic.getStoredBlockMetadata(metadata);
        try {
            return BlockModelDispatcher.getInstance().getDispatch(storedBlock).getBlockTextureFromSideAndMetadata(side, storedBlockMetadata);
        } catch (NullPointerException e) {
            return BLOCK_TEXTURE_UNASSIGNED;
        }
    }

	@Override
	public void renderBlockOnInventory(Tessellator tessellator, int metadata, float brightness, float alpha, @Nullable Integer lightmapCoordinate) {
		Block<?> storedBlock = ((BlockLogicSnowy<?>) this.block.getLogic()).getStoredBlock();
		BlockModel<?> storedBlockModel = BlockModelDispatcher.getInstance().getDispatch(storedBlock);
		storedBlockModel.renderBlockOnInventory(tessellator, metadata, brightness, alpha, lightmapCoordinate);
		this.renderLayerOnInventory(tessellator, metadata, brightness, alpha, lightmapCoordinate);
	}

	public void renderLayerOnInventory(Tessellator tessellator, int metadata, float brightness, float alpha, @Nullable Integer lightmapCoordinate) {
		layerModel.renderBlockOnInventory(tessellator, metadata, brightness, alpha, lightmapCoordinate);
	}
}
