package net.autoitemswitch.gui.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

@Environment(EnvType.CLIENT)
public class SettingsScreen extends Screen {
	public SettingsScreen() {
		super(new TranslatableText("autoitemswitch.settingsScreen.title"));
	}
	
	protected void init() {
	}
}
