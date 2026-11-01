package com.dayscounter;

import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;

public enum CounterFont {
	DEFAULT("Normal de Minecraft", null, null),
	UNIFORM("Uniforme", "minecraft", "uniform"),
	ENCHANTING("Mesa de encantamientos", "minecraft", "alt"),
	ILLAGER("Illager (rúnica)", "minecraft", "illageralt"),
	PIXEL("Pixel retro", "dayscounter", "pixel"),
	CREEPY("Terror", "dayscounter", "creepy"),
	COMIC("Cómic", "dayscounter", "bangers"),
	CUSTOM("Personalizada (resource pack)", "dayscounter", "custom");

	public final String label;
	private final String namespace;
	private final String path;

	CounterFont(String label, String namespace, String path) {
		this.label = label;
		this.namespace = namespace;
		this.path = path;
	}

	public Style apply(Style base) {
		if (namespace == null) {
			return base;
		}
		return base.withFont(new FontDescription.Resource(Identifier.fromNamespaceAndPath(namespace, path)));
	}

	public CounterFont next() {
		CounterFont[] v = values();
		return v[(ordinal() + 1) % v.length];
	}
}
