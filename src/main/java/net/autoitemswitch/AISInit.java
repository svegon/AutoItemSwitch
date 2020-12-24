package net.autoitemswitch;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;

public class AISInit implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
	@Override
	public void onInitialize() {
	}
	
	@Override
	public void onInitializeClient() {
		SharedVariables.mod = new ClientAIS();
	}

	@Override
	public void onInitializeServer() {
		SharedVariables.mod = new ServerAIS();
	}
}