package com.mrbysco.colorfulskies.network.message;

import com.mrbysco.colorfulskies.ColorfulSkies;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CloudColorPayload(int color) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, CloudColorPayload> CODEC = CustomPacketPayload.codec(
			CloudColorPayload::write,
			CloudColorPayload::new);
	public static final Type<CloudColorPayload> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(ColorfulSkies.MOD_ID, "cloud_color"));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}

	public CloudColorPayload(final FriendlyByteBuf buffer) {
		this(buffer.readInt());
	}

	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(this.color);
	}
}
