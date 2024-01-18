package com.mrbysco.colorfulskies.network.message;

import com.mrbysco.colorfulskies.ColorfulSkies;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record CloudColorPayload(int color) implements CustomPacketPayload {
	public static final ResourceLocation ID = new ResourceLocation(ColorfulSkies.MOD_ID, "cloud_color");

	public CloudColorPayload(final FriendlyByteBuf buffer) {
		this(buffer.readInt());
	}

	public void write(FriendlyByteBuf buffer) {
		buffer.writeInt(this.color);
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}
}
