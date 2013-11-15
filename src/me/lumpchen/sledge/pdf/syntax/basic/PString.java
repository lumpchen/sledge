package me.lumpchen.sledge.pdf.syntax.basic;

import me.lumpchen.sledge.pdf.syntax.PObject;

public abstract class PString extends PObject {

	public static final char LF = '\n';
	public static final char CR = '\r';
	public static final char BACKSLASH = '\\'; // Backslash
	public static final char TAB = '\t';
	public static final char BS = '\b'; // Backspace
	public static final char FF = '\f'; // Form feed
	public static final char LEFT_PARENTHESIS = '('; // Left parenthesis
	public static final char RIGHT_PARENTHESIS = ')'; // Right parenthesis
	public static final char SPACE = ' ';
	
	protected char[] charSequence;

	protected PString() {
	}

	public PString create(byte[] data) {
		if (data == null) {
			return null;
		}
		if (data[0] == '(' && data[data.length - 1] == ')') {
			return new PLiteralString(data);
		} else if (data[0] == '<' && data[data.length - 1] == '>') {
			return new PHexString(data);
		}
		return null;
	}

	public String toString() {
		if (charSequence != null) {
			return new String(charSequence);
		}

		return "";
	}
}
