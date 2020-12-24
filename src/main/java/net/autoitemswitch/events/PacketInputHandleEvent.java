package net.autoitemswitch.events;

import net.minecraft.network.Packet;

public class PacketInputHandleEvent {
	private final Packet<?> packet;
	
	public PacketInputHandleEvent(Packet<?> packet) {
		this.packet = packet;
	}
	
	public Packet<?> getPacket() {
		return this.packet;
	}
}
