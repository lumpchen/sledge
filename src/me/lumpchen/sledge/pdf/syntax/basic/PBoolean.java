package me.lumpchen.sledge.pdf.syntax.basic;


public class PBoolean extends PObject {

	public static final byte[] TAG_TRUE = {'t', 'r', 'u', 'e'};
	public static final byte[] TAG_FALSE = {'f', 'a', 'l', 's', 'e'};
	
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	public static final PBoolean True = new PBoolean(true);
	public static final PBoolean False = new PBoolean(false);

	private boolean value;

	public PBoolean() {
		this.type = Type.Boolean;
	}

	public PBoolean(boolean b) {
		this.type = Type.Boolean;
		this.value = b;
	}

	public PBoolean(String s) {
		this.type = Type.Boolean;
		if (TRUE.equalsIgnoreCase(s)) {
			this.value = true;
		} else {
			this.value = false;
		}
	}

	public String toString() {
		if (value) {
			return "true";
		} else {
			return "false";
		}
	}
	
	public boolean getValue() {
		return this.value;
	}
}
