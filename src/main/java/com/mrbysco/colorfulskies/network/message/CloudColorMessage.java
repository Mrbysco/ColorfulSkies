package com.mrbysco.colorfulskies.network.message;

import com.mrbysco.colorfulskies.client.Color;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.NetworkEvent.Context;

public class CloudColorMessage {
	private final int color;

	public CloudColorMessage(int color) {
		this.color = color;
	}

	public static CloudColorMessage decode(final FriendlyByteBuf buffer) {
		return new CloudColorMessage(buffer.readInt());
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.color);
	}

	public void handle(Context ctx) {
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isClient() && FMLEnvironment.dist.isClient()) {
				if (color == -1) {
					com.mrbysco.colorfulskies.client.ClientHandler.setCloudColor(null);
				} else {
					int r = (color >> 16) & 0xFF;
					int g = (color >> 8) & 0xFF;
					int b = (color >> 0) & 0xFF;
					com.mrbysco.colorfulskies.client.ClientHandler.setCloudColor(new Color((float) r / 255.0F, (float) g / 255.0F, (float) b / 255.0F));
				}
			}
		});
		ctx.setPacketHandled(true);
	}
}
