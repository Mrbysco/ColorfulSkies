package com.mrbysco.colorfulskies.network.message;

import com.mrbysco.colorfulskies.ColorfulSkies;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SkyColorPayload(int color) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, SkyColorPayload> CODEC = CustomPacketPayload.codec(
			SkyColorPayload::write,
			SkyColorPayload::new);
	public static final Type<SkyColorPayload> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(ColorfulSkies.MOD_ID, "sky_color"));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}

	public SkyColorPayload(final FriendlyByteBuf buffer) {
		this(buffer.readInt());
	}

	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(this.color);
	}
}
