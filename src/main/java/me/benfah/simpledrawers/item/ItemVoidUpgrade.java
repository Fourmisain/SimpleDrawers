package me.benfah.simpledrawers.item;

import me.benfah.simpledrawers.api.drawer.blockentity.BlockEntityAbstractDrawer;
import me.benfah.simpledrawers.api.drawer.holder.ItemHolder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;

import java.util.function.Consumer;

public class ItemVoidUpgrade extends Item implements DrawerInteractable {
    private Consumer<ItemHolder> action;

    public ItemVoidUpgrade(Settings settings, Consumer<ItemHolder> action){
        super(settings);
        this.action = action;
    }

    @Override
    public void interact(BlockEntityAbstractDrawer blockEntity, PlayerEntity player, ItemHolder holder)
    {
        action.accept(holder);
    }
}
