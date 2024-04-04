package com.mrbysco.colorfulskies.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.colorfulskies.ColorfulSkies;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class ClientHandler {
	public static final ResourceLocation CUSTOM_SUN_LOCATION = new ResourceLocation(ColorfulSkies.MOD_ID, "textures/environment/sun.png");
	public static boolean sunriseDisabled = false;

	private static Color moonColor, sunColor, cloudColor, sunriseColor, skyColor = null;
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

	public static Vec3 getCloudColor() {
		if (cloudColor != null) {
			return new Vec3(cloudColor.red(), cloudColor.green(), cloudColor.blue());
		}
		return null;
	}

	public static Vec3 getSkyColor() {
		if (skyColor != null) {
			return new Vec3(skyColor.red(), skyColor.green(), skyColor.blue());
		}
		return null;
	}

	public static Vec3 generateSkyColor(Vec3 color, float timeOfDay, float partialTicks) {
		if (skyColor != null) {
			return new Vec3(skyColor.red(), skyColor.green(), skyColor.blue());
		}
		return null;
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
			float f1 = Mth.cos(timeOfDay * ((float) Math.PI * 2F));
			if (f1 >= -0.4F && f1 <= 0.4F) {
				float f3 = f1 / 0.4F * 0.5F + 0.5F;
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

	public static void setSkyColor(Color color) {
		skyColor = color;
	}

	public static void setMoonTexture(@Nullable ResourceLocation location) {
		moonTexture = location;
	}

	public static void setSunTexture(@Nullable ResourceLocation location) {
		sunTexture = location;
	}

	public static Vec3 generateSkyColor(@NotNull Vec3 color, float timeOffDay, float rainLevel, float thunderLevel, int flashTime, float partialTick) {
		float f1 = Mth.cos(timeOffDay * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
		System.out.println(f1);
		f1 = Mth.clamp(f1, 0.1F, 1.0F);
		float red = (float)color.x * f1;
		float green = (float)color.y * f1;
		float blue = (float)color.z * f1;

		if (rainLevel > 0.0F) {
			float f6 = (red * 0.3F + green * 0.59F + blue * 0.11F) * 0.6F;
			float f7 = 1.0F - rainLevel * 0.75F;
			red = red * f7 + f6 * (1.0F - f7);
			green = green * f7 + f6 * (1.0F - f7);
			blue = blue * f7 + f6 * (1.0F - f7);
		}

		if (thunderLevel > 0.0F) {
			float f10 = (red * 0.3F + green * 0.59F + blue * 0.11F) * 0.2F;
			float f8 = 1.0F - thunderLevel * 0.75F;
			red = red * f8 + f10 * (1.0F - f8);
			green = green * f8 + f10 * (1.0F - f8);
			blue = blue * f8 + f10 * (1.0F - f8);
		}

		if (flashTime > 0) {
			float f11 = (float)flashTime - partialTick;
			if (f11 > 1.0F) {
				f11 = 1.0F;
			}

			f11 *= 0.45F;
			red = red * (1.0F - f11) + 0.8F * f11;
			green = green * (1.0F - f11) + 0.8F * f11;
			blue = blue * (1.0F - f11) + 1.0F * f11;
		}

		return new Vec3(red, green, blue);
	}
}
