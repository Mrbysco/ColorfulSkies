package com.mrbysco.colorfulskies.network.message;

import com.mrbysco.colorfulskies.ColorfulSkies;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SunColorPayload(int color) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, SunColorPayload> CODEC = CustomPacketPayload.codec(
			SunColorPayload::write,
			SunColorPayload::new);
	public static final Type<SunColorPayload> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(ColorfulSkies.MOD_ID, "sun_color"));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}

	public SunColorPayload(final FriendlyByteBuf buffer) {
		this(buffer.readInt());
	}

	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(this.color);
	}
}
