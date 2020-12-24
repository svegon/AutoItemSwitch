package net.autoitemswitch.events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class StoppedUsingItemEvent {
	private final ItemStack stack;
	private final World world;
	private final LivingEntity user;
	private final int remainingUseTicks;
	
	public StoppedUsingItemEvent(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
		this.stack = stack;
		this.world = world;
		this.user = user;
		this.remainingUseTicks = remainingUseTicks;
	}
	
	public ItemStack getStack() {
		return stack;
	}

	public World getWorld() {
		return world;
	}

	public LivingEntity getUser() {
		return user;
	}

	public int getRemainingUseTicks() {
		return remainingUseTicks;
	}

}
