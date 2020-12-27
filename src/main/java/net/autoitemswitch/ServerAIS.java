package net.autoitemswitch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import net.autoitemswitch.settings.ServerSettings;

public final class ServerAIS extends AutoItemSwitch {
	public ServerAIS() {
		SharedVariables.modDirectory = new File(".").toPath().resolve("AutoSwitchHack");
		
		try {
			Files.createDirectories(SharedVariables.modDirectory);
		} catch(IOException e) {
			throw new RuntimeException("Couldn't create .minecraft/AutoItemSwitch");
		}
		
		SharedVariables.settings = new ServerSettings();
		SharedVariables.EVENT_HANDLER.serverInit();
		
		throw new RuntimeException("Server AutoItemSwitch hasn't been made yet.");
	}
}
