package com.mrbysco.colorfulskies.network.message;

import com.mrbysco.colorfulskies.client.ClientHandler;
import com.mrbysco.colorfulskies.client.Color;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.DistExecutor.SafeRunnable;
import net.minecraftforge.network.NetworkEvent.Context;

import java.io.Serial;
import java.util.function.Supplier;

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

	public void handle(Supplier<Context> context) {
		Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isClient()) {
				UpdateEvent.update(this.color).run();
			}
		});
		ctx.setPacketHandled(true);
	}

	private static class UpdateEvent {
		private static SafeRunnable update(int color) {
			return new SafeRunnable() {
				@Serial
				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					if (color == -1) {
						ClientHandler.setCloudColor(null);
					} else {
						int r = (color >> 16) & 0xFF;
						int g = (color >> 8) & 0xFF;
						int b = (color >> 0) & 0xFF;
						ClientHandler.setCloudColor(new Color((float) r / 255.0F, (float) g / 255.0F, (float) b / 255.0F));
					}
				}
			};
		}
	}
}
