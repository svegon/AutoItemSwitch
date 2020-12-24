package net.autoitemswitch.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.autoitemswitch.SharedVariables;
import net.autoitemswitch.events.BlockInteractionEvent;
import net.autoitemswitch.events.ItemUseEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

@Mixin(ServerPlayerInteractionManager.class)
public abstract class ServerPlayerInteractionManagerMixin {
	private ItemStack interactedStack;
	private ItemStack blockInteractedStack;
	
	@Inject(at = {@At(value = "INVOKE",
			target = "Lnet/minecraft/item/ItemStack;use(Lnet/minecraft/world/World;"
					+ "Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)"
					+ "Lnet/minecraft/util/TypedActionResult;",
			ordinal = 0)}, method = {"interactItem"}, cancellable = true)
	private void preInteractItem(ServerPlayerEntity player, World world, ItemStack stack, Hand hand,
			CallbackInfoReturnable<ActionResult> cir) {
		interactedStack = stack.copy();
	}
	
	@Inject(at = {@At(value = "INVOKE",
			target = "Lnet/minecraft/item/ItemStack;use(Lnet/minecraft/world/World;"
					+ "Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)"
					+ "Lnet/minecraft/util/TypedActionResult;",
			shift = Shift.AFTER, ordinal = 0)}, method = {"interactItem"}, cancellable = true)
	private void postInteractItem(ServerPlayerEntity player, World world, ItemStack stack, Hand hand,
			CallbackInfoReturnable<ActionResult> cir) {
		SharedVariables.EVENT_HANDLER.onItemUse(new ItemUseEvent(player, world, hand,
				interactedStack));
	}
	
	@Inject(at = {@At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;"
			+ "useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/utilActionResult;",
			ordinal = 1)}, method = {"interactBlock"})
	private void preInteractBlock(ServerPlayerEntity player, World world, ItemStack stack,
			Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
		blockInteractedStack = stack.copy();
	}
	
	@Inject(at = {@At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;"
			+ "useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/utilActionResult;",
			shift = Shift.AFTER, ordinal = 1)}, method = {"interactBlock"})
	private void postInteractBlock(ServerPlayerEntity player, World world, ItemStack stack,
			Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
		SharedVariables.EVENT_HANDLER.onBlockInteraction(new BlockInteractionEvent(player, world, hand,
				hitResult, blockInteractedStack));
	}
}
