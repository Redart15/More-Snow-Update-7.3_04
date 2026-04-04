package net.helinos.moresnow.model;

import net.helinos.moresnow.block.logic.BlockLogicSnowy;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

public class BlockModelSnowySlab<T extends BlockLogic> extends BlockModelSnowy<T> {
    public BlockModelSnowySlab(Block<T> block, BlockModel<?> layerModel, String texID) {
		super(block, layerModel, texID);
    }

    @Override
    public boolean render(Tessellator tessellator, int x, int y, int z) {
        int metadata = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
        // Render the slab
        AABB bounds = AABB.getTemporaryBB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
        boolean somethingRendered = this.renderStandardBlock(tessellator, bounds, x, y, z);
        // Render the snow
        int layers = ((BlockLogicSnowy<?>) block.getLogic()).getLayers(metadata);
        double height = layers * 2 / 16.0;
        bounds.set(0.0, 0.5, 0.0, 1.0, 0.5 + height, 1.0);
        somethingRendered |= this.layerModel.renderStandardBlock(tessellator, bounds, x, y, z);
        return somethingRendered;
    }

	@Override
	public void renderLayerOnInventory(Tessellator tessellator, int metadata, float brightness, float alpha, @Nullable Integer lightmapCoordinate) {
		GL11.glTranslatef(0.0F, 0.25F, 0.0F);
		this.layerModel.renderBlockOnInventory(tessellator, metadata, brightness, alpha, lightmapCoordinate);
	}
}
