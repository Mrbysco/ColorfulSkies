package com.mrbysco.colorfulskies.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.colorfulskies.ColorfulSkies;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelHeightAccessor;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class ClientHandler {
	public static final ResourceLocation CUSTOM_SUN_LOCATION = new ResourceLocation(ColorfulSkies.MOD_ID, "textures/environment/sun.png");
	public static boolean sunriseDisabled = false;

	private static Color moonColor, sunColor, cloudColor, sunriseColor = null;
	private static ResourceLocation moonTexture, sunTexture = null;

	public static void colorTheMoon(ClientLevel level, PoseStack poseStack, Matrix4f matrix4f, float partialTick, Camera camera) {
		if (moonColor != null) {
			RenderSystem.setShaderColor(moonColor.red(), moonColor.green(), moonColor.blue(), 1.0F);
		}
	}

	public static void colorTheSun(ClientLevel level, PoseStack poseStack, Matrix4f matrix4f, float partialTick, Camera camera) {
		if (sunColor != null) {
			RenderSystem.setShaderColor(sunColor.red(), sunColor.green(), sunColor.blue(), 1.0F);
		}
	}

	public static void colorTheCloud(ClientLevel level, PoseStack poseStack, Matrix4f matrix4f, float partialTick, double camX, double camY, double camZ) {
		if (cloudColor != null) {
			RenderSystem.setShaderColor(cloudColor.red(), cloudColor.green(), cloudColor.blue(), 1.0F);
		}
	}

	public static void resetCloudColor(ClientLevel level, PoseStack poseStack, Matrix4f matrix4f, float partialTick, double camX, double camY, double camZ) {
		if (cloudColor != null) {
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	public static ResourceLocation getMoonTexture(ResourceLocation defaultTexture) {
		if (moonTexture != null) {
			return moonTexture;
		}
		return defaultTexture;
	}

	public static ResourceLocation getSunTexture(ResourceLocation defaultTexture) {
		if (sunTexture != null) {
			return sunTexture;
		}
		return defaultTexture;
	}

	public static float[] getSunriseColors(float[] sunriseColors, float timeOfDay, float partialTicks) {
		if (ClientHandler.sunriseDisabled) {
			return null;
		}
		if (sunriseColor != null) {
			float[] sunriseCol = new float[4];
			float f1 = Mth.cos(timeOfDay * ((float) Math.PI * 2F)) - 0.0F;
			if (f1 >= -0.4F && f1 <= 0.4F) {
				float f3 = (f1 - -0.0F) / 0.4F * 0.5F + 0.5F;
				float f4 = 1.0F - (1.0F - Mth.sin(f3 * (float) Math.PI)) * 0.99F;
				f4 *= f4;
				sunriseCol[0] = f3 * 0.3F + sunriseColor.red();
				sunriseCol[1] = f3 * f3 * 0.7F + sunriseColor.green();
				sunriseCol[2] = f3 * f3 * 0.0F + sunriseColor.blue();
				sunriseCol[3] = f4;
				return sunriseCol;
			} else {
				return null;
			}
		}
		return sunriseColors;
	}

	public static double getHorizon(LevelHeightAccessor reader) {
		return reader.getMinBuildHeight() - 128D;
	}


	//Setters
	public static void setMoonColor(Color color) {
		moonColor = color;
	}

	public static void setSunColor(Color color) {
		sunColor = color;
	}

	public static void setCloudColor(Color color) {
		cloudColor = color;
	}

	public static void setSunriseColor(Color color) {
		sunriseColor = color;
	}

	public static void setMoonTexture(@Nullable ResourceLocation location) {
		moonTexture = location;
	}

	public static void setSunTexture(@Nullable ResourceLocation location) {
		sunTexture = location;
	}
}
