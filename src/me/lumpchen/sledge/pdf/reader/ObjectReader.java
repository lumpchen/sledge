package me.lumpchen.sledge.pdf.reader;

import java.nio.ByteBuffer;

import me.lumpchen.sledge.pdf.syntax.PObject;
import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PLong;

public class ObjectReader {
	
	public PObject read(ByteBuffer buf, PObject parent) {
		int originalPos = buf.position();
		
		PObject obj = null;
		byte first = buf.get(0);
		if (first == '[') {
			obj = new PArray();
			obj.read(buf, parent);
		}
		
		if (null == obj) {
			buf.position(originalPos);
		}
		return obj;
	}

	public static PLong readIAsLong(byte[] bytes) {
		int len = bytes.length;
		long value = 0;
		for (int i = 0, n = bytes.length; i < n; i++) {
			int c = Character.digit(bytes[i], 10);
			value += c * (int) (Math.pow(10, len - i - 1) + 0.5);
		}
		return new PLong(value);
	}
	
}
