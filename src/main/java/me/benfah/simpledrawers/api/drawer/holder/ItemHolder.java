package me.benfah.simpledrawers.api.drawer.holder;

import me.benfah.simpledrawers.api.border.BorderRegistry;
import me.benfah.simpledrawers.api.drawer.blockentity.BlockEntityAbstractDrawer;
import me.benfah.simpledrawers.utils.NumberUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemHolder
{

    protected Item itemType;
    protected NbtCompound tag;
    protected int amount;
    protected int maxStacks;
    protected BlockEntityAbstractDrawer blockEntity;

    private final InventoryHandler handler;

    private boolean voiding = false;
    private boolean locked = false;

    public ItemHolder(int maxStacks, BlockEntityAbstractDrawer blockEntity)
    {
        this();
        this.maxStacks = maxStacks;
        this.blockEntity = blockEntity;
    }

    private ItemHolder()
    {
        this.handler = new InventoryHandler(this);
    }

    public String getDisplayAmount()
    {
        return NumberUtils.displayNumber(amount);
    }

    public ActionResult offer(ItemStack stack)
    {
        if(shouldOffer(stack))
        {
            if(itemType == null || (amount <= 0 && !locked))
            {
                itemType = stack.getItem();
                amount = stack.getCount();
                tag = stack.getNbt();

                stack.setCount(0);
                blockEntity.sync();
                return ActionResult.SUCCESS;
            }
            if(isStackEqual(stack))
            {
                int newAmount = Math.min(amount + stack.getCount(), getMaxAmount());
                int stackSize = voiding? 0 : (amount + stack.getCount()) - newAmount;
                stack.setCount(stackSize);
                amount = newAmount;
                blockEntity.sync();
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.CONSUME;
    }

    public ActionResult offerAll(ItemStack stack, PlayerEntity player)
    {
        Inventory inventory = player.getInventory();
        for (int i = 0; i < inventory.size(); i++)
        {
            ItemStack currentStack = inventory.getStack(i);
            if (!currentStack.isEmpty() && isStackEqual(currentStack))
            {
                int newAmount = Math.min(amount + currentStack.getCount(), getMaxAmount());
                int stackSize = voiding? 0 : (amount + stack.getCount()) - newAmount;
                currentStack.setCount(stackSize);
                amount = newAmount;
                blockEntity.sync();
                if (!voiding && amount == getMaxAmount())
                {
                    break;
                }
            }
        }
        return ActionResult.CONSUME;
    }

    public boolean shouldOffer(ItemStack stack)
    {
        return stack != null && !stack.isEmpty()
                && ((itemType == null || (amount <= 0 && !locked)) || isStackEqual(stack));
    }

    public int getMaxAmount()
    {
        return itemType.getMaxCount() * getMaxStacks();
    }

    public int getMaxStacks()
    {
        return maxStacks * blockEntity.getCachedState().get(BorderRegistry.BORDER_TYPE).getStackMultiplier();
    }

    public boolean tryInsertIntoInventory(PlayerEntity player, boolean singleItem)
    {
        ItemStack stack = getStack(singleItem);
        if(!stack.isEmpty())
        {
            amount = amount - stack.getCount();
            boolean result = player.getInventory().insertStack(stack);
            amount = amount + stack.getCount();

            if(isEmpty() && !locked)
                clearData();

            blockEntity.sync();
            return result;
        }
        return false;
    }

    public ItemStack getStack(boolean singleItem)
    {
        if(!isEmpty())
        {
            if(singleItem)
            {
                return generateStack(1);
            }
            int stackAmount = Math.min(itemType.getMaxCount(), amount);

            return generateStack(stackAmount);
        }

        return ItemStack.EMPTY;
    }

    public ItemStack generateStack(int amount)
    {
        ItemStack stack = new ItemStack(itemType, amount);
        if(tag != null)
            stack.setNbt(tag);
        return stack;
    }

    public boolean isEmpty()
    {
        return amount <= 0;
    }

    public boolean isVoiding()
    {
        return voiding;
    }

    public boolean isLocked()
    {
        return locked;
    }

    public void clearData()
    {
        this.locked = false;
        this.amount = 0;
        this.itemType = null;
    }

    public void setVoiding(boolean voiding)
    {
        this.voiding = voiding;

        blockEntity.sync();
    }

    public void setLocked(boolean locked)
    {
        this.locked = locked;

        if(isEmpty() && !locked)
            clearData();

        blockEntity.sync();
    }

    public NbtCompound toNBT(NbtCompound tag)
    {
        tag.put("Item", serializeItemData(new NbtCompound()));
        tag.putInt("MaxAmount", maxStacks);
        tag.putBoolean("Voiding", voiding);
        tag.putBoolean("Locked", locked);

        return tag;
    }

    public static ItemHolder fromNBT(NbtCompound tag, BlockEntityAbstractDrawer blockEntity)
    {
        ItemHolder holder = new ItemHolder();

        holder.deserializeItemData(tag.getCompound("Item"));
        holder.maxStacks = tag.getInt("MaxAmount");
        holder.blockEntity = blockEntity;

        if(tag.contains("Voiding"))
            holder.voiding = tag.getBoolean("Voiding");

        if(tag.contains("Locked"))
            holder.locked = tag.getBoolean("Locked");

        return holder;
    }

    public NbtCompound serializeItemData(NbtCompound tag)
    {
        if(itemType != null)
        {
            tag.putString("id", Registry.ITEM.getId(itemType).toString());
            tag.putInt("Count", amount);
            if(this.tag != null && !this.tag.isEmpty())
                tag.put("tag", this.tag);
        } else
            tag.putString("id", Registry.ITEM.getDefaultId().toString());

        return tag;
    }

    public void deserializeItemData(NbtCompound tag)
    {
        Identifier id = new Identifier(tag.getString("id"));

        itemType = Registry.ITEM.get(id);
        if(itemType != Items.AIR)
        {
            amount = tag.getInt("Count");
            if(tag.contains("tag") && !tag.getCompound("tag").isEmpty())
                this.tag = tag.getCompound("tag");
        } else
            itemType = null;

    }

    public Item getItemType()
    {
        return itemType;
    }

    public int getAmount()
    {
        return amount;
    }

    public NbtCompound getTag()
    {
        return tag;
    }

    public boolean isFull()
    {
        return amount >= getMaxAmount();
    }

    public InventoryHandler getInventoryHandler()
    {
        return handler;
    }

    public boolean isStackEqual(ItemStack stack)
    {

        if(stack.getItem().equals(itemType))
        {
            NbtCompound tag1 = this.tag == null ? new NbtCompound() : tag;
            NbtCompound tag2 = stack.getNbt() == null ? new NbtCompound() : stack.getNbt();
            return tag1.equals(tag2);
        }
        return false;
    }

}
