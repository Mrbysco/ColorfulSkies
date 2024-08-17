package com.mrbysco.colorfulskies.world;

import com.mrbysco.colorfulskies.ColorfulSkies;
import com.mrbysco.colorfulskies.network.PacketHandler;
import com.mrbysco.colorfulskies.network.message.CloudColorMessage;
import com.mrbysco.colorfulskies.network.message.DisableSunriseMessage;
import com.mrbysco.colorfulskies.network.message.MoonColorMessage;
import com.mrbysco.colorfulskies.network.message.SunColorMessage;
import com.mrbysco.colorfulskies.network.message.SunriseColorMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.network.PacketDistributor;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class SkyColorData extends SavedData {
	private static final Random random = new Random();
	private static final String DATA_NAME = ColorfulSkies.MOD_ID + "_world_data";

	private static final Map<UUID, SkyColorInfo> skyColorDataMap = new HashMap<>();

	public SkyColorData(Map<UUID, SkyColorInfo> dataMap) {
		this.skyColorDataMap.clear();
		this.skyColorDataMap.putAll(dataMap);
	}

	public SkyColorData() {
		this(new HashMap<>());
	}

	public CompoundTag save(CompoundTag tag) {
		saveMap(tag, skyColorDataMap);
		return tag;
	}

	private static CompoundTag saveMap(CompoundTag tag, Map<UUID, SkyColorInfo> map) {
		ListTag skyColorList = new ListTag();
		for (Map.Entry<UUID, SkyColorInfo> entry : skyColorDataMap.entrySet()) {
			CompoundTag skyTag = new CompoundTag();
			skyTag.putUUID("UUID", entry.getKey());
			skyTag.putInt("Cloud", entry.getValue().cloud());
			skyTag.putInt("Moon", entry.getValue().moon());
			skyTag.putInt("Sun", entry.getValue().sun());
			skyTag.putInt("Sunrise", entry.getValue().sunrise());
			skyTag.putInt("Sky", entry.getValue().sky());
			skyTag.putBoolean("DisableSunrise", entry.getValue().disableSunrise());

			skyColorList.add(skyTag);
		}
		tag.put("SkyColorMap", skyColorList);

		return tag;
	}

	public static com.mrbysco.colorfulskies.world.SkyColorData load(CompoundTag tag) {
		ListTag skyColorList = tag.getList("SkyColorMap", CompoundTag.TAG_COMPOUND);
		Map<UUID, SkyColorInfo> skyColorMap = new HashMap<>();
		for (int i = 0; i < skyColorList.size(); ++i) {
			CompoundTag listTag = skyColorList.getCompound(i);
			UUID uuid = listTag.getUUID("UUID");
			int cloud = listTag.getInt("Cloud");
			int moon = listTag.getInt("Moon");
			int sun = listTag.getInt("Sun");
			int sunrise = listTag.getInt("Sunrise");
			int sky = listTag.getInt("Sky");
			boolean disableSunrise = listTag.getBoolean("DisableSunrise");
			skyColorMap.put(uuid, new SkyColorInfo(cloud, moon, sun, sunrise, sky, disableSunrise));
		}
		return new com.mrbysco.colorfulskies.world.SkyColorData(skyColorMap);
	}

	public static com.mrbysco.colorfulskies.world.SkyColorData get(Level level) {
		if (!(level instanceof ServerLevel)) {
			throw new RuntimeException("Attempted to get the data from a client world. This is wrong.");
		}
		ServerLevel overworld = level.getServer().getLevel(Level.OVERWORLD);

		DimensionDataStorage storage = overworld.getDataStorage();
		return storage.computeIfAbsent(com.mrbysco.colorfulskies.world.SkyColorData::load, com.mrbysco.colorfulskies.world.SkyColorData::new, DATA_NAME);
	}

	public void setColorForUUID(UUID uuid, SkyColorInfo colorInfo) {
		this.skyColorDataMap.put(uuid, colorInfo);
		setDirty();
	}

	public void setCloudColorForUUID(UUID uuid, int color) {
		SkyColorInfo info = this.skyColorDataMap.getOrDefault(uuid, new SkyColorInfo(-1, -1, -1, -1, -1, false));
		this.skyColorDataMap.put(uuid, new SkyColorInfo(color, info.moon(), info.sun(), info.sunrise(), info.sky(), info.disableSunrise()));
		setDirty();
	}

	public void setMoonColorForUUID(UUID uuid, int color) {
		SkyColorInfo info = this.skyColorDataMap.getOrDefault(uuid, new SkyColorInfo(-1, -1, -1, -1, -1, false));
		this.skyColorDataMap.put(uuid, new SkyColorInfo(info.cloud(), color, info.sun(), info.sunrise(), info.sky(), info.disableSunrise()));
		setDirty();
	}

	public void setSunColorForUUID(UUID uuid, int color) {
		SkyColorInfo info = this.skyColorDataMap.getOrDefault(uuid, new SkyColorInfo(-1, -1, -1, -1, -1, false));
		this.skyColorDataMap.put(uuid, new SkyColorInfo(info.cloud(), info.moon(), color, info.sunrise(), info.sky(), info.disableSunrise()));
		setDirty();
	}

	public void setSunriseColorForUUID(UUID uuid, int color) {
		SkyColorInfo info = this.skyColorDataMap.getOrDefault(uuid, new SkyColorInfo(-1, -1, -1, -1, -1, false));
		this.skyColorDataMap.put(uuid, new SkyColorInfo(info.cloud(), info.moon(), info.sun(), color, info.sky(), info.disableSunrise()));
		setDirty();
	}

	public void setSunriseDisabledForUUID(UUID uuid, boolean disabled) {
		SkyColorInfo info = this.skyColorDataMap.getOrDefault(uuid, new SkyColorInfo(-1, -1, -1, -1, -1, false));
		this.skyColorDataMap.put(uuid, new SkyColorInfo(info.cloud(), info.moon(), info.sun(), info.sunrise(), info.sky(), disabled));
		setDirty();
	}

	public void setSkyColorForUUID(UUID uuid, int color) {
		SkyColorInfo info = this.skyColorDataMap.getOrDefault(uuid, new SkyColorInfo(-1, -1, -1, -1, -1, false));
		this.skyColorDataMap.put(uuid, new SkyColorInfo(info.cloud(), info.moon(), info.sun(), info.sunrise(), color, info.disableSunrise()));
		setDirty();
	}

	public void syncColors(ServerPlayer player) {
		SkyColorInfo info = this.skyColorDataMap.getOrDefault(player.getUUID(), new SkyColorInfo(-1, -1, -1, -1, -1, false));
		PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new CloudColorMessage(info.cloud));
		PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new DisableSunriseMessage(info.disableSunrise));
		PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new MoonColorMessage(info.moon));
		PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SunColorMessage(info.sun));
		PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SunriseColorMessage(info.sunrise));
	}

	public record SkyColorInfo(int cloud, int moon, int sun, int sunrise, int sky, boolean disableSunrise) {

	}
}
