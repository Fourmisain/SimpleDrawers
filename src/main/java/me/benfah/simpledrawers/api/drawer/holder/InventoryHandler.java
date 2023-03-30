package me.benfah.simpledrawers.api.drawer.holder;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public class InventoryHandler implements SidedInventory
{

    ItemHolder holder;

    ItemStack[] prevStacks;

    ItemStack[] stacks = new ItemStack[] {ItemStack.EMPTY, ItemStack.EMPTY};

    public InventoryHandler(ItemHolder holder)
    {
        this.holder = holder;
    }

    @Override
    public int size()
    {
        return 2;
    }

    @Override
    public boolean isEmpty()
    {
        return holder.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot)
    {
        return stacks[slot];
    }

    @Override
    public ItemStack removeStack(int slot, int amount)
    {
        ItemStack stack = stacks[slot].copy();
        stack.setCount(amount);
        stacks[slot].decrement(amount);
        return stack;
    }

    @Override
    public ItemStack removeStack(int slot)
    {
        ItemStack stack = stacks[slot].copy();
        stacks[slot].setCount(0);
        return stack;
    }

    @Override
    public void setStack(int slot, ItemStack stack)
    {
        stacks[slot] = stack;
    }

    @Override
    public void markDirty()
    {
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player)
    {
        return false;
    }

    @Override
    public void clear()
    {
        holder.amount = 0;
    }

    @Override
    public int[] getAvailableSlots(Direction side)
    {
        return new int[] {0, 1};
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction dir)
    {
        return slot == 0 && (holder.shouldOffer(stack) && (holder.isEmpty() || holder.isVoiding() || holder.amount + stack.getCount() <= holder.getMaxAmount()));
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir)
    {
        return slot == 1 && !holder.isEmpty();
    }

    public void transferItems()
    {
        boolean markDirty = false;
        if(prevStacks != null)
        {
            int difference = prevStacks[1].getCount() - stacks[1].getCount();

            if(difference > 0)
            {
                holder.amount = holder.amount - difference;
                markDirty = true;
            }
        }

        if(!holder.isEmpty())
        {
            ItemStack stack = holder.getStack(false);
            stacks[1] = stack;
        }
        if(!stacks[0].isEmpty())
        {
            holder.offer(stacks[0]);
            stacks[0].setCount(0);
            markDirty = true;
        }

        if(markDirty)
            holder.blockEntity.sync();

        this.prevStacks = copyStackArray(stacks);

    }

    private ItemStack[] copyStackArray(ItemStack[] stacks)
    {
        ItemStack[] result = new ItemStack[stacks.length];
        for(int i = 0; i < stacks.length; i++)
            result[i] = stacks[i].copy();

        return result;
    }

}