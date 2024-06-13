package com.mrbysco.colorfulskies.network.message;

import com.mrbysco.colorfulskies.ColorfulSkies;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record MoonColorPayload(int color) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, MoonColorPayload> CODEC = CustomPacketPayload.codec(
			MoonColorPayload::write,
			MoonColorPayload::new);
	public static final Type<MoonColorPayload> ID = new Type<>(new ResourceLocation(ColorfulSkies.MOD_ID, "moon_color"));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}

	public MoonColorPayload(final FriendlyByteBuf buffer) {
		this(buffer.readInt());
	}

	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(this.color);
	}
}
