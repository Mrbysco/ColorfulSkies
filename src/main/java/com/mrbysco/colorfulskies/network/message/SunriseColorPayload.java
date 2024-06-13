package com.mrbysco.colorfulskies.network.message;

import com.mrbysco.colorfulskies.ColorfulSkies;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SunriseColorPayload(int color) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, SunriseColorPayload> CODEC = CustomPacketPayload.codec(
			SunriseColorPayload::write,
			SunriseColorPayload::new);
	public static final Type<SunriseColorPayload> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(ColorfulSkies.MOD_ID, "sunrise_color"));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}

	public SunriseColorPayload(final FriendlyByteBuf buffer) {
		this(buffer.readInt());
	}

	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(this.color);
	}
}
