package com.mrbysco.colorfulskies;

import com.mojang.logging.LogUtils;
import com.mrbysco.colorfulskies.commands.ModCommands;
import com.mrbysco.colorfulskies.handler.SyncHandler;
import com.mrbysco.colorfulskies.network.PacketHandler;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ColorfulSkies.MOD_ID)
public class ColorfulSkies {
	public static final String MOD_ID = "colorfulskies";
	public static final Logger LOGGER = LogUtils.getLogger();

	public ColorfulSkies() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		eventBus.addListener(this::setup);
		NeoForge.EVENT_BUS.addListener(this::onCommandRegister);
		NeoForge.EVENT_BUS.register(new SyncHandler());
	}

	private void setup(final FMLCommonSetupEvent event) {
		PacketHandler.init();
	}

	public void onCommandRegister(RegisterCommandsEvent event) {
		ModCommands.register(event.getDispatcher());
	}
}
