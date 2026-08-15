package net.helinos.moresnow.model;

import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelLayer;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.helper.Side;
import org.jetbrains.annotations.NotNull;

public abstract class BlockModelSnowy<T extends BlockLogicSnowy<?>> extends BlockModelStandard<T> {
	protected final BlockModelStandard<?> layerModel;

    protected BlockModelSnowy(Block<T> block, BlockModel<?> layerModel) {
        super(block);
		this.layerModel = new BlockModelLayer<>(layerModel.block)
			.setTex(layerModel.getParticleTexture(Side.TOP, 0), Side.TOP)
			.setTex(layerModel.getParticleTexture(Side.BOTTOM, 0), Side.BOTTOM)
			.setTex(layerModel.getParticleTexture(Side.NORTH, 0), Side.NORTH)
			.setTex(layerModel.getParticleTexture(Side.SOUTH, 0), Side.SOUTH)
			.setTex(layerModel.getParticleTexture(Side.WEST, 0), Side.WEST)
			.setTex(layerModel.getParticleTexture(Side.EAST, 0), Side.EAST);
	}

    @Override
    public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int metadata) {
        BlockLogicSnowy<?> logic = this.block.getLogic();
        int storedBlockID = logic.storedBlockId(metadata);
        Block<?> storedBlock = Blocks.getBlock(storedBlockID);
        int storedBlockMetadata = logic.storedBlockMetadata(metadata);
        try {
            return BlockModelDispatcher.getInstance().getDispatch(storedBlock).getParticleTexture(side, storedBlockMetadata);
        } catch (NullPointerException e) {
            return BLOCK_TEXTURE_UNASSIGNED;
        }
    }

	@Override
	public void renderStandalone(@NotNull TessellatorGeneral tessellator, int metadata, byte lightIndex) {
		Block<?> storedBlock = this.block.getLogic().storedBlock();
		BlockModel<?> storedBlockModel = BlockModelDispatcher.getInstance().getDispatch(storedBlock);
		storedBlockModel.renderStandalone(tessellator, metadata,lightIndex);
		this.renderLayerOnInventory(tessellator, metadata,lightIndex);
	}



	public void renderLayerOnInventory(@NotNull TessellatorGeneral tessellator, int metadata, byte lightIndex) {
		this.layerModel.renderStandalone(tessellator, metadata,lightIndex);
	}
}
