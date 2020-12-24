package net.autoitemswitch.settings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.autoitemswitch.SharedVariables;
import net.autoitemswitch.exceptions.JsonException;
import net.autoitemswitch.utils.CollectionUtils;
import net.autoitemswitch.utils.JsonUtils;
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

public abstract class AISSettings {
	private final Map<Item, ArrayList<Item>> switchMap = Maps.newHashMap();
	
	public AISSettings() {
		load();
		save();
	}
	
	public void load() {
		loadSwitchMap();
	}

	public void save() {
		try {
			saveSwitchMap();
		} catch(Throwable e) {
			SharedVariables.LOGGER.error("An unexpected error was thrown while saving:", e);
		}
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
		Map<Class<?>, ArrayList<Item>> toolItems = Maps.newHashMap();
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
				switchMap.put(item, (ArrayList<Item>) Arrays.asList(item));
			}
		}
		
		for(ArrayList<Item> value : toolItems.values()) {
			for(Item toolItem : value)
				switchMap.put(toolItem, CollectionUtils.copy(value));
		}
	}
	
	public void saveSwitchMap() {
		JsonObject json = new JsonObject();
		
		for(Entry<Item, ArrayList<Item>> entry : switchMap.entrySet()) {
			json.add(Registry.ITEM.getId(entry.getKey()).toString(),
					JsonUtils.getItemListAsJsonArray(entry.getValue()));
		}
		
		try {
			JsonUtils.toJson(json, SharedVariables.modDirectory.resolve("switch_map.json"));
		} catch(IOException | JsonException e) {
			SharedVariables.LOGGER.error("Saving the switch map has encountered an error:", e);
		}
	}
	
	public Map<Item, ArrayList<Item>> getSwitchMap() {
		return switchMap;
	}
	
	public ArrayList<Item> getSwitchableItems(Item item) {
		if(switchMap.containsKey(item))
			return switchMap.get(item);
		
		ArrayList<Item> emptyList = new ArrayList<Item>();
		putToSwitchMap(item, emptyList);
		
		return emptyList;
	}
	
	public void putToSwitchMap(Item item, ArrayList<Item> list) {
		switchMap.put(item, list);
		saveSwitchMap();
	}
}
