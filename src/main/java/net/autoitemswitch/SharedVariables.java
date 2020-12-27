package net.autoitemswitch;

import java.nio.file.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.autoitemswitch.events.EventHandler;
import net.autoitemswitch.settings.AISSettings;

public final class SharedVariables {
	public static final Logger LOGGER = LogManager.getLogger("AutoItemSwitch");
	public static final EventHandler EVENT_HANDLER = new EventHandler();
	
	public static Path modDirectory;
	public static AISSettings settings;
	public static AutoItemSwitch mod;
}
