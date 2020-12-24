package net.autoitemswitch.settings;

import org.lwjgl.glfw.GLFW;

import net.autoitemswitch.SharedVariables;
import net.autoitemswitch.events.TickListener;
import net.autoitemswitch.gui.screen.SettingsScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;

@Environment(EnvType.CLIENT)
public class ClientSettings extends AISSettings implements TickListener {
	private KeyBinding settingsKey;
	
	public ClientSettings() {
		settingsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.autoitemswitch.settings", GLFW.GLFW_KEY_COMMA, "key.categories.autoitemswitch"));
		
		load();
		save();
		
		SharedVariables.EVENT_HANDLER.addTickListener(this);
	}

	@Override
	public void onTick() {
		MinecraftClient MC = MinecraftClient.getInstance();
		
		if(MC.currentScreen != null)
			return;
		
		while(settingsKey.wasPressed())
			MC.openScreen(new SettingsScreen());
	}
	
	@SuppressWarnings("resource")
	public void save() {
		super.save();
		
		try {
			MinecraftClient.getInstance().options.write();
		} catch(Throwable e) {
			SharedVariables.LOGGER.error("An unexpected error was thrown while saving key binds:", e);
		}
	}
	
	public void setSettingsKey(String key) {
		settingsKey.setBoundKey(InputUtil.fromTranslationKey(key));
	}
	
	public KeyBinding getSettingsKeyBind() {
		return settingsKey;
	}
}
