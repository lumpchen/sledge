package me.lumpchen.sledge.pdf.reader;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Tokenizer {

	private RandomByteReader reader;
	private int pos = 0;
	private byte[] buf = new byte[512];
	
	public Tokenizer(RandomByteReader reader) {
		this.reader = reader;
		try {
			this.buf = this.reader.read(this.buf.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Tokenizer(byte[] bytes) {
		this.reader = new ByteBufferRandomByteReader(bytes);
		try {
			this.buf = this.reader.read(this.buf.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	private int read() throws IOException {
		if (this.pos < this.buf.length) {
			return this.buf[pos++];
		}
		this.buf = this.reader.read(this.buf.length);
		if (this.buf.length == 0) {
			return -1;
		}
		this.pos = 0;
		return this.buf[pos++] & 0xFF;
	}
	
	private int peek() throws IOException {
		if (this.pos < this.buf.length) {
			return this.buf[pos];
		}
		this.buf = this.reader.read(this.buf.length);
		if (this.buf.length == 0) {
			return -1;
		}
		this.pos = 0;
		return this.buf[pos] & 0xFF;
	}
	
	public Token nextToken() throws IOException {
		Token token = new Token();
		int last = 0;
		while (true) {
			int b = this.peek();
			if (b == -1) {
				break;
			}
			
			if ('(' == b || ')' == b || '[' == b || ']' == b || '/' == b || '%' == b) {
				if (token.size() > 0) {
					break;
				}
				token.add(this.read());
				
				if ('\\' == last) {
					continue;
				} else {
					break;
				}
			} else if ('<' == b) {
				if (token.size() > 0) {
					break;
				}
				token.add(this.read());
				
				int next = this.peek();
				if ('<' == next) {
					token.add(this.read());
				}
				break;
			} else if ('>' == b) {
				if (token.size() > 0) {
					break;
				}
				token.add(this.read());
				
				int next = this.peek();
				if ('>' == next) {
					token.add(this.read());
				}
				break;
			} else if (' ' == b || '\r' == b || '\n' == b) {
				if (token.size() > 0) {
					break;
				} else {
					this.read();
					continue;
				}
			}
			
			token.add(this.read());
			last = b;
		}
		
		return token;
	}
	
	public Token nextToken(byte end) throws IOException {
		Token token = new Token();
		int last = 0;
		while (true) {
			int b = this.peek();
			if (b == -1) {
				break;
			}
			if (b == end && '\\' != last) {
				break;
			}
			token.add(this.read());
			last = b;
		}
		return token;
	}
	
	public byte[] readLine() throws IOException {
		ByteBuffer buf = ByteBuffer.allocate(256);
		int last = 0;
		while (true) {
			int b = this.read();
			if (b == -1) {
				break;
			}
			if (b == '\n' && '\\' != last) {
				break;
			} else if (b == '\r' && '\\' != last) {
				if (this.peek() == '\n') {
					this.read();
				}
				break;
			}
			last = b;
			buf.put((byte) (b & 0xFF));
		}
		
		return buf.array();
	}
}
