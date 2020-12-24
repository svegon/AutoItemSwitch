package net.autoitemswitch.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemUseEvent {
	private final PlayerEntity player;
	private final World world;
	private final Hand hand;
	private final ItemStack stack;
	
	public ItemUseEvent(PlayerEntity player, World world, Hand hand, ItemStack stack) {
		this.stack = stack;
		this.world = world;
		this.player = player;
		this.hand = hand;
	}
	
	public ItemStack getStack() {
		return stack;
	}
	
	public World getWorld() {
		return world;
	}

	public PlayerEntity getPlayer() {
		return player;
	}
	
	public Hand getHand() {
		return hand;
	}
}
