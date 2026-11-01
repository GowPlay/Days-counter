package com.dayscounter;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaysCounterClient implements ClientModInitializer {
	public static final String MOD_ID = "dayscounter";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final long TICKS_PER_DAY = 24000L;

	public static DaysCounterConfig config;

	private static KeyMapping keyPosition;
	private static KeyMapping keyFont;
	private static KeyMapping keyToggle;

	private static String feedback = null;
	private static long feedbackUntil = 0L;

	@Override
	public void onInitializeClient() {
		config = DaysCounterConfig.load();

		KeyMapping.Category category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MOD_ID, "main"));
		keyPosition = KeyMappingHelper.registerKeyMapping(
			new KeyMapping("key.dayscounter.cycle_position", InputConstants.KEY_J, category));
		keyFont = KeyMappingHelper.registerKeyMapping(
			new KeyMapping("key.dayscounter.cycle_font", InputConstants.KEY_K, category));
		keyToggle = KeyMappingHelper.registerKeyMapping(
			new KeyMapping("key.dayscounter.toggle", InputConstants.KEY_H, category));

		ClientTickEvents.END_CLIENT_TICK.register(DaysCounterClient::onTick);

		HudElementRegistry.attachElementAfter(
			VanillaHudElements.MISC_OVERLAYS,
			Identifier.fromNamespaceAndPath(MOD_ID, "days_counter"),
			DaysCounterClient::render);
	}

	private static void onTick(Minecraft mc) {
		while (keyPosition.consumeClick()) {
			config.position = config.position.next();
			config.save();
			showFeedback("Posición: " + config.position.label);
		}
		while (keyFont.consumeClick()) {
			config.font = config.font.next();
			config.save();
			showFeedback("Tipografía: " + config.font.label);
		}
		while (keyToggle.consumeClick()) {
			config.enabled = !config.enabled;
			config.save();
			showFeedback("Contador de días: " + (config.enabled ? "visible" : "oculto"));
		}
	}

	private static void showFeedback(String text) {
		feedback = text;
		feedbackUntil = System.currentTimeMillis() + 2000L;
	}

	private static void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null) {
			return;
		}

		Font font = mc.font;
		int screenWidth = graphics.guiWidth();
		float bottom = config.marginY;

		if (config.enabled) {
			long days = Math.floorDiv(mc.level.getOverworldClockTime(), TICKS_PER_DAY);
			if (config.startAtOne) {
				days += 1;
			}

			String text;
			try {
				text = String.format(config.format, days);
			} catch (Exception e) {
				text = "Día " + days;
			}

			Style style = config.font.apply(Style.EMPTY).withBold(config.bold);
			Component component = Component.literal(text).withStyle(style);

			float scale = config.scale;
			float scaledWidth = font.width(component) * scale;

			float x = switch (config.position) {
				case LEFT -> config.marginX;
				case CENTER -> (screenWidth - scaledWidth) / 2f;
				case RIGHT -> screenWidth - scaledWidth - config.marginX;
			};
			float y = config.marginY;

			graphics.pose().pushMatrix();
			graphics.pose().translate(x, y);
			graphics.pose().scale(scale, scale);
			graphics.text(font, component, 0, 0, config.colorRgb(), config.shadow);
			graphics.pose().popMatrix();

			bottom = y + font.lineHeight * scale;
		}

		if (feedback != null && System.currentTimeMillis() < feedbackUntil) {
			Component fb = Component.literal(feedback);
			int fx = (screenWidth - font.width(fb)) / 2;
			int fy = (int) bottom + 4;
			graphics.text(font, fb, fx, fy, 0xFFFFFFAA, true);
		}
	}
}
