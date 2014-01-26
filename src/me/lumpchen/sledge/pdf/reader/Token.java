package me.lumpchen.sledge.pdf.reader;

import java.util.ArrayList;
import java.util.List;

public class Token {

	private List<Byte> array;

	public Token() {
		this.array = new ArrayList<Byte>();
	}

	public Token(byte[] bytes) {
		this.array = new ArrayList<Byte>();
		for (byte b : bytes) {
			this.array.add(b);
		}
	}

	public void add(byte b) {
		this.array.add(b);
	}
	
	public void add(int b) {
		this.add((byte) (b & 0xFF));
	}

	public String toString() {
		return new String(this.getBytes());
	}

	public byte[] getBytes() {
		byte[] data = new byte[this.array.size()];
		for (int i = 0; i < this.array.size(); i++) {
			data[i] = this.array.get(i);
		}
		return data;
	}

	public int size() {
		return this.array.size();
	}

	public boolean match(byte... tag) {
		int size = this.array.size();
		if (size != tag.length) {
			return false;
		}
		for (int i = 0, n = tag.length; i < n; i++) {
			if (this.array.get(i) != tag[i]) {
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
		int size = this.array.size();
		int value = 0;
		for (int i = 0; i < size; i++) {
			int c = Character.digit(this.array.get(i), 10);
			value += c * (int) (Math.pow(10, size - i - 1) + 0.5);
		}
		return value;
	}

	public long readAsLong() {
		int size = this.array.size();
		long value = 0;
		for (int i = 0; i < size; i++) {
			int c = Character.digit(this.array.get(i), 10);
			value += c * (int) (Math.pow(10, size - i - 1) + 0.5);
		}
		return value;
	}

	public String readAsString() {
		byte[] source = this.getBytes();
		return new String(source);
	}

	public boolean startsWith(byte[] prefix) {
		int size = this.array.size();
		if (size < prefix.length) {
			return false;
		}
		for (int i = 0, n = prefix.length; i < n; i++) {
			if (this.array.get(i) != prefix[i]) {
				return false;
			}
		}
		return true;
	}

	public boolean startsWith(String prefix) {
		int size = this.array.size();
		if (size < prefix.length()) {
			return false;
		}
		return this.readAsString().startsWith(prefix);
	}

	public boolean contain(byte[] dst) {
		int size = this.array.size();
		if (size < dst.length) {
			return false;
		}
		byte first = dst[0];
		int i = 0;
		while (true) {
			if (i >= size) {
				break;
			}
			if (first == this.array.get(i)) {
				int j = 0;
				while (this.array.get(++i) == dst[++j]) {
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
