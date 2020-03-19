package me.benfah.simpledrawers.init;

import com.mojang.datafixers.util.Either;

import me.benfah.simpledrawers.SimpleDrawersMod;
import me.benfah.simpledrawers.item.ItemDrawerFullUpgrade;
import me.benfah.simpledrawers.item.ItemDrawerUpgrade;
import me.benfah.simpledrawers.item.ItemKey;
import me.benfah.simpledrawers.models.border.Border;
import me.benfah.simpledrawers.models.border.Border.BorderType;
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

	public static ItemGroup SD_GROUP;

	public static ItemKey LOCK_KEY;
	
	public static ItemDrawerUpgrade BASIC_IRON_UPGRADE;
	public static ItemDrawerUpgrade IRON_GOLD_UPGRADE;
	public static ItemDrawerUpgrade GOLD_DIAMOND_UPGRADE;
	public static ItemDrawerUpgrade DIAMOND_EMERALD_UPGRADE;

	public static ItemDrawerFullUpgrade FULL_GOLD_UPGRADE;
	public static ItemDrawerFullUpgrade FULL_DIAMOND_UPGRADE;
	public static ItemDrawerFullUpgrade FULL_EMERALD_UPGRADE;
	
	public static void init()
	{
		initItemGroup();
		OAK_DRAWER = registerBlockItem(SDBlocks.OAK_DRAWER);
		BIRCH_DRAWER = registerBlockItem(SDBlocks.BIRCH_DRAWER);
		SPRUCE_DRAWER = registerBlockItem(SDBlocks.SPRUCE_DRAWER);
		JUNGLE_DRAWER = registerBlockItem(SDBlocks.JUNGLE_DRAWER);
		ACACIA_DRAWER = registerBlockItem(SDBlocks.ACACIA_DRAWER);
		DARK_OAK_DRAWER = registerBlockItem(SDBlocks.DARK_OAK_DRAWER);
		
		BASIC_IRON_UPGRADE = register("iron_upgrade", new ItemDrawerUpgrade(new Settings().group(SD_GROUP).maxCount(1), Either.right(BorderType.BASIC), Border.IRON_BORDER));
		IRON_GOLD_UPGRADE = register("gold_upgrade", new ItemDrawerUpgrade(new Settings().group(SD_GROUP).maxCount(1), Either.left(Border.IRON_BORDER), Border.GOLD_BORDER));
		GOLD_DIAMOND_UPGRADE = register("diamond_upgrade", new ItemDrawerUpgrade(new Settings().group(SD_GROUP).maxCount(1), Either.left(Border.GOLD_BORDER), Border.DIAMOND_BORDER));
		DIAMOND_EMERALD_UPGRADE = register("emerald_upgrade", new ItemDrawerUpgrade(new Settings().group(SD_GROUP).maxCount(1), Either.left(Border.DIAMOND_BORDER), Border.EMERALD_BORDER));
		
		FULL_GOLD_UPGRADE = register("full_gold_upgrade", new ItemDrawerFullUpgrade(new Settings().group(SD_GROUP).maxCount(1), Border.GOLD_BORDER));
		FULL_DIAMOND_UPGRADE = register("full_diamond_upgrade", new ItemDrawerFullUpgrade(new Settings().group(SD_GROUP).maxCount(1), Border.DIAMOND_BORDER));
		FULL_EMERALD_UPGRADE = register("full_emerald_upgrade", new ItemDrawerFullUpgrade(new Settings().group(SD_GROUP).maxCount(1), Border.EMERALD_BORDER));

		LOCK_KEY = register("lock_key", new ItemKey(new Settings().group(SD_GROUP), (itemHolder) ->
		{
			if (!itemHolder.isEmpty() || itemHolder.isLocked())
			{
				itemHolder.setLocked(!itemHolder.isLocked());
			}
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
