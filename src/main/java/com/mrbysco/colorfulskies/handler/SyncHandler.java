package com.mrbysco.colorfulskies.handler;

import com.mrbysco.colorfulskies.world.SkyColorData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class SyncHandler {
	@SubscribeEvent
	public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
		Player player = event.getEntity();
		if (!player.level().isClientSide) {
			SkyColorData colorData = SkyColorData.get(player.level());
			colorData.syncColors((ServerPlayer) player);
		}
	}
}
