/*
 * Copyright (C) 2014 - 2020 | Alexander01998 | All rights reserved.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.autoitemswitch.utils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public final class ChatUtils {
	public static final String INFO_PREFIX =
		"\u00a7c[\u00a76INFO\u00a7c]\u00a7r ";
	private static final String WARNING_PREFIX =
		"\u00a7c[\u00a76\u00a7lWARNING\u00a7c]\u00a7r ";
	private static final String ERROR_PREFIX =
		"\u00a7c[\u00a74\u00a7lERROR\u00a7c]\u00a7r ";
	private static final String SYNTAX_ERROR_PREFIX =
		"\u00a74Syntax error:\u00a7r ";
	
	private ChatUtils() {
		throw new AssertionError();
	}
	
	@SuppressWarnings("resource")
	@Environment(EnvType.CLIENT)
	public static void component(Text component) {
		MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(component);
	}

	@Environment(EnvType.CLIENT)
	public static void showMessage(String message) {
		component(new LiteralText(message));
	}

	@Environment(EnvType.CLIENT)
	public static void info(String message) {
		showMessage(INFO_PREFIX + message);
	}

	@Environment(EnvType.CLIENT)
	public static void warning(String message) {
		showMessage(WARNING_PREFIX + message);
	}

	@Environment(EnvType.CLIENT)
	public static void error(String message) {
		showMessage(ERROR_PREFIX + message);
	}

	@Environment(EnvType.CLIENT)
	public static void syntaxError(String message) {
		showMessage(SYNTAX_ERROR_PREFIX + message);
	}
	
	@SuppressWarnings("resource")
	@Environment(EnvType.CLIENT)
	public static void sendMessage(String message) {
		MinecraftClient.getInstance().player.networkHandler
		.sendPacket(new ChatMessageC2SPacket(message));
	}
}
