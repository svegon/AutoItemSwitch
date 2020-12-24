package net.autoitemswitch.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;

import net.autoitemswitch.SharedVariables;
import net.autoitemswitch.events.ScreenCloseEvent;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
	protected ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}
	
	@Inject(at = {@At("HEAD")}, method = {"closeScreen()V"}, cancellable = true)
	private void onCloseScreen(CallbackInfo ci) {
		ScreenCloseEvent event = new ScreenCloseEvent();
		SharedVariables.EVENT_HANDLER.onScreenClose(event);
		
		if(event.isCancelled())
			ci.cancel();
	}
}
