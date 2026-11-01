package com.dayscounter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Configuración guardada en config/dayscounter.json (se puede editar a mano). */
public class DaysCounterConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("dayscounter.json");

	public boolean enabled = true;
	public CounterPosition position = CounterPosition.CENTER;
	public CounterFont font = CounterFont.DEFAULT;
	/** Texto mostrado. %d se reemplaza por el número de día. */
	public String format = "Día %d";
	/** Si es true, el primer día del mundo se muestra como "Día 1"; si es false, como "Día 0". */
	public boolean startAtOne = true;
	public boolean shadow = true;
	public boolean bold = false;
	/** Color en hexadecimal RGB, ej. FFFFFF. */
	public String color = "FFFFFF";
	public float scale = 1.0f;
	/** Margen en píxeles respecto a los bordes de la pantalla. */
	public int marginX = 6;
	public int marginY = 6;

	public int colorRgb() {
		try {
			return 0xFF000000 | Integer.parseInt(color.replace("#", "").trim(), 16);
		} catch (NumberFormatException e) {
			return 0xFFFFFFFF;
		}
	}

	public static DaysCounterConfig load() {
		try {
			if (Files.exists(FILE)) {
				DaysCounterConfig cfg = GSON.fromJson(Files.readString(FILE), DaysCounterConfig.class);
				if (cfg != null) {
					if (cfg.position == null) cfg.position = CounterPosition.CENTER;
					if (cfg.font == null) cfg.font = CounterFont.DEFAULT;
					if (cfg.format == null) cfg.format = "Día %d";
					if (cfg.scale <= 0f) cfg.scale = 1.0f;
					return cfg;
				}
			}
		} catch (Exception e) {
			DaysCounterClient.LOGGER.warn("No se pudo leer la configuración, usando valores por defecto", e);
		}
		DaysCounterConfig cfg = new DaysCounterConfig();
		cfg.save();
		return cfg;
	}

	public void save() {
		try {
			Files.createDirectories(FILE.getParent());
			Files.writeString(FILE, GSON.toJson(this));
		} catch (IOException e) {
			DaysCounterClient.LOGGER.warn("No se pudo guardar la configuración", e);
		}
	}
}
