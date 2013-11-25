package me.lumpchen.sledge.pdf.reader;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class BytesReader {

	private ByteBuffer buf;
	
	public BytesReader(byte[] bytes) {
		this.buf = ByteBuffer.wrap(bytes);
	}
	
	public int remaining() {
		return this.buf.remaining();
	}
	
	public int position() {
		return this.buf.position();
	}
	
	public byte getByte(int position) {
		return this.buf.get(position);
	}
	
	public byte readByte() {
		return this.buf.get();
	}
	
	public byte[] readToSpace() {
		int i = 0;
		while (true) {
			if (isSpace(buf.get(this.buf.position() + i))) {
				break;
			}
			i++;
		}
		byte[] bytes = new byte[i];
		buf.get(bytes);
		
		this.skipSpace();
		return bytes;
	}

	public byte[] readToStop() {
		int i = 0;
		while (true) {
			if (isKeyword(buf.get(this.buf.position() + i))) {
				break;
			}
			i++;
		}
		byte[] bytes = new byte[i];
		buf.get(bytes);
		
		this.skipSpace();
		return bytes;
	}
	
	private boolean isKeyword(byte b) {
		if (b == '/' 
				|| b == '[' || b == ']' 
				|| b == ' ' 
				|| b == '<' || b == '>'
				|| b == '(' || b == ')') {
			return true;
		}
		return false;
	}
	
	public byte[] readToFlag(byte flag) {
		int pos = this.buf.position();
		int run = 0;
		byte last = 0;
		while (true) {
			byte next = this.buf.get(pos + run);
			if (next == flag) {
				if (!(last == '\\' && isKeyword(next))) {
					break;
				}
			}
			
			last = next;
			run++;
		}
		
		byte[] bytes = new byte[run];
		this.buf.get(bytes);
		
		return bytes;
	}
	
	public byte[] readBytes(int size) {
		byte[] bytes = new byte[size];
		this.buf.get(bytes);
		return bytes;
	}

	public int readInt() {
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
		
		int value = 0;
		for (int i = 0, n = num.size(); i < n; i++) {
			value += num.get(i) * (int) (Math.pow(10, n - i - 1) + 0.5);
		}
		return value;
	}
	
	public long readLong() {
		List<Integer> num = new ArrayList<Integer>();
		while (this.remaining() > 0) {
			byte b = this.buf.get();
			if (!Character.isDigit(b)) {
				if (!isSpace(b)) {
					this.buf.position(this.buf.position() - 1);					
				}
				break;
			}
			num.add(Character.digit(b, 10));
		}
		
		long value = 0;
		for (int i = 0, n = num.size(); i < n; i++) {
			value += num.get(i) * (int) (Math.pow(10, n - i - 1) + 0.5);
		}
		return value;
	}
	
	public static boolean isSpace(byte b) {
		return Character.isWhitespace(b);
	}

	public static boolean isNumber(byte... bytes) {
		for (byte b : bytes) {
			if (!Character.isDigit(b)) {
				return false;
			}
		}
		return true;
	}

	public byte[] peekToSpace(int offset) {
		int pos = this.buf.position();
		int remain = this.buf.remaining();
		
		int run = 0;
		while (true) {
			if (remain - offset == run) {
				break;
			}
			byte next = this.buf.get(pos + offset + run);
			if (isSpace(next)) {
				break;
			}
			run++;
		}

		this.buf.position(pos + offset);
		byte[] bytes = new byte[run];
		this.buf.get(bytes, 0, run);

		this.buf.position(pos); // back to original position
		return bytes;
	}
	
	public void skipSpace() {
		while (true) {
			byte b = this.buf.get();
			if (!isSpace(b)) {
				break;
			}
		}
		this.buf.position(this.buf.position() - 1);
	}
}
