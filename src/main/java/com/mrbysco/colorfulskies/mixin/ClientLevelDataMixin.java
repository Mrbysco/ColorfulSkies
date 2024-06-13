package com.mrbysco.colorfulskies.mixin;

import com.mrbysco.colorfulskies.client.ClientHandler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.LevelHeightAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.ClientLevelData.class)
public class ClientLevelDataMixin {

	@Inject(method = "getHorizonHeight(Lnet/minecraft/world/level/LevelHeightAccessor;)D", at = @At(
			value = "HEAD"
	), cancellable = true)
	public void getHorizonHeight(LevelHeightAccessor accessor, CallbackInfoReturnable<Double> cir) {
		if (ClientHandler.sunriseDisabled) {
			cir.setReturnValue(ClientHandler.getHorizon(accessor));
		}
	}
}