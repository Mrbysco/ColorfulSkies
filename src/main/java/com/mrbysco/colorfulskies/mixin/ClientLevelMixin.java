package com.mrbysco.colorfulskies.mixin;

import com.mrbysco.colorfulskies.client.ClientHandler;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.ARGB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LevelRenderer.class)
public abstract class ClientLevelMixin implements ResourceManagerReloadListener, AutoCloseable {

	@ModifyArg(
			method = "renderLevel(Lcom/mojang/blaze3d/resource/GraphicsResourceAllocator;Lnet/minecraft/client/DeltaTracker;ZLnet/minecraft/client/Camera;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lorg/joml/Vector4f;Z)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/LevelRenderer;addCloudsPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/CloudStatus;Lnet/minecraft/world/phys/Vec3;JFIFLorg/joml/Matrix4f;)V"
			), index = 5
	)
	private int colorfulskies_colorClouds(int cloudColor) {
		Integer color = ClientHandler.getCloudColor();
		if (color != null) {
			return ARGB.opaque(color);
		}
		return cloudColor;
	}
}