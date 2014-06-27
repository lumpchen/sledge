package com.docscience.pathfinder.font.driver.ps;

/**
 * @author wxin
 *
 */
public final class PSInteger extends PSNumber {
	
	private static PSInteger[] cache;
	static {
		cache = new PSInteger[256];
		for (int i=0; i<256; i++) {
			cache[i] = new PSInteger(i - 128);
		}
	}
	
	private int value;

	private PSInteger(int value) {
		this.value = value;
	}

	public static PSInteger valueOf(int value) {
		if (value >= -128 && value <= 127) {
			return cache[value + 128];
		}
		else {
			return new PSInteger(value);
		}
	}
	
	@Override
	public int getType() {
		return TYPE_INTEGER;
	}

	@Override
	public int intValue() {
		return value;
	}
	
	@Override
	public double doubleValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return Integer.toString(value);
	}
	
}
