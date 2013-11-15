package me.lumpchen.sledge.pdf.syntax.basic;

import me.lumpchen.sledge.pdf.syntax.PObject;

public class PBoolean extends PObject {
	
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	private boolean value;
	
	public PBoolean(boolean b) {
		this.value = b;
	}
	
	public PBoolean(String s) {
		if (TRUE.equalsIgnoreCase(s)) {
			this.value = true;
		} else {
			this.value = false;
		}
	}
	
	public boolean getValue() {
		return this.value;
	}
}
