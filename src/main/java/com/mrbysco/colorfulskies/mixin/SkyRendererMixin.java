package com.mrbysco.colorfulskies.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.colorfulskies.client.ClientHandler;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkyRenderer.class)
public class SkyRendererMixin {

	@ModifyArg(
			method = "renderMoon(IFLnet/minecraft/client/renderer/MultiBufferSource;Lcom/mojang/blaze3d/vertex/PoseStack;)V",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;setColor(I)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
			)
	)
	private int colorfulskies_colorMoon(int originalColor) {
		return ClientHandler.colorTheMoon(originalColor);
	}

	@ModifyArg(
			method = "renderSun(FLnet/minecraft/client/renderer/MultiBufferSource;Lcom/mojang/blaze3d/vertex/PoseStack;)V",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;setColor(I)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
			)
	)
	private int colorfulskies_colorSun(int originalColor) {
		return ClientHandler.colorTheSun(originalColor);
	}

	@ModifyArg(
			method = "renderSun(FLnet/minecraft/client/renderer/MultiBufferSource;Lcom/mojang/blaze3d/vertex/PoseStack;)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/RenderType;celestial(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;"),
			index = 0)
	public ResourceLocation colorfulskies_changeSunTexture(ResourceLocation location) {
		return ClientHandler.getSunTexture(location);
	}

	@Inject(
			method = "renderSunriseAndSunset(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;FI)V",
			at = @At("HEAD"), cancellable = true)
	public void colorfulskies_changeSunriseColor(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, float sunAngle, int color, CallbackInfo ci) {
		Integer newColor = ClientHandler.getSunriseColor();
		if (newColor != null) {
			ClientHandler.renderCustomSunrise(poseStack, bufferSource, sunAngle);
			ci.cancel();
		}
	}
}