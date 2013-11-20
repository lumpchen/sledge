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
		return new LineData(data);
	}

	private byte[] readNextLine() {
		boolean cr = false;
		int run = this.buf.position();
		while (true) {
			byte b = buf.get(run);
			if (b == '\n') {
				if (cr) {
					run--;
				}
				break;
			}
			if (b == '\r') {
				cr = true;
			}
			run++;
		}
		
		byte[] name = new byte[run - this.buf.position()];
		buf.get(name);
		
		int newPos = run + 1 + (cr ? 1 : 0);
		buf.position(newPos);
		return name;
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
		while (true) {
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
