package me.lumpchen.sledge.pdf.reader;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Token {

	private ByteBuffer buf;

	public Token() {
		this.buf = ByteBuffer.allocate(32);
	}

	public Token(byte[] bytes) {
		this.buf = ByteBuffer.wrap(bytes);
	}

	public void add(byte b) {
		this.buf.put(b);
	}

	public String toString() {
		if (this.buf != null) {
			return new String(this.buf.array());
		}
		return "";
	}

	public byte[] getBytes() {
		return this.buf.array();
	}

	public int size() {
		if (this.buf != null) {
			return this.buf.array().length;
		}
		return 0;
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
	
	public Integer readInt() {
		List<Integer> num = new ArrayList<Integer>();
		while (this.buf.remaining() > 0) {
			byte b = this.buf.get();
			if (!Character.isDigit(b)) {
				if (!isSpace(b)) {
					this.buf.position(this.buf.position() - 1);					
				}
				break;
			}
			num.add(Character.digit(b, 10));
		}
		
		if (0 == num.size()) {
			return null;	
		}
		
		int value = 0;
		for (int i = 0, n = num.size(); i < n; i++) {
			value += num.get(i) * (int) (Math.pow(10, n - i - 1) + 0.5);
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
