package net.autoitemswitch.utils;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class ItemUtils {
	public static boolean areItemsEqual(ItemStack stack1, ItemStack stack2) {
		return (stack1.isEmpty() && stack2.isEmpty()) || (stack1.getItem() == stack2.getItem()
				&& stack1.getTag().equals(stack2.getTag()));
	}
	
	public static boolean areItemsEqual(ItemStack stack, Item item) {
		return (stack.isEmpty() && item == null)
				|| (stack.getItem() == item && stack.getTag().isEmpty());
	}
	
	public static boolean areItemsEqual(Item item, ItemStack stack) {
		return (stack.isEmpty() && item == null)
				|| (stack.getItem() == item && stack.getTag().isEmpty());
	}
	
	public static boolean areItemsEqualIgnoreTag(ItemStack stack1, ItemStack stack2) {
		return (stack1.isEmpty() && stack2.isEmpty()) || stack1.getItem() == stack2.getItem();
	}
	
	public static boolean areItemsEqualIgnoreTag(ItemStack stack, Item item) {
		return (stack.isEmpty() && item == null) || stack.getItem() == item;
	}
	
	public static boolean areItemsEqualIgnoreTag(Item item, ItemStack stack) {
		return (stack.isEmpty() && item == null) || stack.getItem() == item;
	}
	
	public static int getSlotWithItem(Inventory inv, Item item) {
		for(int i = 0; i < inv.size(); i++) {
			ItemStack stack = (ItemStack)inv.getStack(i);
			
			if(!stack.isEmpty() && stack.getItem() == item)
				return i;
		}
		
		return -1;
	}

	// finds the slot with a reffered stack in inventory
	public static int getSlotWithStack(Inventory inv, ItemStack stack) {
		for(int i = 0; i < inv.size(); i++) {
			if(inv.getStack(i).hashCode() == stack.hashCode())
				return i;
		}
		
		return -1;
	}
}
