package net.autoitemswitch;

import java.io.IOException;
import java.nio.file.Files;

import org.jetbrains.annotations.Nullable;

import net.autoitemswitch.events.BlockInteractionEvent;
import net.autoitemswitch.events.BlockInteractionListener;
import net.autoitemswitch.events.ItemUseEvent;
import net.autoitemswitch.events.ItemUseListener;
import net.autoitemswitch.events.PacketInputEvent;
import net.autoitemswitch.events.PacketInputHandleEvent;
import net.autoitemswitch.events.PacketInputHandleListener;
import net.autoitemswitch.events.PacketInputListener;
import net.autoitemswitch.events.StoppedUsingItemEvent;
import net.autoitemswitch.events.StoppedUsingItemListener;
import net.autoitemswitch.settings.ClientSettings;
import net.autoitemswitch.utils.ItemUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public final class ClientAIS extends AutoItemSwitch implements BlockInteractionListener,
		ItemUseListener, PacketInputHandleListener, PacketInputListener, StoppedUsingItemListener {
	private static final MinecraftClient MC = MinecraftClient.getInstance();
	
	@Nullable
	private Item brokenItem;
	private EquipmentSlot brokenSlot;
	@Nullable
	private Item consumedItem;
	private EquipmentSlot consumedSlot;
	
	public ClientAIS() {
		SharedVariables.modDirectory = MC.runDirectory.toPath().resolve("AutoItemSwitch");

		try {
			Files.createDirectories(SharedVariables.modDirectory);
		} catch(IOException e) {
			throw new RuntimeException("Couldn't create " + SharedVariables.modDirectory + ":", e);
		}
		
		SharedVariables.settings = new ClientSettings();
		SharedVariables.EVENT_HANDLER.client();

		SharedVariables.EVENT_HANDLER.addBlockInteractionListener(this);
		SharedVariables.EVENT_HANDLER.addItemUseListener(this);
		SharedVariables.EVENT_HANDLER.addPacketInputHandleListener(this);
		SharedVariables.EVENT_HANDLER.addPacketInputListener(this);
	}

	@Override
	public void onBlockInteraction(BlockInteractionEvent event) {
		SharedVariables.LOGGER.error(event.getStack() + " " + event.getWorld().getRegistryKey().getValue() + " " + event.getPlayer().getDisplayName().asString() + " " + (event.getHand() == Hand.OFF_HAND ? "OFF_HAND" : "MAIN_HAND"));
		
		if(!MC.player.getStackInHand(event.getHand()).isEmpty())
			return;
		
		ItemStack stack = event.getStack();
		
		if(stack.isEmpty())
			return;
		
		Item item = stack.getItem();
		
		if(item instanceof ArmorItem)
			return;
		
		reequipItem(item, event.getHand() == Hand.OFF_HAND ? EquipmentSlot.OFFHAND
				: EquipmentSlot.MAINHAND);
	}

	@Override
	public void onItemUse(ItemUseEvent event) {
		SharedVariables.LOGGER.error(event.getStack() + " " + event.getWorld().getRegistryKey().getValue() + " " + event.getPlayer().getDisplayName().asString() + " " + (event.getHand() == Hand.OFF_HAND ? "OFF_HAND" : "MAIN_HAND"));

		if(event.getPlayer() != MC.player)
			return;
		
		if(!MC.player.getStackInHand(event.getHand()).isEmpty())
			return;
		
		ItemStack stack = event.getStack();
		
		if(stack.isEmpty())
			return;
		
		Item item = stack.getItem();
		
		if(item instanceof ArmorItem)
			return;
		
		reequipItem(item, event.getHand() == Hand.OFF_HAND ? EquipmentSlot.OFFHAND
				: EquipmentSlot.MAINHAND);
	}

	@Override
	public void onPacketInput(PacketInputEvent event) {
		if(event.getPacket() instanceof EntityStatusS2CPacket) {
			EntityStatusS2CPacket packet = (EntityStatusS2CPacket)event.getPacket();
			
			if(packet.getEntity(MC.world) != MC.player)
				return;
			
			byte status = packet.getStatus();
			
			if(47 <= status && status <= 52) {
				SharedVariables.LOGGER.error("Replacing broken item");
				
				EquipmentSlot slot = breakStatusToEquipmentSlot(packet.getStatus());

				brokenItem = MC.player.getEquippedStack(slot).getItem();
				brokenSlot = slot;
				
				return;
			}
			
			if(status == 9) {
				ItemStack stack = MC.player.getActiveItem();
				
				if(stack.isEmpty()) {
					SharedVariables.LOGGER.error("Received a consumption packet while none is in use.");
					return;
				}
				
				consumedItem = stack.getItem();
				consumedSlot = MC.player.getActiveHand() == Hand.OFF_HAND ? EquipmentSlot.OFFHAND
						: EquipmentSlot.MAINHAND;
			}
		}
	}

	@Override
	public void onPacketInputHandle(PacketInputHandleEvent event) {
		if(brokenItem != null) {
			reequipItem(brokenItem, brokenSlot);
			
			brokenItem = null;
		} else if(consumedItem != null) {
			if(MC.player.getEquippedStack(consumedSlot).isEmpty())
				reequipItem(consumedItem, consumedSlot);
			
			consumedItem = null;
		}
	}

	@Override
	public void onStoppedUsingItem(StoppedUsingItemEvent event) {
		Item item = event.getStack().getItem();
		
		if(!(item instanceof TridentItem))
			return;
		
		if(!MC.player.getMainHandStack().isEmpty())
			return;
		
		reequipItem(item, EquipmentSlot.MAINHAND);
	}
	
	private boolean reequipItem(Item itemToRenew, EquipmentSlot slot) {
		int slotId;
		
		for(Item item : SharedVariables.settings.getSwitchableItems(itemToRenew)) {
			slotId = ItemUtils.getSlotWithItem(MC.player.inventory, item);
			
			if(slotId < 0)
				continue;
			
			if(slot == EquipmentSlot.MAINHAND)
				MC.interactionManager.clickSlot(MC.player.currentScreenHandler.syncId,
						slotId < 9 ? slotId + 36 : slotId, MC.player.inventory.selectedSlot,
								SlotActionType.SWAP, MC.player);
			else if(slot == EquipmentSlot.OFFHAND)
				MC.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(
						PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN,
						Direction.DOWN));
			else
				MC.interactionManager.clickSlot(MC.player.currentScreenHandler.syncId,
						slotId < 9 ? slotId + 36 : slotId, 0, SlotActionType.QUICK_MOVE, MC.player);
			
			SharedVariables.LOGGER.error("Successfully reequipped " + itemToRenew + ".");
			return true;
		}
	
		SharedVariables.LOGGER.error("Failed to reequip " + itemToRenew + ".");
		return false;
	}
	
	private EquipmentSlot breakStatusToEquipmentSlot(byte status) {
		switch(status) {
		case 48:
			return EquipmentSlot.OFFHAND;
		case 49:
			return EquipmentSlot.HEAD;
		case 50:
			return EquipmentSlot.CHEST;
		case 51:
			return EquipmentSlot.LEGS;
		case 52:
			return EquipmentSlot.FEET;
		default:
			return EquipmentSlot.MAINHAND;
		}
	}
}
