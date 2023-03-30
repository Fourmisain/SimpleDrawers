package me.benfah.simpledrawers.block.entity.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import me.benfah.simpledrawers.api.drawer.BlockAbstractDrawer;
import me.benfah.simpledrawers.api.drawer.holder.ItemHolder;
import me.benfah.simpledrawers.block.entity.BlockEntityQuadDrawer;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;

public class BlockEntityQuadDrawerRenderer extends BlockEntityAbstractDrawerRenderer<BlockEntityQuadDrawer>
{

    public BlockEntityQuadDrawerRenderer(BlockEntityRendererFactory.Context ctx)
    {
        super(ctx);
    }

    @Override
    public void render(BlockEntityQuadDrawer blockEntity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay)
    {
        light = calcLight(blockEntity);

        Direction facing = blockEntity.getCachedState().get(BlockAbstractDrawer.FACING);
        int count = 0;
        for(int i = 0; i < 2; i++)
        {
            for(int j = 0; j < 2; j++)
            {
                drawHolder(4 + j * 8, 4 + i * 8, blockEntity.getItemHolders().get(count), matrices, vertexConsumers, light,
                        overlay, facing);
                count++;
            }
        }

        matrices.pop();
        DiffuseLighting.disableForLevel(matrices.peek().getPositionMatrix());
        matrices.push();
    }

    private void drawHolder(int x, int y, ItemHolder holder, MatrixStack matrices,
                            VertexConsumerProvider vertexConsumers, int light, int overlay, Direction facing)
    {
        if(holder.isVoiding())
            drawVoid(17 - x, 16 - y + 2, matrices, vertexConsumers, light, overlay, facing);

        if(holder.isLocked())
            drawLock(16 - x, 16 - y + 2, matrices, vertexConsumers, light, overlay, facing);

        if(!holder.isEmpty() || holder.isLocked())
        {
            drawItem(x, y, 0.25F, holder.generateStack(1), matrices,
                    vertexConsumers, light, overlay, facing);
        }
    }

}
