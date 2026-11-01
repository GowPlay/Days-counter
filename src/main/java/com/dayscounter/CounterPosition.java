package com.dayscounter;

public enum CounterPosition {
	LEFT("Izquierda"),
	CENTER("Centro"),
	RIGHT("Derecha");

	public final String label;

	CounterPosition(String label) {
		this.label = label;
	}

	public CounterPosition next() {
		CounterPosition[] v = values();
		return v[(ordinal() + 1) % v.length];
	}
}
