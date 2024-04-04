package com.mrbysco.colorfulskies.mixin;

import com.mrbysco.colorfulskies.client.ClientHandler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelTimeAccess;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level {
	protected ClientLevelMixin(WritableLevelData pLevelData, ResourceKey<Level> pDimension, RegistryAccess pRegistryAccess, Holder<DimensionType> pDimensionTypeRegistration, Supplier<ProfilerFiller> pProfiler, boolean pIsClientSide, boolean pIsDebug, long pBiomeZoomSeed, int pMaxChainedNeighborUpdates) {
		super(pLevelData, pDimension, pRegistryAccess, pDimensionTypeRegistration, pProfiler, pIsClientSide, pIsDebug, pBiomeZoomSeed, pMaxChainedNeighborUpdates);
	}

	@Inject(at = @At(value = "HEAD"), method = "getCloudColor(F)Lnet/minecraft/world/phys/Vec3;",
			cancellable = true)
	public void colorfulskies_colorClouds(float partialTick, CallbackInfoReturnable<Vec3> cir) {
		Vec3 color = ClientHandler.getCloudColor();
		if (color != null) {
			cir.setReturnValue(color);
		}
	}

	@Inject(at = @At(value = "HEAD"), method = "getSkyColor(Lnet/minecraft/world/phys/Vec3;F)Lnet/minecraft/world/phys/Vec3;",
			cancellable = true)
	public void colorfulskies_colorSky(Vec3 pPos, float partialTick, CallbackInfoReturnable<Vec3> cir) {
		Vec3 color = ClientHandler.getSkyColor();
		if (color != null) {
			float timeOffDay = this.getTimeOfDay(partialTick);
			float rainLevel = this.getRainLevel(partialTick);
			float thunderLevel = this.getThunderLevel(partialTick);
			int flashTime = ((ClientLevel) (Object) this).getSkyFlashTime();
			color = ClientHandler.generateSkyColor(color, timeOffDay, rainLevel, thunderLevel, flashTime, partialTick);

			cir.setReturnValue(color);
		}
	}
}