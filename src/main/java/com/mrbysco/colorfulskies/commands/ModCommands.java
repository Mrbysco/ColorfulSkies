package com.mrbysco.colorfulskies.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mrbysco.colorfulskies.ColorfulSkies;
import com.mrbysco.colorfulskies.network.PacketHandler;
import com.mrbysco.colorfulskies.network.message.CloudColorMessage;
import com.mrbysco.colorfulskies.network.message.DisableSunriseMessage;
import com.mrbysco.colorfulskies.network.message.MoonColorMessage;
import com.mrbysco.colorfulskies.network.message.SunColorMessage;
import com.mrbysco.colorfulskies.network.message.SunriseColorMessage;
import com.mrbysco.colorfulskies.world.SkyColorData;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

import java.util.Collection;
import java.util.Collections;

public class ModCommands {
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(ColorfulSkies.MOD_ID);
		root.requires((sourceStack) -> sourceStack.hasPermission(2))
				.then(Commands.literal("disablesunrise")
						.then(Commands.argument("players", EntityArgument.players())
								.then(Commands.argument("disabled", BoolArgumentType.bool()).executes(ModCommands::disableSunrise)))
				)
				.then(Commands.literal("color")
						.then(Commands.argument("players", EntityArgument.players())
								.then(Commands.literal("moon")
										.then(Commands.argument("hex", StringArgumentType.word()).suggests((cs, builder) ->
														SharedSuggestionProvider.suggest(Collections.singleton("clear"), builder))
												.executes(ModCommands::setMoonColor)))
								.then(Commands.literal("cloud")
										.then(Commands.argument("hex", StringArgumentType.word()).suggests((cs, builder) ->
														SharedSuggestionProvider.suggest(Collections.singleton("clear"), builder))
												.executes(ModCommands::setCloudColor)))
								.then(Commands.literal("sun")
										.then(Commands.argument("hex", StringArgumentType.word()).suggests((cs, builder) ->
														SharedSuggestionProvider.suggest(Collections.singleton("clear"), builder))
												.executes(ModCommands::setSunColor)))
								.then(Commands.literal("sunrise")
										.then(Commands.argument("hex", StringArgumentType.word()).suggests((cs, builder) ->
														SharedSuggestionProvider.suggest(Collections.singleton("clear"), builder))
												.executes(ModCommands::setSunriseColor)))
						)
				)
		;
		dispatcher.register(root);
	}

	private static int disableSunrise(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		boolean disabled = BoolArgumentType.getBool(context, "disabled");
		Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "players");
		for (ServerPlayer player : players) {
			SkyColorData colorData = SkyColorData.get(player.level());
			colorData.setSunriseDisabledForUUID(player.getUUID(), disabled);
			PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new DisableSunriseMessage(disabled));
		}

		if (disabled) {
			context.getSource().sendSuccess(() -> Component.translatable("colorfulskies.commands.sunrise.success", disabled).withStyle(ChatFormatting.YELLOW), true);
		} else {
			context.getSource().sendSuccess(() -> Component.translatable("colorfulskies.commands.sunrise.success1", disabled).withStyle(ChatFormatting.YELLOW), true);
		}

		return 0;
	}

	private static int setCloudColor(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		String value = StringArgumentType.getString(context, "hex");
		final String hex = value.startsWith("#") ? value : "#" + value;
		if (!hex.equals("#clear") && !hex.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")) {
			context.getSource().sendFailure(Component.translatable("colorfulskies.commands.color.invalid_hex"));
			return 0;
		}
		try {
			int color = hex.equals("#clear") ? -1 : Integer.decode(hex);

			Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "players");
			for (ServerPlayer player : players) {
				SkyColorData colorData = SkyColorData.get(player.level());
				colorData.setCloudColorForUUID(player.getUUID(), color);
				PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new CloudColorMessage(color));
			}

			if (color == -1) {
				context.getSource().sendSuccess(() -> Component.translatable("colorfulskies.commands.color.cloud.reset")
						.withStyle(ChatFormatting.YELLOW), true);
			} else {
				context.getSource().sendSuccess(() -> Component.translatable("colorfulskies.commands.color.cloud.success",
						hex).withStyle(ChatFormatting.YELLOW), true);
			}
		} catch (NumberFormatException e) {
			context.getSource().sendFailure(Component.translatable("colorfulskies.commands.color.invalid_hex"));
			return 0;
		}

		return 0;
	}

	private static int setSunColor(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		String value = StringArgumentType.getString(context, "hex");
		final String hex = value.startsWith("#") ? value : "#" + value;
		if (!hex.equals("#clear") && !hex.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")) {
			context.getSource().sendFailure(Component.translatable("colorfulskies.commands.color.invalid_hex"));
			return 0;
		}
		try {
			int color = hex.equals("#clear") ? -1 : Integer.decode(hex);

			Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "players");
			for (ServerPlayer player : players) {
				SkyColorData colorData = SkyColorData.get(player.level());
				colorData.setSunColorForUUID(player.getUUID(), color);
				PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SunColorMessage(color));
			}

			if (color == -1) {
				context.getSource().sendSuccess(() -> Component.translatable("colorfulskies.commands.color.sun.reset")
						.withStyle(ChatFormatting.YELLOW), true);
			} else {
				context.getSource().sendSuccess(() -> Component.translatable("colorfulskies.commands.color.sun.success",
						hex).withStyle(ChatFormatting.YELLOW), true);
			}
		} catch (NumberFormatException e) {
			context.getSource().sendFailure(Component.translatable("colorfulskies.commands.color.invalid_hex"));
			return 0;
		}

		return 0;
	}

	private static int setSunriseColor(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		String value = StringArgumentType.getString(context, "hex");
		final String hex = value.startsWith("#") ? value : "#" + value;
		if (!hex.equals("#clear") && !hex.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")) {
			context.getSource().sendFailure(Component.translatable("colorfulskies.commands.color.invalid_hex"));
			return 0;
		}
		try {
			int color = hex.equals("#clear") ? -1 : Integer.decode(hex);

			Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "players");
			for (ServerPlayer player : players) {
				SkyColorData colorData = SkyColorData.get(player.level());
				colorData.setSunriseColorForUUID(player.getUUID(), color);
				PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SunriseColorMessage(color));
			}

			if (color == -1) {
				context.getSource().sendSuccess(() -> Component.translatable("colorfulskies.commands.color.sunrise.reset")
						.withStyle(ChatFormatting.YELLOW), true);
			} else {
				context.getSource().sendSuccess(() -> Component.translatable("colorfulskies.commands.color.sunrise.success",
						hex).withStyle(ChatFormatting.YELLOW), true);
			}
		} catch (NumberFormatException e) {
			context.getSource().sendFailure(Component.translatable("colorfulskies.commands.color.invalid_hex"));
			return 0;
		}

		return 0;
	}

	private static int setMoonColor(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		String value = StringArgumentType.getString(context, "hex");
		final String hex = value.startsWith("#") ? value : "#" + value;
		if (!hex.equals("#clear") && !hex.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")) {
			context.getSource().sendFailure(Component.translatable("colorfulskies.commands.color.invalid_hex"));
			return 0;
		}
		try {
			int color = hex.equals("#clear") ? -1 : Integer.decode(hex);

			Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "players");
			for (ServerPlayer player : players) {
				SkyColorData colorData = SkyColorData.get(player.level());
				colorData.setMoonColorForUUID(player.getUUID(), color);
				PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new MoonColorMessage(color));
			}

			if (color == -1) {
				context.getSource().sendSuccess(() -> Component.translatable("colorfulskies.commands.color.moon.reset")
						.withStyle(ChatFormatting.YELLOW), true);
			} else {
				context.getSource().sendSuccess(() -> Component.translatable("colorfulskies.commands.color.moon.success",
						hex).withStyle(ChatFormatting.YELLOW), true);
			}
		} catch (NumberFormatException e) {
			context.getSource().sendFailure(Component.translatable("colorfulskies.commands.color.invalid_hex"));
			return 0;
		}

		return 0;
	}
}
