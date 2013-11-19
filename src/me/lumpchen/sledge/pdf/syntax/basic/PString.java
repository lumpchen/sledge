package me.lumpchen.sledge.pdf.syntax.basic;

import java.util.ArrayList;
import java.util.List;

import me.lumpchen.sledge.pdf.reader.ObjectReader;
import me.lumpchen.sledge.pdf.syntax.PObject;

public abstract class PString extends PObject {

	public static final byte LF = '\n';
	public static final byte CR = '\r';
	public static final byte BACKSLASH = '\\'; // Backslash
	public static final byte TAB = '\t';
	public static final byte BS = '\b'; // Backspace
	public static final byte FF = '\f'; // Form feed
	public static final byte LEFT_PARENTHESIS = '('; // Left parenthesis
	public static final byte RIGHT_PARENTHESIS = ')'; // Right parenthesis
	public static final byte SPACE = ' ';
	
	protected char[] charSequence;

	public PString() {
	}

	public void read(ObjectReader reader) {
		List<Byte> byteArr = new ArrayList<>();
		byte prev = 0;
		
		while (true) {
			byte b = reader.readByte();
			if (b == RIGHT_PARENTHESIS) {
				if (prev != BACKSLASH) {
					break;
				}
			}
			byteArr.add(b);
			prev = b;
		}

		byte[] bytes = new byte[byteArr.size()];
		for (int i = 0, n = byteArr.size(); i < n; i++) {
			bytes[i] = byteArr.get(i);
		}
		
		encode(bytes);
	}
	
	abstract protected void encode(byte[] bytes);
	
	public String toString() {
		if (charSequence != null) {
			return new String(charSequence);
		}

		return "";
	}
}
