package com.mrbysco.colorfulskies.mixin;

import com.mrbysco.colorfulskies.client.ClientHandler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level {

	protected ClientLevelMixin(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess,
	                           Holder<DimensionType> dimensionTypeRegistration, boolean isClientSide, boolean isDebug,
	                           long biomeZoomSeed, int maxChainedNeighborUpdates) {
		super(levelData, dimension, registryAccess, dimensionTypeRegistration, isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates);
	}

	@Inject(at = @At(value = "HEAD"), method = "getCloudColor(F)I",
			cancellable = true)
	public void colorfulskies_colorClouds(float partialTick, CallbackInfoReturnable<Integer> cir) {
		Integer color = ClientHandler.getCloudColor();
		if (color != null) {
			cir.setReturnValue(color);
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "getSkyColor(Lnet/minecraft/world/phys/Vec3;F)I",
			cancellable = true)
	public void colorfulskies_colorSky(Vec3 pPos, float partialTick, CallbackInfoReturnable<Integer> cir) {
		Vec3 color = ClientHandler.getSkyColor();
		if (color != null) {
			float timeOffDay = this.getTimeOfDay(partialTick);
			float rainLevel = this.getRainLevel(partialTick);
			float thunderLevel = this.getThunderLevel(partialTick);
			int flashTime = ((ClientLevel) (Object) this).getSkyFlashTime();
			cir.setReturnValue(ClientHandler.generateSkyColor(color, timeOffDay, rainLevel, thunderLevel, flashTime, partialTick));
		}
	}
}