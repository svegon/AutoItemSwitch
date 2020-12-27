package net.autoitemswitch.events;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.StartTick;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;

public class EventHandler implements StartTick,
		net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.StartTick {
	private final List<BlockInteractionListener> blockInteractionListeners = 
			new ArrayList<BlockInteractionListener>();
	private final List<ItemUseListener> itemUseListeners = new ArrayList<ItemUseListener>();
	private final List<ItemStackDamageListener> itemStackDamageListeners =
			new ArrayList<ItemStackDamageListener>();
	private final List<PacketInputHandleListener> packetInputHandleListeners =
			new ArrayList<PacketInputHandleListener>();
	private final List<PacketInputListener> packetInputListeners =
			new ArrayList<PacketInputListener>();
	private final List<ScreenCloseListener> screenCloseListeners =
			new ArrayList<ScreenCloseListener>();
	private final List<StoppedUsingItemListener> stoppedUsingItemListeners = 
			new ArrayList<StoppedUsingItemListener>();
	private final List<TickListener> tickListeners = new ArrayList<TickListener>();

	public void addBlockInteractionListener(BlockInteractionListener listener) {
		blockInteractionListeners.add(listener);
	}

	public void addItemStackDamageListener(ItemStackDamageListener listener) {
		itemStackDamageListeners.add(listener);
	}

	public void addItemUseListener(ItemUseListener listener) {
		itemUseListeners.add(listener);
	}

	public void addPacketInputHandleListener(PacketInputHandleListener listener) {
		packetInputHandleListeners.add(listener);
	}

	public void addPacketInputListener(PacketInputListener listener) {
		packetInputListeners.add(listener);
	}

	public void addScreenCloseListener(ScreenCloseListener listener) {
		screenCloseListeners.add(listener);
	}
	
	public void addStoppedUsingItemListener(StoppedUsingItemListener listener) {
		stoppedUsingItemListeners.add(listener);
	}

	public void addTickListener(TickListener listener) {
		tickListeners.add(listener);
	}

	public void removeBlockInteractionListener(BlockInteractionListener listener) {
		blockInteractionListeners.remove(listener);
	}

	public void removeItemStackDamageListener(ItemStackDamageListener listener) {
		itemStackDamageListeners.remove(listener);
	}

	public void removeItemUseListener(ItemUseListener listener) {
		itemUseListeners.remove(listener);
	}

	public void removePacketInputHandleListener(PacketInputHandleListener listener) {
		packetInputHandleListeners.remove(listener);
	}

	public void removePacketInputListener(PacketInputListener listener) {
		packetInputListeners.remove(listener);
	}

	public void removeScreenCloseListener(ScreenCloseListener listener) {
		screenCloseListeners.remove(listener);
	}
	
	public void removeStoppedUsingItemListener(StoppedUsingItemListener listener) {
		stoppedUsingItemListeners.remove(listener);
	}

	public void removeTickListener(TickListener listener) {
		tickListeners.remove(listener);
	}

	public void onBlockInteraction(BlockInteractionEvent event) {
		for(BlockInteractionListener listener : blockInteractionListeners)
			listener.onBlockInteraction(event);
	}

	public void onItemStackDamage(ItemStackDamageEvent event) {
		for(ItemStackDamageListener listener : itemStackDamageListeners)
			listener.onItemStackDamage(event);
	}

	public void onItemUse(ItemUseEvent event) {
		for(ItemUseListener listener : itemUseListeners)
			listener.onItemUse(event);
	}

	public void onPacketInputHandle(PacketInputHandleEvent event) {
		for(PacketInputHandleListener listener : packetInputHandleListeners)
			listener.onPacketInputHandle(event);
	}

	public void onPacketInput(PacketInputEvent event) {
		for(PacketInputListener listener : packetInputListeners)
			listener.onPacketInput(event);
	}
	
	public void onScreenClose(ScreenCloseEvent event) {
		for(ScreenCloseListener listener : screenCloseListeners)
			listener.onScreenClose(event);
	}
	
	public void onStoppedUsingItem(StoppedUsingItemEvent event) {
		for(StoppedUsingItemListener listener : stoppedUsingItemListeners)
			listener.onStoppedUsingItem(event);
	}
	
	@Environment(EnvType.CLIENT)
	@Override
	public void onStartTick(MinecraftClient client) {
		for(TickListener listener : tickListeners)
			listener.onTick();
	}
	
	@Environment(EnvType.SERVER)
	@Override
	public void onStartTick(MinecraftServer server) {
		for(TickListener listener : tickListeners)
			listener.onTick();
	}
	
	@Environment(EnvType.CLIENT)
	public void clientInit() {
		ClientTickEvents.START_CLIENT_TICK.register(this);
	}
	
	@Environment(EnvType.SERVER)
	public void serverInit() {
		ServerTickEvents.START_SERVER_TICK.register(this);
	}
}
