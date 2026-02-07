package com.mrbysco.colorfulskies.client;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mrbysco.colorfulskies.ColorfulSkies;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelHeightAccessor;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.joml.Vector4fc;

import java.util.OptionalDouble;
import java.util.OptionalInt;

public class ClientHandler {
	public static final Identifier CUSTOM_SUN_LOCATION = Identifier.fromNamespaceAndPath(ColorfulSkies.MOD_ID, "sun");
	public static boolean sunriseDisabled = false;

	private static Color moonColor, sunColor, cloudColor, sunriseColor, skyColor = null;
	private static Identifier sunTexture = null;

	public static Vector4fc colorTheMoon(Vector4fc originalColor) {
		if (moonColor != null) {
			return new Vector4f(moonColor.red(), moonColor.green(), moonColor.blue(), 1.0F);
		}
		return originalColor;
	}

	public static Vector4fc colorTheSun(Vector4fc originalColor, float rainBrightness) {
		if (sunColor != null) {
			return new Vector4f(sunColor.red(), sunColor.green(), sunColor.blue(), rainBrightness);
		}
		return originalColor;
	}

	public static Vector4fc colorTheSky(Vector4fc originalColor) {
		if (skyColor != null) {
			return ARGB.vector4fFromARGB32(skyColor.original());
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

	public static boolean hasSunTexture() {
		return sunTexture != null;
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

	public static void setSunTexture(@Nullable Identifier location) {
		sunTexture = location;
	}

	public static void renderCustomSunrise(PoseStack poseStack, float sunAngle, GpuBuffer sunriseBuffer) {
		float alpha = ARGB.alphaFloat(sunriseColor.original());
		if (!(alpha <= 0.001F)) {
			poseStack.pushPose();
			poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
			float f1 = Mth.sin(sunAngle) < 0.0F ? 180.0F : 0.0F;
			poseStack.mulPose(Axis.ZP.rotationDegrees(f1 + 90.0F));
			Matrix4fStack matrix4fstack = RenderSystem.getModelViewStack();
			matrix4fstack.pushMatrix();
			matrix4fstack.mul(poseStack.last().pose());
			matrix4fstack.scale(1.0F, 1.0F, alpha);

			GpuBufferSlice gpubufferslice = RenderSystem.getDynamicUniforms()
					.writeTransform(matrix4fstack, ARGB.vector4fFromARGB32(sunriseColor.original()), new Vector3f(), new Matrix4f());
			GpuTextureView gputextureview = Minecraft.getInstance().getMainRenderTarget().getColorTextureView();
			GpuTextureView gputextureview1 = Minecraft.getInstance().getMainRenderTarget().getDepthTextureView();

			try (RenderPass renderpass = RenderSystem.getDevice()
					.createCommandEncoder()
					.createRenderPass(() -> "Sunrise sunset", gputextureview, OptionalInt.empty(), gputextureview1, OptionalDouble.empty())) {
				renderpass.setPipeline(RenderPipelines.SUNRISE_SUNSET);
				RenderSystem.bindDefaultUniforms(renderpass);
				renderpass.setUniform("DynamicTransforms", gpubufferslice);
				renderpass.setVertexBuffer(0, sunriseBuffer);
				renderpass.draw(0, 18);
			}

			matrix4fstack.popMatrix();
			poseStack.popPose();
		}
	}
}
