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
import org.jetbrains.annotations.Nullable;

public abstract class BlockModelSnowy<T extends BlockLogic> extends BlockModelStandard<T> {
	private final BlockModel<?> model;
	private final IconCoordinate iconCoordinate;

    private static final IconCoordinate SNOW_TEXTURE = TextureRegistry.getTexture("minecraft:block/block_snow");
    protected boolean renderingSnow = false;

    protected BlockModelSnowy(Block<T> block) {
        super(block);
		this.model = BlockModelDispatcher.getInstance().getDispatch(Blocks.LAYER_SNOW);
		this.iconCoordinate = TextureRegistry.getTexture("minecraft:block/block_snow");
    }

    @Override
    public IconCoordinate getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        if (this.renderingSnow) {
            return SNOW_TEXTURE;
        }
        int metadata = blockAccess.getBlockMetadata(x, y, z);
        return this.getBlockTextureFromSideAndMetadata(side, metadata);
    }

    @Override
    public IconCoordinate getBlockTextureFromSideAndMetadata(Side side, int metadata) {
        BlockLogicSnowy<?> logic = (BlockLogicSnowy<?>) this.block.getLogic();
        int storedBlockID = logic.getStoredBlockId(metadata);
        Block<?> storedBlock = Blocks.getBlock(storedBlockID);
        int storedBlockMetadata = logic.getStoredBlockMetadata(metadata);
        try {
            return BlockModelDispatcher.getInstance().getDispatch(storedBlock).getBlockTextureFromSideAndMetadata(side, storedBlockMetadata);
        } catch (NullPointerException _exception) {
            return BLOCK_TEXTURE_UNASSIGNED;
        }
    }

	@Override
	public void renderBlockOnInventory(Tessellator tessellator, int metadata, float brightness, float alpha, @Nullable Integer lightmapCoordinate) {
		Block<?> storedBlock = ((BlockLogicSnowy<?>) this.block.getLogic()).getStoredBlock();
		BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(storedBlock);
		model.renderBlockOnInventory(tessellator, metadata, brightness, alpha, lightmapCoordinate);
		this.renderLayerOnInventory(tessellator, metadata, brightness, alpha, lightmapCoordinate);
	}

	public void renderLayerOnInventory(Tessellator tessellator, int metadata, float brightness, float alpha, @Nullable Integer lightmapCoordinate) {
		BlockModelDispatcher.getInstance().getDispatch(Blocks.LAYER_SNOW).renderBlockOnInventory(tessellator, metadata, brightness, alpha, lightmapCoordinate);
	}

	public final void startLayerRendering(){
		this.renderingSnow = true;
	}

	public final void stopLayerRendering(){
		this.renderingSnow = false;
	}
}
