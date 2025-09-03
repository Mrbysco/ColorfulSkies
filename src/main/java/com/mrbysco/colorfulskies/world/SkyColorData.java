package com.mrbysco.colorfulskies.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.colorfulskies.ColorfulSkies;
import com.mrbysco.colorfulskies.network.message.CloudColorPayload;
import com.mrbysco.colorfulskies.network.message.DisableSunrisePayload;
import com.mrbysco.colorfulskies.network.message.MoonColorPayload;
import com.mrbysco.colorfulskies.network.message.SkyColorPayload;
import com.mrbysco.colorfulskies.network.message.SunColorPayload;
import com.mrbysco.colorfulskies.network.message.SunriseColorPayload;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SkyColorData extends SavedData {
	private static final String DATA_NAME = ColorfulSkies.MOD_ID + "_world_data";

	private static final Codec<Map<UUID, SkyColorInfo>> MAP_CODEC = Codec.unboundedMap(UUIDUtil.STRING_CODEC, SkyColorInfo.CODEC);
	private static final Codec<SkyColorData> CODEC = RecordCodecBuilder.create(inst -> inst.group(
					MAP_CODEC
							.fieldOf("skyColorDataMap").forGetter(data -> data.skyColorDataMap)
			)
			.apply(inst, SkyColorData::new));


	private final Map<UUID, SkyColorInfo> skyColorDataMap = new HashMap<>();

	public SkyColorData(Map<UUID, SkyColorInfo> dataMap) {
		this.skyColorDataMap.clear();
		this.skyColorDataMap.putAll(dataMap);
	}

	public SkyColorData() {
		this(new HashMap<>());
	}

	public static SavedDataType<SkyColorData> type() {
		return new SavedDataType<>(DATA_NAME, SkyColorData::new, CODEC, null);
	}

	public static SkyColorData get(Level level) {
		if (!(level instanceof ServerLevel)) {
			throw new RuntimeException("Attempted to get the data from a client world. This is wrong.");
		}
		ServerLevel overworld = level.getServer().getLevel(Level.OVERWORLD);

		assert overworld != null;
		DimensionDataStorage storage = overworld.getDataStorage();
		return storage.computeIfAbsent(type());
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
		player.connection.send(new CloudColorPayload(info.cloud));
		player.connection.send(new MoonColorPayload(info.moon));
		player.connection.send(new SunColorPayload(info.sun));
		player.connection.send(new SkyColorPayload(info.sky));
		player.connection.send(new DisableSunrisePayload(info.disableSunrise));
		if (!info.disableSunrise) player.connection.send(new SunriseColorPayload(info.sun));
	}

	public record SkyColorInfo(int cloud, int moon, int sun, int sunrise, int sky, boolean disableSunrise) {
		public static Codec<SkyColorInfo> CODEC = RecordCodecBuilder.create(
				instance -> instance.group(
								Codec.INT.fieldOf("Cloud").forGetter(SkyColorInfo::cloud),
								Codec.INT.fieldOf("Moon").forGetter(SkyColorInfo::moon),
								Codec.INT.fieldOf("Sun").forGetter(SkyColorInfo::sun),
								Codec.INT.fieldOf("Sunrise").forGetter(SkyColorInfo::sunrise),
								Codec.INT.fieldOf("Sky").forGetter(SkyColorInfo::sky),
								Codec.BOOL.fieldOf("DisableSunrise").forGetter(SkyColorInfo::disableSunrise)
						)
						.apply(instance, SkyColorInfo::new)
		);
	}
}
