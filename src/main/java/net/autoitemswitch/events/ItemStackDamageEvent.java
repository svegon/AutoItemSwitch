package net.autoitemswitch.events;

import java.util.function.Consumer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class ItemStackDamageEvent {
	private final Hand hand;
	private final int amount;
	private final LivingEntity entity;
	private final Consumer<LivingEntity> breakCallback;
	private final ItemStack stack;

	@SuppressWarnings("resource")
	public ItemStackDamageEvent(int amount, LivingEntity entity,
			Consumer<LivingEntity> breakCallback, ItemStack stack) {
		this.hand = MinecraftClient.getInstance().player.getOffHandStack() == stack
				? Hand.OFF_HAND : Hand.MAIN_HAND;
		this.amount = amount;
		this.entity = entity;
		this.breakCallback = breakCallback;
		this.stack = stack;
	}

	public Hand getHand() {
		return hand;
	}

	public Consumer<LivingEntity> getBreakCallback() {
		return breakCallback;
	}
	
	public int getAmount() {
		return amount;
	}

	public LivingEntity getEntity() {
		return entity;
	}

	public ItemStack getStack() {
		return stack;
	}	
}
