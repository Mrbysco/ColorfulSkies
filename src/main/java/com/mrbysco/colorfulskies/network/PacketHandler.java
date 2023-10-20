package com.mrbysco.colorfulskies.network;

import com.mrbysco.colorfulskies.ColorfulSkies;
import com.mrbysco.colorfulskies.network.message.CloudColorMessage;
import com.mrbysco.colorfulskies.network.message.DisableSunriseMessage;
import com.mrbysco.colorfulskies.network.message.MoonColorMessage;
import com.mrbysco.colorfulskies.network.message.SunColorMessage;
import com.mrbysco.colorfulskies.network.message.SunriseColorMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(ColorfulSkies.MOD_ID, "main"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);

	private static int id = 0;

	public static void init() {
		CHANNEL.registerMessage(id++, MoonColorMessage.class, MoonColorMessage::encode, MoonColorMessage::decode, MoonColorMessage::handle);
		CHANNEL.registerMessage(id++, CloudColorMessage.class, CloudColorMessage::encode, CloudColorMessage::decode, CloudColorMessage::handle);
		CHANNEL.registerMessage(id++, SunColorMessage.class, SunColorMessage::encode, SunColorMessage::decode, SunColorMessage::handle);
		CHANNEL.registerMessage(id++, SunriseColorMessage.class, SunriseColorMessage::encode, SunriseColorMessage::decode, SunriseColorMessage::handle);
		CHANNEL.registerMessage(id++, DisableSunriseMessage.class, DisableSunriseMessage::encode, DisableSunriseMessage::decode, DisableSunriseMessage::handle);
	}
}
