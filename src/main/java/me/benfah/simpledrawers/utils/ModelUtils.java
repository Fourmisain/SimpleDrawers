package me.benfah.simpledrawers.utils;

import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.Registry;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ModelUtils
{
    public static Random RANDOM = Random.createLocal();

    public static final Function<Entry<Property<?>, Comparable<?>>, String> PROPERTY_MAP_PRINTER = new Function<>() {
        public String apply(Map.Entry<Property<?>, Comparable<?>> entry) {
            if (entry == null) {
                return "<NULL>";
            } else {
                Property<?> property = entry.getKey();
                return property.getName() + "=" + this.nameValue(property, entry.getValue());
            }
        }

        private <T extends Comparable<T>> String nameValue(Property<T> property, Comparable<?> value) {
            return property.name((T) value);
        }
    };

    public static void loadSpecialModels()
    {

        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) ->
        {
            for(SpecialModel model : SpecialModel.values())
                out.accept(model.getIdentifier());
        });

        ModelLoadingRegistry.INSTANCE.registerVariantProvider(manager -> ((modelId, context) ->
        {
            for(SpecialModel m : SpecialModel.values())
                if(modelId.equals(m.getIdentifier()))
                    return context.loadModel(new Identifier(modelId.getNamespace(), modelId.getPath()));
            return null;
        }));


    }

    public static void drawSpecialTexture(MatrixStack matrices, VertexConsumerProvider consumers, BakedModel model,
                                          int light, int overlay)
    {
        renderQuads(matrices.peek(), consumers.getBuffer(RenderLayer.getTranslucent()),
                model.getQuads(null, null, ModelUtils.RANDOM), light, overlay);

    }

    private static void renderQuads(MatrixStack.Entry entry, VertexConsumer vertexConsumer, List<BakedQuad> list,
                                    int light, int overlay)
    {
        for(BakedQuad quad : list)
            vertexConsumer.quad(entry, quad, 1F, 1F, 1F, light, overlay);
    }

    public enum SpecialModel
    {

        LOCK(new ModelIdentifier("simpledrawers:attributes/lock"));


        private ModelIdentifier identifier;

        SpecialModel(ModelIdentifier identifier)
        {
            this.identifier = identifier;
        }

        public ModelIdentifier getIdentifier()
        {
            return identifier;
        }

        public BakedModel getBakedModel()
        {
            return MinecraftClient.getInstance().getBakedModelManager().getModel(identifier);
        }

    }

    public static boolean identifiersEqual(Identifier id1, Identifier id2)
    {
        return id1.getNamespace().equals(id2.getNamespace()) && id1.getPath().equals(id2.getPath());
    }

    public static String variantMapToString(Map<Property<?>, Comparable<?>> map)
    {
        return map.entrySet().stream().map(PROPERTY_MAP_PRINTER).collect(Collectors.joining(","));
    }

    public static ModelIdentifier getStateModelIdentifier(BlockState state)
    {
        Identifier id = Registry.BLOCK.getId(state.getBlock());
        String variant = variantMapToString(state.getEntries());
        return new ModelIdentifier(id, variant);
    }

    public static BakedModel getBakedDrawerModel(BlockState state)
    {
        return MinecraftClient.getInstance().getBakedModelManager().getModel(getStateModelIdentifier(state));
    }
}
