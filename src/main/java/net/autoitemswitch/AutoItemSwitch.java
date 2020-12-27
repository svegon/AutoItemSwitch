package net.autoitemswitch;

// Abstract parent class of client/server specific mod executors
public abstract class AutoItemSwitch {
	public static boolean isClient() {
		return SharedVariables.mod instanceof ClientAIS;
	}
	
	public static boolean isServer() {
		return SharedVariables.mod instanceof ServerAIS;
	}
}
