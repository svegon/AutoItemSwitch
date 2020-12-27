package net.autoitemswitch.settings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.glfw.GLFW;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.autoitemswitch.SharedVariables;
import net.autoitemswitch.events.TickListener;
import net.autoitemswitch.exceptions.JsonException;
import net.autoitemswitch.gui.screen.SettingsScreen;
import net.autoitemswitch.utils.JsonUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorStandItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.DecorationItem;
import net.minecraft.item.EggItem;
import net.minecraft.item.EnchantedGoldenAppleItem;
import net.minecraft.item.EndCrystalItem;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.ExperienceBottleItem;
import net.minecraft.item.FireChargeItem;
import net.minecraft.item.FireworkItem;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.HoneyBottleItem;
import net.minecraft.item.Item;
import net.minecraft.item.MinecartItem;
import net.minecraft.item.PotionItem;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SnowballItem;
import net.minecraft.item.ToolItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@Environment(EnvType.CLIENT)
public class ClientSettings extends AISSettings implements TickListener {
	private static MinecraftClient mc;
	
	private final Map<Item, List<Item>> switchMap = Maps.newHashMap();
	private final KeyBinding settingsKey;
	
	public ClientSettings() {
		mc = MinecraftClient.getInstance();
		
		settingsKey = null;
		//settingsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			//"key.autoitemswitch.settings", GLFW.GLFW_KEY_COMMA, "key.categories.autoitemswitch"));
		
		load();
		save();
		
		//SharedVariables.EVENT_HANDLER.addTickListener(this);
	}

	@Override
	public void onTick() {
		if(mc.currentScreen != null)
			return;
		
		while(settingsKey.wasPressed())
			mc.openScreen(new SettingsScreen());
	}
	
	@Override
	public void load() {
		loadSwitchMap();
	}
	
	public void loadSwitchMap() {
		try {
			JsonObject switchMapJson = JsonUtils.parseFileToObject(
					SharedVariables.modDirectory.resolve("switch_map.json"));
			
			for(Entry<String, JsonElement> entry : switchMapJson.entrySet()) {
				ArrayList<Item> list = new ArrayList<Item>();
				
				for(JsonElement element : JsonUtils.getAsArray(entry.getValue()))
					list.add(Registry.ITEM.get(new Identifier(JsonUtils.getAsString(element))));
				
				switchMap.put(Registry.ITEM.get(new Identifier(entry.getKey())), list);
			}
		} catch(IOException | JsonException e) {
			SharedVariables.LOGGER.info("Couldn't load switch map due to:", e,
					"Loading default switch map values.");
			loadDefaultSwitchMap();
		}
	}
	
	public void loadDefaultSwitchMap() {
		Map<Class<?>, List<Item>> toolItems = Maps.newHashMap();
		Iterator<Item> items = Registry.ITEM.iterator();
		Item item;
		
		while(items.hasNext()) {
			item = items.next();
			
			if(item instanceof ToolItem) {
				if(!toolItems.containsKey(item.getClass())) {
					toolItems.put(item.getClass(), new ArrayList<Item>());
				}
				
				toolItems.get(item.getClass()).add(item);
				continue;
			}
			
			if(item instanceof ArmorStandItem || item instanceof BlockItem
					|| item instanceof BoneMealItem || item instanceof DecorationItem
					|| item instanceof EggItem || item instanceof EnchantedGoldenAppleItem
					|| item instanceof EndCrystalItem || item instanceof EnderEyeItem
					|| item instanceof EnderPearlItem || item instanceof ExperienceBottleItem
					|| item instanceof FireChargeItem || item instanceof FireworkItem
					|| item instanceof FishingRodItem || item instanceof FlintAndSteelItem
					|| item instanceof HoneyBottleItem || item instanceof MinecartItem
					|| item instanceof PotionItem || item instanceof RangedWeaponItem
					|| item instanceof ShearsItem || item instanceof ShieldItem
					|| item instanceof SnowballItem) {
				switchMap.put(item, Arrays.asList(item));
			}
		}
		
		for(List<Item> value : toolItems.values()) {
			for(Item toolItem : value)
				switchMap.put(toolItem, value);
		}
	}
	
	public void saveSwitchMap() {
		try {
			JsonObject json = new JsonObject();
			
			for(Entry<Item, List<Item>> entry : switchMap.entrySet()) {
				json.add(Registry.ITEM.getId(entry.getKey()).toString(),
						JsonUtils.getItemListAsJsonArray(entry.getValue()));
			}
		
			JsonUtils.toJson(json, SharedVariables.modDirectory.resolve("switch_map.json"));
		} catch(IOException | JsonException e) {
			SharedVariables.LOGGER.error("Saving the switch map has encountered an error:", e);
		}
	}
	
	@Override
	public void save() {
		saveSwitchMap();
	}
	
	public void setSettingsKey(String key) {
		mc.options.setKeyCode(settingsKey, InputUtil.fromTranslationKey(key));
	}
	
	public KeyBinding getSettingsKeyBind() {
		return settingsKey;
	}

	@Override
	public Map<Item, List<Item>> getSwitchMap(PlayerEntity player) {
		return switchMap;
	}
}
