package com.mrbysco.colorfulskies.network.message;

import com.mrbysco.colorfulskies.client.ClientHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.DistExecutor.SafeRunnable;
import net.minecraftforge.network.NetworkEvent.Context;

import java.io.Serial;
import java.util.function.Supplier;

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

	public void handle(Supplier<Context> context) {
		Context ctx = context.get();
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isClient()) {
				UpdateEvent.update(this.disabled).run();
			}
		});
		ctx.setPacketHandled(true);
	}

	private static class UpdateEvent {
		private static SafeRunnable update(boolean disabled) {
			return new SafeRunnable() {
				@Serial
				private static final long serialVersionUID = 1L;

				@Override
				public void run() {
					ClientHandler.sunriseDisabled = disabled;
				}
			};
		}
	}
}
