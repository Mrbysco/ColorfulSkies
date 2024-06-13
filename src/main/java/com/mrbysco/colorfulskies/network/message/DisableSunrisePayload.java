package com.mrbysco.colorfulskies.network.message;

import com.mrbysco.colorfulskies.ColorfulSkies;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record DisableSunrisePayload(boolean disabled) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, DisableSunrisePayload> CODEC = CustomPacketPayload.codec(
			DisableSunrisePayload::write,
			DisableSunrisePayload::new);
	public static final Type<DisableSunrisePayload> ID = new Type<>(ResourceLocation.fromNamespaceAndPath(ColorfulSkies.MOD_ID, "disable_sunrise"));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}

	public DisableSunrisePayload(final FriendlyByteBuf buffer) {
		this(buffer.readBoolean());
	}

	public void write(FriendlyByteBuf buffer) {
		buffer.writeBoolean(this.disabled);
	}
}
