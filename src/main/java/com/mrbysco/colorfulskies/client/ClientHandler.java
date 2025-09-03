package com.mrbysco.colorfulskies.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mrbysco.colorfulskies.ColorfulSkies;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public class ClientHandler {
	public static final ResourceLocation CUSTOM_SUN_LOCATION = ResourceLocation.fromNamespaceAndPath(ColorfulSkies.MOD_ID, "textures/environment/sun.png");
	public static boolean sunriseDisabled = false;

	private static Color moonColor, sunColor, cloudColor, sunriseColor, skyColor = null;
	private static ResourceLocation moonTexture, sunTexture = null;

	public static int colorTheMoon(int originalColor) {
		if (moonColor != null) {
			return ARGB.colorFromFloat(1.0F, moonColor.red(), moonColor.green(), moonColor.blue());
		}
		return originalColor;
	}

	public static int colorTheSun(int originalColor) {
		if (moonColor != null) {
			return ARGB.colorFromFloat(1.0F, sunColor.red(), sunColor.green(), sunColor.blue());
		}
		return originalColor;
	}

	public static Integer getCloudColor() {
		if (cloudColor != null) {
			return cloudColor.original();
		}
		return null;
	}

	public static Integer getSunriseColor() {
		if (sunriseColor != null) {
			return sunriseColor.original();
		}
		return null;
	}

	public static Vec3 getSkyColor() {
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
		return reader.getMinY() - 128D;
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

	public static void renderCustomSunrise(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, float sunAngle) {
		int color = sunriseColor.original();
		poseStack.pushPose();
		poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
		float f = Mth.sin(sunAngle) < 0.0F ? 180.0F : 0.0F;
		poseStack.mulPose(Axis.ZP.rotationDegrees(f));
		poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
		Matrix4f matrix4f = poseStack.last().pose();
		VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.sunriseSunset());
		float f1 = ARGB.alphaFloat(color);
		vertexconsumer.addVertex(matrix4f, 0.0F, 100.0F, 0.0F).setColor(color);
		int i = ARGB.transparent(color);

		for (int k = 0; k <= 16; k++) {
			float f2 = (float)k * (float) (Math.PI * 2) / 16.0F;
			float f3 = Mth.sin(f2);
			float f4 = Mth.cos(f2);
			vertexconsumer.addVertex(matrix4f, f3 * 120.0F, f4 * 120.0F, -f4 * 40.0F * f1).setColor(i);
		}

		poseStack.popPose();
	}

	public static int generateSkyColor(@NotNull Vec3 color, float timeOffDay, float rainLevel, float thunderLevel, int flashTime, float partialTick) {
		float f1 = Mth.cos(timeOffDay * (float) (Math.PI * 2)) * 2.0F + 0.5F;
		f1 = Mth.clamp(f1, 0.0F, 1.0F);
		color = color.scale(f1);
		int i = ARGB.color(color);
		if (rainLevel > 0.0F) {
			float f4 = rainLevel * 0.75F;
			int j = ARGB.scaleRGB(ARGB.greyscale(i), 0.6F);
			i = ARGB.lerp(f4, i, j);
		}

		if (thunderLevel > 0.0F) {
			float f7 = thunderLevel * 0.75F;
			int k = ARGB.scaleRGB(ARGB.greyscale(i), 0.2F);
			i = ARGB.lerp(f7, i, k);
		}

		if (flashTime > 0) {
			float f8 = Math.min((float)flashTime - partialTick, 1.0F);
			f8 *= 0.45F;
			i = ARGB.lerp(f8, i, ARGB.color(204, 204, 255));
		}

		return i;
	}
}
