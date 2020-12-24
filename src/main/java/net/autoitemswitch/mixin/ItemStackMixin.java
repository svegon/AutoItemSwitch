package net.autoitemswitch.mixin;

import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At.Shift;

import net.autoitemswitch.SharedVariables;
import net.autoitemswitch.events.ItemStackDamageEvent;
import net.autoitemswitch.events.StoppedUsingItemEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
	private ItemStack damagedStack;
	private ItemStack usedStack;
	
	@Inject(at = {@At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V",
			ordinal = 0)},
			method = {"damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V"})
	private <T extends LivingEntity> void preDamage(int amount, T entity, Consumer<T> breakCallback,
			CallbackInfo ci) {
		this.damagedStack = this.copy();
	}
	
	@SuppressWarnings("unchecked")
	@Inject(at = {@At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V",
			shift = Shift.AFTER, ordinal = 0)},
			method = {"damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V"})
	private <T extends LivingEntity> void postDamage(int amount, T entity, Consumer<T> breakCallback,
			CallbackInfo ci) {
		SharedVariables.EVENT_HANDLER.onItemStackDamage(new ItemStackDamageEvent(amount,
				entity, (Consumer<LivingEntity>) breakCallback, this.damagedStack));
	}
	
	@Inject(at = {@At("HEAD")}, method = {"onStoppedUsing"})
	private void preOnStoppedUsing(World world, LivingEntity user, int remainingUseTicks) {
		this.usedStack = this.copy();
	}
	
	@Inject(at = {@At("TAIL")}, method = {"onStoppedUsing"})
	private void postOnStoppedUsing(World world, LivingEntity user, int remainingUseTicks) {
		SharedVariables.EVENT_HANDLER.onStoppedUsingItem(new StoppedUsingItemEvent(
				this.usedStack, world, user, remainingUseTicks));
	}
	
	@Shadow
	public ItemStack copy() {
		return null;
	}
}
