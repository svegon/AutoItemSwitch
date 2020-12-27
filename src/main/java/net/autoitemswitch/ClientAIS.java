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
import net.autoitemswitch.utils.PacketUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;

public final class ClientAIS extends AutoItemSwitch implements BlockInteractionListener,
		ItemUseListener, PacketInputHandleListener, PacketInputListener, StoppedUsingItemListener {
	private static MinecraftClient mc;
	
	@Nullable
	private Item brokenItem;
	private EquipmentSlot brokenSlot;
	@Nullable
	private Item consumedItem;
	private EquipmentSlot consumedSlot;
	
	public ClientAIS() {
		mc = MinecraftClient.getInstance();
		SharedVariables.modDirectory = mc.runDirectory.toPath().resolve("AutoItemSwitch");

		try {
			Files.createDirectories(SharedVariables.modDirectory);
		} catch(IOException e) {
			throw new RuntimeException("Couldn't create " + SharedVariables.modDirectory + ":", e);
		}
		
		SharedVariables.settings = new ClientSettings();
		SharedVariables.EVENT_HANDLER.clientInit();

		SharedVariables.EVENT_HANDLER.addBlockInteractionListener(this);
		SharedVariables.EVENT_HANDLER.addItemUseListener(this);
		SharedVariables.EVENT_HANDLER.addPacketInputHandleListener(this);
		SharedVariables.EVENT_HANDLER.addPacketInputListener(this);
	}

	@Override
	public void onBlockInteraction(BlockInteractionEvent event) {
		SharedVariables.LOGGER.error(event.getStack() + " " + event.getWorld().getRegistryKey().getValue() + " " + event.getPlayer().getDisplayName().asString() + " " + (event.getHand() == Hand.OFF_HAND ? "OFF_HAND" : "MAIN_HAND"));
		
		if(!mc.player.getStackInHand(event.getHand()).isEmpty())
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

		if(event.getPlayer() != mc.player)
			return;
		
		if(!mc.player.getStackInHand(event.getHand()).isEmpty())
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
			
			if(packet.getEntity(mc.world) != mc.player)
				return;
			
			byte status = packet.getStatus();
			
			if(47 <= status && status <= 52) {
				SharedVariables.LOGGER.error("Replacing broken item");
				
				EquipmentSlot slot = PacketUtils.breakStatusToEquipmentSlot(packet.getStatus());

				brokenItem = mc.player.getEquippedStack(slot).getItem();
				brokenSlot = slot;
				
				return;
			}
			
			if(status == 9) {
				ItemStack stack = mc.player.getActiveItem();
				
				if(stack.isEmpty()) {
					SharedVariables.LOGGER.error("Received a consumption packet while none is in use.");
					return;
				}
				
				consumedItem = stack.getItem();
				consumedSlot = mc.player.getActiveHand() == Hand.OFF_HAND ? EquipmentSlot.OFFHAND
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
			if(mc.player.getEquippedStack(consumedSlot).isEmpty())
				reequipItem(consumedItem, consumedSlot);
			
			consumedItem = null;
		}
	}

	@Override
	public void onStoppedUsingItem(StoppedUsingItemEvent event) {
		Item item = event.getStack().getItem();
		
		if(!(item instanceof TridentItem))
			return;
		
		if(!mc.player.getMainHandStack().isEmpty())
			return;
		
		reequipItem(item, EquipmentSlot.MAINHAND);
	}
	
	private boolean reequipItem(Item itemToRenew, EquipmentSlot slot) {
		int slotId;
		
		for(Item item : SharedVariables.settings.getSwitchMap(mc.player).get(itemToRenew)) {
			slotId = ItemUtils.getSlotWithItem(mc.player.inventory, item);
			
			if(slotId < 0)
				continue;
			
			switch(slot) {
			case MAINHAND:
				mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId,
						slotId < 9 ? slotId + 36 : slotId, mc.player.inventory.selectedSlot,
								SlotActionType.SWAP, mc.player);
				break;
			case OFFHAND:
				mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId,
						slotId < 9 ? slotId + 36 : slotId, 40, SlotActionType.SWAP, mc.player);
				break;
			default:
				mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId,
						slotId < 9 ? slotId + 36 : slotId, 0, SlotActionType.QUICK_MOVE, mc.player);
				break;
			}
			
			SharedVariables.LOGGER.error("Successfully reequipped " + itemToRenew + ".");
			return true;
		}
	
		SharedVariables.LOGGER.error("Failed to reequip " + itemToRenew + ".");
		return false;
	}
}
