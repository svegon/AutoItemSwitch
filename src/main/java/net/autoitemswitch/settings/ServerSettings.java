package net.autoitemswitch.settings;

import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;

public class ServerSettings extends AISSettings {
	public ServerSettings() {
		load();
		save();
	}

	@Override
	public void load() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<Item, List<Item>> getSwitchMap(PlayerEntity player) {
		// TODO Auto-generated method stub
		return null;
	}
}
