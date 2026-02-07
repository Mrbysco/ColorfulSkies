package com.mrbysco.colorfulskies.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.colorfulskies.client.ClientHandler;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.AtlasManager;
import org.joml.Vector4fc;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkyRenderer.class)
public abstract class SkyRendererMixin {
	@Shadow
	private static GpuBuffer buildCelestialQuad(String name, TextureAtlasSprite atlas) {
		return null;
	}

	@Shadow
	@Final
	private TextureAtlas celestialsAtlas;
	@Shadow
	@Final
	private GpuBuffer sunriseBuffer;
	@Unique
	private GpuBuffer colorfulskies$sunbuffer;

	@Inject(
			method = "<init>(Lnet/minecraft/client/renderer/texture/TextureManager;Lnet/minecraft/client/resources/model/AtlasManager;)V",
			at = @At("TAIL"))
	private void colorfulskies_changeSunTexture(TextureManager textureManager, AtlasManager atlasManager, CallbackInfo ci) {
		if (colorfulskies$sunbuffer == null) {
			colorfulskies$sunbuffer = buildCelestialQuad("Custom Sun quad", celestialsAtlas.getSprite(ClientHandler.CUSTOM_SUN_LOCATION));
		}
	}

	@ModifyArg(
			method = "renderMoon(Lnet/minecraft/world/level/MoonPhase;FLcom/mojang/blaze3d/vertex/PoseStack;)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/DynamicUniforms;writeTransform(Lorg/joml/Matrix4fc;Lorg/joml/Vector4fc;Lorg/joml/Vector3fc;Lorg/joml/Matrix4fc;)Lcom/mojang/blaze3d/buffers/GpuBufferSlice;"
			)
	)
	private Vector4fc colorfulskies_colorMoon(Vector4fc originalColor) {
		return ClientHandler.colorTheMoon(originalColor);
	}

	@ModifyArg(
			method = "renderSun(FLcom/mojang/blaze3d/vertex/PoseStack;)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/DynamicUniforms;writeTransform(Lorg/joml/Matrix4fc;Lorg/joml/Vector4fc;Lorg/joml/Vector3fc;Lorg/joml/Matrix4fc;)Lcom/mojang/blaze3d/buffers/GpuBufferSlice;"
			)
	)
	private Vector4fc colorfulskies_colorSun(Vector4fc colorModulator, @Local(argsOnly = true) float rainBrightness) {
		return ClientHandler.colorTheSun(colorModulator, rainBrightness);
	}


	@ModifyArg(
			method = "renderSun(FLcom/mojang/blaze3d/vertex/PoseStack;)V",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/blaze3d/systems/RenderPass;setVertexBuffer(ILcom/mojang/blaze3d/buffers/GpuBuffer;)V"
			),
			index = 1
	)
	private GpuBuffer colorfulskies_changeSunBuffer(GpuBuffer buffer) {
		if (ClientHandler.hasSunTexture()) {
			return this.colorfulskies$sunbuffer;
		}
		return buffer;
	}

	@Inject(
			method = "renderSunriseAndSunset(Lcom/mojang/blaze3d/vertex/PoseStack;FI)V",
			at = @At("HEAD"), cancellable = true)
	public void colorfulskies_changeSunriseColor(PoseStack poseStack, float sunAngle, int color, CallbackInfo ci) {
		Integer newColor = ClientHandler.getSunriseColor();
		if (newColor != null) {
			ClientHandler.renderCustomSunrise(poseStack, sunAngle, this.sunriseBuffer);
			ci.cancel();
		}
	}


	@ModifyArg(
			method = "renderSkyDisc(I)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/DynamicUniforms;writeTransform(Lorg/joml/Matrix4fc;Lorg/joml/Vector4fc;Lorg/joml/Vector3fc;Lorg/joml/Matrix4fc;)Lcom/mojang/blaze3d/buffers/GpuBufferSlice;"
			)
	)
	private Vector4fc colorfulskies_colorSky(Vector4fc colorModulator) {
		return ClientHandler.colorTheSky(colorModulator);
	}

}