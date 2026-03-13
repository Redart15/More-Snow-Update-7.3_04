package net.helinos.moresnow.model;

import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.minecraft.client.render.block.color.BlockColor;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.BlockModelPlanksPainted;
import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

public class BlockModelSnowySlab<T extends BlockLogic> extends BlockModelSnowy<T> {
    public BlockModelSnowySlab(Block<T> block) {
        super(block);
    }

    @Override
    public boolean render(Tessellator tessellator, int x, int y, int z) {
        int metadata = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
        // Render the slab
        AABB bounds = AABB.getTemporaryBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
        boolean somethingRendered = this.renderStandardBlock(tessellator, bounds, x, y, z);
        // Render the snow
		this.startLayerRendering();
        int layers = ((BlockLogicSnowy<?>) block.getLogic()).getLayers(metadata);
        double height = layers * 2 / 16.0;
        bounds.set(0.0, 0.5, 0.0, 1.0, 0.5 + height, 1.0);
        somethingRendered |= this.renderStandardBlock(tessellator, bounds, x, y, z);
		this.stopLayerRendering();
        return somethingRendered;
    }

	@Override
	public void renderLayerOnInventory(Tessellator tessellator, int metadata, float brightness, float alpha, @Nullable Integer lightmapCoordinate) {
		GL11.glTranslatef(0.0F, 0.25F, 0.0F);
		BlockModelDispatcher.getInstance().getDispatch(Blocks.LAYER_SNOW).renderBlockOnInventory(tessellator, metadata, brightness, alpha, lightmapCoordinate);
	}
}
