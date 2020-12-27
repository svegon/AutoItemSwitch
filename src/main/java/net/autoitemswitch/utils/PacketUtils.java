package net.autoitemswitch.utils;

import net.minecraft.entity.EquipmentSlot;

public final class PacketUtils {
	private PacketUtils() {
		throw new AssertionError();
	}
	
	public static EquipmentSlot breakStatusToEquipmentSlot(byte status) {
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
