package net.autoitemswitch.settings;

import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;

public abstract class AISSettings {
	public abstract void load();
	
	public abstract void save();
	
	public abstract Map<Item, List<Item>> getSwitchMap(PlayerEntity player);
}
