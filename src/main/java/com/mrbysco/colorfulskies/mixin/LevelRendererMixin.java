package com.mrbysco.colorfulskies.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.colorfulskies.client.ClientHandler;
import com.mrbysco.colorfulskies.client.Color;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

	@Shadow
	@Nullable
	private ClientLevel level;

	@Inject(method = "renderSky(Lcom/mojang/blaze3d/vertex/PoseStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/Camera;ZLjava/lang/Runnable;)V", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/multiplayer/ClientLevel;getMoonPhase()I",
			shift = Shift.BEFORE,
			ordinal = 0
	))
	private void colorfulskies_colorMoon(PoseStack poseStack, Matrix4f matrix4f, float partialTick, Camera camera, boolean bl, Runnable runnable, CallbackInfo ci) {
		if (this.level != null) {
			ClientHandler.colorTheMoon(level, poseStack, matrix4f, partialTick, camera);
		}
	}

	@Inject(method = "renderSky(Lcom/mojang/blaze3d/vertex/PoseStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/Camera;ZLjava/lang/Runnable;)V", at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V",
			shift = Shift.AFTER,
			ordinal = 0
	))
	private void colorfulskies_colorSun(PoseStack poseStack, Matrix4f matrix4f, float partialTick, Camera camera, boolean bl, Runnable runnable, CallbackInfo ci) {
		if (this.level != null) {
			ClientHandler.colorTheSun(level, poseStack, matrix4f, partialTick, camera);
		}
	}

	@ModifyArg(method = "renderSky",
			slice = @Slice(
					from = @At(ordinal = 1, value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferUploader;drawWithShader(Lcom/mojang/blaze3d/vertex/BufferBuilder$RenderedBuffer;)V"),
					to = @At(ordinal = 0, value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getMoonPhase()I")
			),
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V"),
			index = 1)
	public ResourceLocation colorfulskies_changeMoonTexture(ResourceLocation location) {
		return ClientHandler.getMoonTexture(location);
	}

	@ModifyArg(method = "renderSky",
			slice = @Slice(
					from = @At(ordinal = 1, value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getTimeOfDay(F)F"),
					to = @At(ordinal = 1, value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/BufferBuilder;begin(Lcom/mojang/blaze3d/vertex/VertexFormat$Mode;Lcom/mojang/blaze3d/vertex/VertexFormat;)V")
			),
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V"),
			index = 1)
	public ResourceLocation colorfulskies_changeSunTexture(ResourceLocation location) {
		return ClientHandler.getSunTexture(location);
	}

	@Redirect(method = "renderSky",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/DimensionSpecialEffects;getSunriseColor(FF)[F"))
	protected float[] colorfulskies_changeSunriseColor(DimensionSpecialEffects specialEffects, float timeOfDay, float partialTicks) {
		float[] sunriseColors = specialEffects.getSunriseColor(timeOfDay, partialTicks);
		return ClientHandler.getSunriseColors(sunriseColors, timeOfDay, partialTicks);
	}
}