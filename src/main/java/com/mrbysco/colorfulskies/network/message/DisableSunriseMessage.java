package com.mrbysco.colorfulskies.network.message;

import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.NetworkEvent.Context;

public class DisableSunriseMessage {
	private final boolean disabled;

	public DisableSunriseMessage(boolean disabled) {
		this.disabled = disabled;
	}

	public static DisableSunriseMessage decode(final FriendlyByteBuf buffer) {
		return new DisableSunriseMessage(buffer.readBoolean());
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeBoolean(this.disabled);
	}

	public void handle(Context ctx) {
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isClient() && FMLEnvironment.dist.isClient()) {
				com.mrbysco.colorfulskies.client.ClientHandler.sunriseDisabled = disabled;
			}
		});
		ctx.setPacketHandled(true);
	}
}
