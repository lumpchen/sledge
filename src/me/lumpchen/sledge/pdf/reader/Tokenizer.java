package me.lumpchen.sledge.pdf.reader;

import java.io.IOException;

public class Tokenizer {

	private RandomByteReader reader;
	private int pos = 0;
	private byte[] buf = new byte[512];
	
	public Tokenizer(RandomByteReader reader) {
		this.reader = reader;
	}
	
	public Tokenizer(byte[] bytes) {
		this.reader = new ByteBufferRandomByteReader(bytes);
	}
	
	public long remaining() {
		return this.reader.remain();
	}
	
	public long position() {
		return this.reader.position();
	}
	
	public byte[] readBytes(int size) throws IOException {
		this.pos += size;
		return this.reader.read(size);
	}
	
	public void readEOL() {
	}
	
	private byte read() throws IOException {
		if (this.pos < this.buf.length) {
			return this.buf[pos++];
		}
		this.buf = this.reader.read(this.buf.length);
		this.pos = 0;
		return this.buf[pos++];
	}
	
	private byte peek() throws IOException {
		if (this.pos < this.buf.length) {
			return this.buf[pos];
		}
		this.buf = this.reader.read(this.buf.length);
		this.pos = 0;
		return this.buf[pos];
	}
	
	public Token nextToken() throws IOException {
		Token token = new Token();
		byte last = 0;
		while (true) {
			byte b = this.read();
			if ('(' == b || ')' == b || '[' == b || ']' == b || '/' == b || '%' == b) {
				token.add(b);
				if ('\\' == last) {
					continue;
				} else {
					break;
				}
			} else if ('<' == b) {
				byte next = this.peek();
				if ('<' == next) {
					token.add(this.read());
				}
				break;
			} else if ('>' == b) {
				byte next = this.peek();
				if ('>' == next) {
					token.add(this.read());
				}
				break;
			} else if (' ' == b || '\r' == b || '\n' == b) {
				if (token.size() > 0) {
					break;
				}
			} else {
				token.add(b);				
			}
			
			last = b;
		}
		
		return token;
	}
	
	public Token nextToken(byte end) throws IOException {
		Token token = new Token();
		byte last = 0;
		while (true) {
			byte b = this.peek();
			if (b == end && '\\' != last) {
				break;
			}
			token.add(this.read());
		}
		return token;
	}
	
	public Token readLine() throws IOException {
		Token token = new Token();
		while (true) {
			byte b = this.read();
			if (b == '\n') {
				break;
			} else if (b == '\r') {
				if (this.peek() == '\n') {
					this.read();
				}
				break;
			}
			
			token.add(b);
		}
		
		return token;
	}
}
