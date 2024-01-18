package com.mrbysco.colorfulskies.network.handler;

import com.mrbysco.colorfulskies.client.ClientHandler;
import com.mrbysco.colorfulskies.client.Color;
import com.mrbysco.colorfulskies.network.message.CloudColorPayload;
import com.mrbysco.colorfulskies.network.message.DisableSunrisePayload;
import com.mrbysco.colorfulskies.network.message.MoonColorPayload;
import com.mrbysco.colorfulskies.network.message.SunColorPayload;
import com.mrbysco.colorfulskies.network.message.SunriseColorPayload;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class ClientPayloadHandler {
	private static final ClientPayloadHandler INSTANCE = new ClientPayloadHandler();

	public static ClientPayloadHandler getInstance() {
		return INSTANCE;
	}

	public void handleCloudData(final CloudColorPayload data, final PlayPayloadContext context) {
		context.workHandler().submitAsync(() -> {
					int color = data.color();
					if (color == -1) {
						com.mrbysco.colorfulskies.client.ClientHandler.setCloudColor(null);
					} else {
						int r = (color >> 16) & 0xFF;
						int g = (color >> 8) & 0xFF;
						int b = (color >> 0) & 0xFF;
						com.mrbysco.colorfulskies.client.ClientHandler.setCloudColor(new Color((float) r / 255.0F, (float) g / 255.0F, (float) b / 255.0F));
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.packetHandler().disconnect(Component.translatable("colorfulskies.networking.cloud_color.failed", e.getMessage()));
					return null;
				});
	}

	public void handleMoonData(final MoonColorPayload data, final PlayPayloadContext context) {
		context.workHandler().submitAsync(() -> {
					int color = data.color();
					if (color == -1) {
						com.mrbysco.colorfulskies.client.ClientHandler.setMoonColor(null);
					} else {
						int r = (color >> 16) & 0xFF;
						int g = (color >> 8) & 0xFF;
						int b = (color >> 0) & 0xFF;
						com.mrbysco.colorfulskies.client.ClientHandler.setMoonColor(new Color((float) r / 255.0F, (float) g / 255.0F, (float) b / 255.0F));
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.packetHandler().disconnect(Component.translatable("colorfulskies.networking.moon_color.failed", e.getMessage()));
					return null;
				});
	}

	public void handleSunData(final SunColorPayload data, final PlayPayloadContext context) {
		context.workHandler().submitAsync(() -> {
					int color = data.color();
					if (color == -1) {
						com.mrbysco.colorfulskies.client.ClientHandler.setSunColor(null);
						com.mrbysco.colorfulskies.client.ClientHandler.setSunTexture(null);
					} else {
						int r = (color >> 16) & 0xFF;
						int g = (color >> 8) & 0xFF;
						int b = (color >> 0) & 0xFF;
						com.mrbysco.colorfulskies.client.ClientHandler.setSunColor(new Color((float) r / 255.0F, (float) g / 255.0F, (float) b / 255.0F));
						com.mrbysco.colorfulskies.client.ClientHandler.setSunTexture(ClientHandler.CUSTOM_SUN_LOCATION);
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.packetHandler().disconnect(Component.translatable("colorfulskies.networking.sun_color.failed", e.getMessage()));
					return null;
				});
	}

	public void handleSunriseData(final SunriseColorPayload data, final PlayPayloadContext context) {
		context.workHandler().submitAsync(() -> {
					int color = data.color();
					if (color == -1) {
						com.mrbysco.colorfulskies.client.ClientHandler.setSunriseColor(null);
					} else {
						int r = (color >> 16) & 0xFF;
						int g = (color >> 8) & 0xFF;
						int b = (color >> 0) & 0xFF;
						com.mrbysco.colorfulskies.client.ClientHandler.setSunriseColor(new Color((float) r / 255.0F, (float) g / 255.0F, (float) b / 255.0F));
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.packetHandler().disconnect(Component.translatable("colorfulskies.networking.sunrise_color.failed", e.getMessage()));
					return null;
				});
	}

	public void handleDisableData(final DisableSunrisePayload data, final PlayPayloadContext context) {
		context.workHandler().submitAsync(() -> {
					com.mrbysco.colorfulskies.client.ClientHandler.sunriseDisabled = data.disabled();
				})
				.exceptionally(e -> {
					// Handle exception
					context.packetHandler().disconnect(Component.translatable("colorfulskies.networking.disable_sunrise.failed", e.getMessage()));
					return null;
				});
	}
}
