package me.lumpchen.sledge.pdf.syntax.lang;


public class PNull extends PObject {

	public static final byte[] NULL = { 'n', 'u', 'l', 'l' };

	public PNull() {
		super.classType = ClassType.Null;
	}

	public String toString() {
		return "null";
	}

}
