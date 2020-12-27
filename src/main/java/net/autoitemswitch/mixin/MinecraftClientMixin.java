package net.autoitemswitch.mixin;

import java.io.File;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.autoitemswitch.SharedVariables;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.WindowEventHandler;
import net.minecraft.client.options.GameOptions;
import net.minecraft.util.snooper.SnooperListener;
import net.minecraft.util.thread.ReentrantThreadExecutor;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin extends ReentrantThreadExecutor<Runnable>
		implements SnooperListener, WindowEventHandler {
	@Shadow
	@Final
	@Mutable
	public GameOptions options;
	@Shadow
	@Final
	public File runDirectory;

	protected MinecraftClientMixin(String string) {
		super(string);
	}
	
	@Inject(at = {@At("RETURN")}, method = {"<init>*"})
	private void onInitialize(RunArgs args, CallbackInfo ci) {
		while(options == null) {
			options = new GameOptions((MinecraftClient)(Object)this, runDirectory);
			SharedVariables.LOGGER.error("MinecraftClient.options was null.");
		}
	}
	
	@Inject(at = {@At("HEAD")}, method = {"tick"})
	private void onTick(CallbackInfo ci) {
		while(options == null) {
			options = new GameOptions((MinecraftClient)(Object)this, runDirectory);
			SharedVariables.LOGGER.error("MinecraftClient.options was null.");
		}
	}
}
