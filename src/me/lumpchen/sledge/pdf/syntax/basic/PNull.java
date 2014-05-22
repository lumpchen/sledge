package me.lumpchen.sledge.pdf.syntax.basic;


public class PNull extends PObject {

	public static final byte[] NULL = { 'n', 'u', 'l', 'l' };

	public PNull() {
		super.type = TYPE.Null;
	}

	public String toString() {
		return "null";
	}

}
