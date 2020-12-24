package net.autoitemswitch.events;

import net.minecraft.network.Packet;

public class PacketInputEvent extends CancellableEvent {
	private final Packet<?> packet;
	
	public PacketInputEvent(Packet<?> packet) {
		this.packet = packet;
	}
	
	public Packet<?> getPacket() {
		return this.packet;
	}
}
