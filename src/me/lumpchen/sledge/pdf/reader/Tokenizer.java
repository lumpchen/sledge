package me.lumpchen.sledge.pdf.reader;

import java.io.IOException;

public class Tokenizer {

	private RandomByteReader reader;
	private int pos = 0;
	private int read = 0;
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
	
	public int hasRead() {
		return this.read;
	}
	
	public byte[] readBytes(int size) throws IOException {
		return this.read(size);
	}
	
	public void readEOL() throws IOException {
		byte next = (byte) (this.peek() & 0xFF);
		while (next == '\r' || next == '\n' || isSpace(next)) {
			this.read();
			next = (byte) (this.peek() & 0xFF);
		}
	}
	
	private int read() throws IOException {
		if (this.pos < this.buf.length) {
			this.read++;
			return this.buf[pos++] & 0xFF;
		}
		this.buf = this.reader.read(this.buf.length);
		if (this.buf.length == 0) {
			return -1;
		}
		this.pos = 0;
		this.read++;
		return this.buf[pos++] & 0xFF;
	}
	
	private byte[] read(int size) throws IOException {
		if (this.pos + size < this.buf.length) {
			byte[] dst = new byte[size];
			System.arraycopy(this.buf, this.pos, dst, 0, size);
			this.pos += size;
			this.read += size;
			return dst;
		}
		
		byte[] dst = new byte[size];
		int remain = this.buf.length - this.pos;
		System.arraycopy(this.buf, this.pos, dst, 0, remain);
		
		int unread = size - remain;
		this.buf = this.reader.read(unread);
		System.arraycopy(this.buf, 0, dst, remain, unread);
		
		this.pos = this.buf.length;
		this.read += size;
		return dst;
	}
	
	private int peek() throws IOException {
		if (this.pos < this.buf.length) {
			return this.buf[pos] & 0xFF;
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
					this.readEOL();
					break;
				} else {
					this.readEOL();
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
		int last = -1;
		while (true) {
			int b = this.peek();
			if (b == -1) {
				break;
			}
			if (b == end && last != '\\') {
				break;
			}
			token.add(this.read());
			last = b & 0xFF;
		}
		return token;
	}
	
	public byte[] readLine() throws IOException {
		Token token = new Token();
		int last = -1;
		while (true) {
			int b = this.read();
			if (b == -1) {
				break;
			}
			if (b == '\n') {
				if (last == '\\' || last == -1) {
					continue;
				} else {
					break;
				}
			} else if (b == '\r' && last != '\\' && last != -1) {
				if (last == '\\' || last == -1) {
					continue;
				} else {
					if (this.peek() == '\n') {
						this.read();
					}
					break;
				}
			}
			last = b & 0xFF;
			token.add((byte) (b & 0xFF));
		}
		
		return token.getBytes();
	}
	
	public static boolean isSpace(byte b) {
		return Character.isWhitespace(b);
	}
}
