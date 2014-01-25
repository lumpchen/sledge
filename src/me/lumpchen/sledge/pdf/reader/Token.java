package me.lumpchen.sledge.pdf.reader;

import java.nio.ByteBuffer;

public class Token {

	private ByteBuffer buf;
	private int size = 0;

	public Token() {
		this.buf = ByteBuffer.allocate(32);
	}

	public Token(byte[] bytes) {
		this.size = bytes.length;
		this.buf = ByteBuffer.wrap(bytes);
	}

	public void add(byte b) {
		this.buf.put(b);
		size++;
	}
	
	public void add(int b) {
		this.buf.put((byte) (b & 0xFF));
		size++;
	}

	public String toString() {
		if (this.buf != null) {
			return new String(this.buf.array());
		}
		return "";
	}

	public byte[] getBytes() {
		this.buf.rewind();
		byte[] filled = new byte[size];
		this.buf.get(filled);
		return filled;
	}

	public int size() {
		return this.size;
	}

	public boolean match(byte... tag) {
		byte[] bytes = this.getBytes();
		if (bytes == null || bytes.length != tag.length) {
			return false;
		}
		for (int i = 0, n = tag.length; i < n; i++) {
			if (bytes[i] != tag[i]) {
				return false;
			}
		}
		return true;
	}

	public boolean isNumber() {
		byte[] bytes = this.getBytes();
		if (bytes == null || bytes.length == 0) {
			return false;
		}
		return isNumber(bytes);
	}

	public static boolean isNumber(byte... bytes) {
		for (byte b : bytes) {
			if ('.' == b || '-' == b || '+' == b) {
				continue;
			}
			if (!Character.isDigit(b)) {
				return false;
			}
		}
		return true;
	}
	
	public Integer readAsInt() {
		byte[] source = this.getBytes();
		int len = source.length;
		int value = 0;
		for (int i = 0, n = source.length; i < n; i++) {
			int c = Character.digit(source[i], 10);
			value += c * (int) (Math.pow(10, len - i - 1) + 0.5);
		}
		return value;
	}

	public long readAsLong() {
		byte[] source = this.getBytes();
		int len = source.length;
		long value = 0;
		for (int i = 0, n = source.length; i < n; i++) {
			int c = Character.digit(source[i], 10);
			value += c * (int) (Math.pow(10, len - i - 1) + 0.5);
		}
		return value;
	}

	public String readAsString() {
		byte[] source = this.getBytes();
		return new String(source);
	}

	public boolean startsWith(byte[] prefix) {
		byte[] source = this.getBytes();
		if (source.length < prefix.length) {
			return false;
		}
		for (int i = 0, n = prefix.length; i < n; i++) {
			if (source[i] != prefix[i]) {
				return false;
			}
		}
		return true;
	}

	public boolean startsWith(String prefix) {
		byte[] source = this.getBytes();
		if (source.length < prefix.length()) {
			return false;
		}
		return this.readAsString().startsWith(prefix);
	}

	public boolean contain(byte[] dst) {
		byte[] source = this.getBytes();
		if (source.length < dst.length) {
			return false;
		}
		byte first = dst[0];
		int i = 0;
		while (true) {
			if (i >= source.length) {
				break;
			}
			if (first == source[i]) {
				int j = 0;
				while (source[++i] == dst[++j]) {
					if (j == dst.length - 1) {
						return true;
					}
				}
			}
			i++;
		}

		return false;
	}
	
	public static boolean isSpace(byte b) {
		return Character.isWhitespace(b);
	}
}
