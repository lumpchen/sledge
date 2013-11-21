package me.lumpchen.sledge.pdf.reader;

import java.nio.ByteBuffer;

public class LineReader {

	private ByteBuffer buf;
	private boolean reverse = false;
	
	public LineReader(ByteBuffer buf) {
		this(buf, false);
	}
	
	public LineReader(ByteBuffer buf, boolean reverse) {
		this.buf = buf;
		this.reverse = reverse;
		if (reverse) {
			buf.position(buf.position() + buf.remaining() - 1);
		}
	}
	
	public LineData getLine() {
		byte[] data = null;
		if (this.reverse) {
			data = this.readPrevLine();
		} else {
			data = readNextLine();
		}
		if (data == null) {
			return null;
		}
		return new LineData(data);
	}

	private byte[] readNextLine() {
		int pos = this.buf.position();
		int run = 0;
		int remain = this.buf.remaining();
		while (true) {
			if (run == remain) {
				break;
			}
			byte b = buf.get(pos + run);
			if (b == '\n') {
				run++;
				break;
			}
			run++;
		}

		if (run == 0) {
			return null;
		}
		
		byte[] data = new byte[run];
		buf.get(data);
		
		buf.position(pos + run);
		
		return data;
	}
	
	private byte[] readPrevLine() {
		int pos = this.buf.position();
		while (true) {
			byte b = this.buf.get(pos);
			if (b != '\r' && b != '\n') {
				break;
			}
			pos--;
		}
		this.buf.position(pos);
		
		int run = 0;
		while (pos >= 0) {
			byte b = this.buf.get(pos);
			if (b == '\n') {
				break;
			}
			run++;
			pos--;
		}
		
		this.buf.position(pos + 1);
		byte[] line = new byte[run];
		this.buf.get(line);
		
		this.buf.position(pos);
		
		return line;
	}
}
