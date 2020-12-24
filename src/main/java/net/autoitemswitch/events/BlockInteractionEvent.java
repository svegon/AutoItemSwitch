package net.autoitemswitch.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class BlockInteractionEvent extends ItemUseEvent {
	private final BlockHitResult hitResult;
	
	public BlockInteractionEvent(PlayerEntity player, World world, Hand hand,
			BlockHitResult hitResult, ItemStack stack) {
		super(player, world, hand, stack);
		this.hitResult = hitResult;
	}

	public BlockHitResult getHitResult() {
		return hitResult;
	}
}
