package com.mrbysco.colorfulskies.network;

import com.mrbysco.colorfulskies.ColorfulSkies;
import com.mrbysco.colorfulskies.network.handler.ClientPayloadHandler;
import com.mrbysco.colorfulskies.network.message.CloudColorPayload;
import com.mrbysco.colorfulskies.network.message.DisableSunrisePayload;
import com.mrbysco.colorfulskies.network.message.MoonColorPayload;
import com.mrbysco.colorfulskies.network.message.SunColorPayload;
import com.mrbysco.colorfulskies.network.message.SunriseColorPayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

public class PacketHandler {

	public static void setupPackets(final RegisterPayloadHandlerEvent event) {
		final IPayloadRegistrar registrar = event.registrar(ColorfulSkies.MOD_ID);

		registrar.play(CloudColorPayload.ID, CloudColorPayload::new, handler -> handler
				.client(ClientPayloadHandler.getInstance()::handleCloudData));
		registrar.play(DisableSunrisePayload.ID, DisableSunrisePayload::new, handler -> handler
				.client(ClientPayloadHandler.getInstance()::handleDisableData));
		registrar.play(MoonColorPayload.ID, MoonColorPayload::new, handler -> handler
				.client(ClientPayloadHandler.getInstance()::handleMoonData));
		registrar.play(SunColorPayload.ID, SunColorPayload::new, handler -> handler
				.client(ClientPayloadHandler.getInstance()::handleSunData));
		registrar.play(SunriseColorPayload.ID, SunriseColorPayload::new, handler -> handler
				.client(ClientPayloadHandler.getInstance()::handleSunriseData));
	}
}
