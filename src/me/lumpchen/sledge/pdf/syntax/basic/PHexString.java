package me.lumpchen.sledge.pdf.syntax.basic;


public class PHexString extends PString {

	private char[] charSequence;

	public PHexString(byte[] data) {
		encode(data);
	}

	private void encode(byte[] data) {
		int size = data.length;
		char[] cs = new char[size];
		for (int i = 0; i < size; i++) {
			byte b = data[i];
			if (b == CR || b == LF || b == TAB || b == FF || b == SPACE) {
				continue;
			}
		}
	}
}
