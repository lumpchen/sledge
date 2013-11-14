package me.lumpchen.sledge.pdf.reader;

import me.lumpchen.sledge.pdf.syntax.basic.PDFLong;

public class ObjectReader {

	public static PDFLong readIAsLong(byte[] bytes) {
		int len = bytes.length;
		long value = 0;
		for (int i = 0, n = bytes.length; i < n; i++) {
			int c = Character.digit(bytes[i], 10);
			value += c * (int) (Math.pow(10, len - i - 1) + 0.5);
		}
		return new PDFLong(value);
	}
	
}
