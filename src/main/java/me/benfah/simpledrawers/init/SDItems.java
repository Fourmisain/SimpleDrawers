package me.benfah.simpledrawers.init;

import com.mojang.datafixers.util.Either;
import me.benfah.simpledrawers.SimpleDrawersMod;
import me.benfah.simpledrawers.api.border.Border;
import me.benfah.simpledrawers.api.border.Border.BorderType;
import me.benfah.simpledrawers.item.*;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class SDItems
{

    private SDItems()
    {
    }

    public static BlockItem OAK_DRAWER;
    public static BlockItem BIRCH_DRAWER;
    public static BlockItem SPRUCE_DRAWER;
    public static BlockItem JUNGLE_DRAWER;
    public static BlockItem ACACIA_DRAWER;
    public static BlockItem DARK_OAK_DRAWER;
    public static BlockItem CRIMSON_DRAWER;
    public static BlockItem WARPED_DRAWER;

    public static BlockItem HALF_OAK_DRAWER;
    public static BlockItem HALF_BIRCH_DRAWER;
    public static BlockItem HALF_SPRUCE_DRAWER;
    public static BlockItem HALF_JUNGLE_DRAWER;
    public static BlockItem HALF_ACACIA_DRAWER;
    public static BlockItem HALF_DARK_OAK_DRAWER;
    public static BlockItem HALF_CRIMSON_DRAWER;
    public static BlockItem HALF_WARPED_DRAWER;

    public static BlockItem QUAD_OAK_DRAWER;
    public static BlockItem QUAD_BIRCH_DRAWER;
    public static BlockItem QUAD_SPRUCE_DRAWER;
    public static BlockItem QUAD_JUNGLE_DRAWER;
    public static BlockItem QUAD_ACACIA_DRAWER;
    public static BlockItem QUAD_DARK_OAK_DRAWER;
    public static BlockItem QUAD_CRIMSON_DRAWER;
    public static BlockItem QUAD_WARPED_DRAWER;

    public static BlockItem DRAWER_CONTROLLER;

    public static ItemGroup SD_GROUP;

    public static ItemKey LOCK_KEY;

    public static ItemDrawerUpgrade BASIC_IRON_UPGRADE;
    public static ItemDrawerUpgrade IRON_GOLD_UPGRADE;
    public static ItemDrawerUpgrade GOLD_DIAMOND_UPGRADE;
    public static ItemDrawerUpgrade DIAMOND_EMERALD_UPGRADE;

    public static ItemDrawerFullUpgrade FULL_GOLD_UPGRADE;
    public static ItemDrawerFullUpgrade FULL_DIAMOND_UPGRADE;
    public static ItemDrawerFullUpgrade FULL_EMERALD_UPGRADE;

    public static ItemTape TAPE;
    public static ItemPackagedDrawer PACKAGED_DRAWER;

    public static ItemVoidUpgrade VOID_UPGRADE;

    public static void init()
    {
        initItemGroup();
        OAK_DRAWER = registerBlockItem(SDBlocks.OAK_DRAWER);
        BIRCH_DRAWER = registerBlockItem(SDBlocks.BIRCH_DRAWER);
        SPRUCE_DRAWER = registerBlockItem(SDBlocks.SPRUCE_DRAWER);
        JUNGLE_DRAWER = registerBlockItem(SDBlocks.JUNGLE_DRAWER);
        ACACIA_DRAWER = registerBlockItem(SDBlocks.ACACIA_DRAWER);
        DARK_OAK_DRAWER = registerBlockItem(SDBlocks.DARK_OAK_DRAWER);
        CRIMSON_DRAWER = registerBlockItem(SDBlocks.CRIMSON_DRAWER);
        WARPED_DRAWER = registerBlockItem(SDBlocks.WARPED_DRAWER);

        HALF_OAK_DRAWER = registerBlockItem(SDBlocks.HALF_OAK_DRAWER);
        HALF_BIRCH_DRAWER = registerBlockItem(SDBlocks.HALF_BIRCH_DRAWER);
        HALF_SPRUCE_DRAWER = registerBlockItem(SDBlocks.HALF_SPRUCE_DRAWER);
        HALF_JUNGLE_DRAWER = registerBlockItem(SDBlocks.HALF_JUNGLE_DRAWER);
        HALF_ACACIA_DRAWER = registerBlockItem(SDBlocks.HALF_ACACIA_DRAWER);
        HALF_DARK_OAK_DRAWER = registerBlockItem(SDBlocks.HALF_DARK_OAK_DRAWER);
        HALF_CRIMSON_DRAWER = registerBlockItem(SDBlocks.HALF_CRIMSON_DRAWER);
        HALF_WARPED_DRAWER = registerBlockItem(SDBlocks.HALF_WARPED_DRAWER);

        QUAD_OAK_DRAWER = registerBlockItem(SDBlocks.QUAD_OAK_DRAWER);
        QUAD_BIRCH_DRAWER = registerBlockItem(SDBlocks.QUAD_BIRCH_DRAWER);
        QUAD_SPRUCE_DRAWER = registerBlockItem(SDBlocks.QUAD_SPRUCE_DRAWER);
        QUAD_JUNGLE_DRAWER = registerBlockItem(SDBlocks.QUAD_JUNGLE_DRAWER);
        QUAD_ACACIA_DRAWER = registerBlockItem(SDBlocks.QUAD_ACACIA_DRAWER);
        QUAD_DARK_OAK_DRAWER = registerBlockItem(SDBlocks.QUAD_DARK_OAK_DRAWER);
        QUAD_CRIMSON_DRAWER = registerBlockItem(SDBlocks.QUAD_CRIMSON_DRAWER);
        QUAD_WARPED_DRAWER = registerBlockItem(SDBlocks.QUAD_WARPED_DRAWER);

        DRAWER_CONTROLLER = registerBlockItem(SDBlocks.DRAWER_CONTROLLER);

        BASIC_IRON_UPGRADE = register("iron_upgrade", new ItemDrawerUpgrade(new Settings().group(SD_GROUP).maxCount(1), Either.right(BorderType.BASIC), Border.IRON_BORDER));
        IRON_GOLD_UPGRADE = register("gold_upgrade", new ItemDrawerUpgrade(new Settings().group(SD_GROUP).maxCount(1), Either.left(Border.IRON_BORDER), Border.GOLD_BORDER));
        GOLD_DIAMOND_UPGRADE = register("diamond_upgrade", new ItemDrawerUpgrade(new Settings().group(SD_GROUP).maxCount(1), Either.left(Border.GOLD_BORDER), Border.DIAMOND_BORDER));
        DIAMOND_EMERALD_UPGRADE = register("emerald_upgrade", new ItemDrawerUpgrade(new Settings().group(SD_GROUP).maxCount(1), Either.left(Border.DIAMOND_BORDER), Border.EMERALD_BORDER));

        FULL_GOLD_UPGRADE = register("full_gold_upgrade", new ItemDrawerFullUpgrade(new Settings().group(SD_GROUP).maxCount(1), Border.GOLD_BORDER));
        FULL_DIAMOND_UPGRADE = register("full_diamond_upgrade", new ItemDrawerFullUpgrade(new Settings().group(SD_GROUP).maxCount(1), Border.DIAMOND_BORDER));
        FULL_EMERALD_UPGRADE = register("full_emerald_upgrade", new ItemDrawerFullUpgrade(new Settings().group(SD_GROUP).maxCount(1), Border.EMERALD_BORDER));

        LOCK_KEY = register("lock_key", new ItemKey(new Settings().group(SD_GROUP), (itemHolder) ->
        {
            if(!itemHolder.isEmpty() || itemHolder.isLocked())
            {
                itemHolder.setLocked(!itemHolder.isLocked());
            }
        }));

        TAPE = register("tape", new ItemTape(new Settings().maxCount(1).group(SD_GROUP).maxDamage(3)));
        PACKAGED_DRAWER = register("packaged_drawer", new ItemPackagedDrawer(new Settings().maxCount(1)));

        VOID_UPGRADE = register("void_upgrade", new ItemVoidUpgrade(new Settings().group(SD_GROUP), (itemHolder) ->
        {
            itemHolder.setVoiding(!itemHolder.isVoiding());
        }));
    }

    private static void initItemGroup()
    {
        SD_GROUP = FabricItemGroupBuilder.build(new Identifier(SimpleDrawersMod.MOD_ID, "simple_drawers"),
                () -> new ItemStack(OAK_DRAWER));
    }

    public static BlockItem registerBlockItem(Block block)
    {
        BlockItem blockItem = new BlockItem(block, new Settings().group(SD_GROUP));
        Identifier identifier = Registry.BLOCK.getId(block);
        return Registry.register(Registry.ITEM, identifier, blockItem);
    }

    public static <T extends Item> T register(String name, T item)
    {
        Registry.register(Registry.ITEM, new Identifier(SimpleDrawersMod.MOD_ID, name), item);
        return item;
    }

}
