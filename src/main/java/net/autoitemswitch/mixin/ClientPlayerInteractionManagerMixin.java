package net.autoitemswitch.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.autoitemswitch.SharedVariables;
import net.autoitemswitch.events.BlockInteractionEvent;
import net.autoitemswitch.events.ItemUseEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {
	private ItemStack interactedStack;
	private ItemStack blockInteractedStack;

	@Inject(at = {@At(value = "INVOKE",
			target = "Lnet/minecraft/item/ItemStack;use(Lnet/minecraft/world/World;"
					+ "Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)"
					+ "Lnet/minecraft/util/TypedActionResult;",
			ordinal = 0)}, method = {"interactItem"})
	private void preInteractItem(PlayerEntity player, World world, Hand hand,
			CallbackInfoReturnable<ActionResult> cir) {
		interactedStack = player.getStackInHand(hand).copy();
	}

	@Inject(at = {@At(value = "INVOKE",
			target = "Lnet/minecraft/item/ItemStack;use(Lnet/minecraft/world/World;"
					+ "Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)"
					+ "Lnet/minecraft/util/TypedActionResult;",
			shift = Shift.AFTER, ordinal = 0)}, method = {"interactItem"})
	private void postInteractItem(PlayerEntity player, World world, Hand hand,
			CallbackInfoReturnable<ActionResult> cir) {
		SharedVariables.EVENT_HANDLER.onItemUse(new ItemUseEvent(player, world, hand,
				interactedStack));
	}
	
	@Inject(at = {@At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;"
			+ "useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;",
			ordinal = 1)}, method = {"interactBlock"})
	private void preInteractBlock(ClientPlayerEntity player, ClientWorld world, Hand hand,
			BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
		blockInteractedStack = player.getStackInHand(hand).copy();
	}
	
	@Inject(at = {@At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;"
			+ "useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;",
			shift = Shift.AFTER, ordinal = 1)}, method = {"interactBlock"})
	private void postInteractBlock(ClientPlayerEntity player, ClientWorld world, Hand hand,
			BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
		SharedVariables.EVENT_HANDLER.onBlockInteraction(new BlockInteractionEvent(player, world, hand,
				hitResult, blockInteractedStack));
	}
}
