package com.mrbysco.colorfulskies.network.message;

import com.mrbysco.colorfulskies.ColorfulSkies;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record DisableSunrisePayload(boolean disabled) implements CustomPacketPayload {
	public static final ResourceLocation ID = new ResourceLocation(ColorfulSkies.MOD_ID, "disable_sunrise");

	public DisableSunrisePayload(final FriendlyByteBuf buffer) {
		this(buffer.readBoolean());
	}

	public void write(FriendlyByteBuf buffer) {
		buffer.writeBoolean(this.disabled);
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}
}
