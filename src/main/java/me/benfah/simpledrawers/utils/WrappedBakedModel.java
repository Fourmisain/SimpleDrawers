package me.benfah.simpledrawers.utils;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WrappedBakedModel implements BakedModel
{
    List<BakedModel> modelList;

    public WrappedBakedModel(BakedModel... models)
    {
        this.modelList = Arrays.asList(models);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random)
    {
        ArrayList<BakedQuad> quads = new ArrayList<>();
        for(BakedModel model : modelList)
        {
            quads.addAll(model.getQuads(state, face, random));
        }
        return quads;
    }

    @Override
    public boolean useAmbientOcclusion()
    {
        return modelList.get(0).useAmbientOcclusion();
    }

    @Override
    public boolean hasDepth()
    {
        return modelList.get(0).hasDepth();
    }

    @Override
    public boolean isSideLit()
    {
        return modelList.get(0).isSideLit();
    }

    @Override
    public boolean isBuiltin()
    {
        return modelList.get(0).isBuiltin();
    }

    @Override
    public Sprite getParticleSprite()
    {
        return modelList.get(0).getParticleSprite();
    }

    @Override
    public ModelTransformation getTransformation()
    {
        return modelList.get(0).getTransformation();
    }

    @Override
    public ModelOverrideList getOverrides()
    {
        return modelList.get(0).getOverrides();
    }


}
