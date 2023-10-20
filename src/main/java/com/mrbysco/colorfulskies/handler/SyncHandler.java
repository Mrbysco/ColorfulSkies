package com.mrbysco.colorfulskies.handler;

import com.mrbysco.colorfulskies.world.SkyColorData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
