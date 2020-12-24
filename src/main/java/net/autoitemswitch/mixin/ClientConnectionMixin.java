
package net.autoitemswitch.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.AsciiHeadersEncoder.SeparatorType;
import net.autoitemswitch.SharedVariables;
import net.autoitemswitch.events.PacketInputEvent;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin
		extends SimpleChannelInboundHandler<Packet<?>> {
	@Inject(at = {@At(value = "INVOKE",
		target = "Lnet/minecraft/network/ClientConnection;handlePacket(Lnet/minecraft/network/Packet;"
				+ "Lnet/minecraft/network/listener/PacketListener;)V",
		ordinal = 0)},
		method = {"channelRead0(Lio/netty/channel/ChannelHandlerContext;"
				+ "Lnet/minecraft/network/Packet;)V"},
		cancellable = true)
	private void preChannelRead0(ChannelHandlerContext channelHandlerContext,
			Packet<?> packet, CallbackInfo ci) {
		PacketInputEvent event = new PacketInputEvent(packet);
		SharedVariables.EVENT_HANDLER.onPacketInput(event);
		
		if(event.isCancelled())
			ci.cancel();
	}
	
	@Inject(at = {@At(value = "INVOKE",
		target = "Lnet/minecraft/network/ClientConnection;handlePacket(Lnet/minecraft/network/Packet;"
				+ "Lnet/minecraft/network/listener/PacketListener;)V",
		shift = At.Shift.AFTER, ordinal = 0)},
		method = {"channelRead0(Lio/netty/channel/ChannelHandlerContext;"
				+ "Lnet/minecraft/network/Packet;)V"})
	private void postChannelRead0(ChannelHandlerContext channelHandlerContext,
			Packet<?> packet, CallbackInfo ci) {
		
	}
}
