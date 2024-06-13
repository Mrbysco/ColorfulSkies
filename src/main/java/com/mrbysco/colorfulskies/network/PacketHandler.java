package com.mrbysco.colorfulskies.network;

import com.mrbysco.colorfulskies.ColorfulSkies;
import com.mrbysco.colorfulskies.network.handler.ClientPayloadHandler;
import com.mrbysco.colorfulskies.network.message.CloudColorPayload;
import com.mrbysco.colorfulskies.network.message.DisableSunrisePayload;
import com.mrbysco.colorfulskies.network.message.MoonColorPayload;
import com.mrbysco.colorfulskies.network.message.SkyColorPayload;
import com.mrbysco.colorfulskies.network.message.SunColorPayload;
import com.mrbysco.colorfulskies.network.message.SunriseColorPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PacketHandler {

	public static void setupPackets(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar(ColorfulSkies.MOD_ID);

		registrar.playToClient(CloudColorPayload.ID, CloudColorPayload.CODEC, ClientPayloadHandler.getInstance()::handleCloudData);
		registrar.playToClient(DisableSunrisePayload.ID, DisableSunrisePayload.CODEC, ClientPayloadHandler.getInstance()::handleDisableData);
		registrar.playToClient(MoonColorPayload.ID, MoonColorPayload.CODEC, ClientPayloadHandler.getInstance()::handleMoonData);
		registrar.playToClient(SunColorPayload.ID, SunColorPayload.CODEC, ClientPayloadHandler.getInstance()::handleSunData);
		registrar.playToClient(SunriseColorPayload.ID, SunriseColorPayload.CODEC, ClientPayloadHandler.getInstance()::handleSunriseData);
		registrar.playToClient(SkyColorPayload.ID, SkyColorPayload.CODEC, ClientPayloadHandler.getInstance()::handleSkyData);
	}
}
