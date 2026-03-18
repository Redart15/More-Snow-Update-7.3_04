package net.helinos.moresnow.model;

import net.helinos.moresnow.block.logic.BlockLogicSnowyFenceThin;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicFenceThin;
import net.minecraft.core.util.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import static net.helinos.moresnow.model.MoreSnowModels.zFactor;

public class BlockModelSnowyFenceThin<T extends BlockLogic, F extends BlockLogicFenceThin> extends BlockModelSnowy<T> {

    public BlockModelSnowyFenceThin(Block<T> block, BlockModel<?> layerModel, String texID) {
		super(block, layerModel, texID);
    }

    @Override
    public boolean render(Tessellator tessellator, int x, int y, int z) {
        BlockLogicSnowyFenceThin<?, ?> logic = (BlockLogicSnowyFenceThin<?, ?>) this.block.getLogic();
        int metadata = renderBlocks.blockAccess.getBlockMetadata(x, y, z);
        boolean somethingRendered = false;
		if(logic.getLayers(metadata) != 8){
			somethingRendered |= BlockModelDispatcher.getInstance().getDispatch(logic.storedBlock).render(tessellator, x, y, z);
		}
        // Render snow
        int layers = logic.getLayers(metadata);
        double height = layers * 2 / 16.0;
		AABB bounds = AABB.getTemporaryBB(zFactor, 0.0, zFactor, 1.0f - zFactor, height, 1.0f - zFactor);
        somethingRendered |= this.layerModel.renderStandardBlock(tessellator, bounds, x, y, z);
        return somethingRendered;
    }

	@Override
	public void renderLayerOnInventory(Tessellator tessellator, int metadata, float brightness, float alpha, @Nullable Integer lightmapCoordinate) {
		GL11.glTranslatef(0.0F, -0.25F, 0.0F);
		this.layerModel.renderBlockOnInventory(tessellator, metadata, brightness, alpha, lightmapCoordinate);
	}
}
