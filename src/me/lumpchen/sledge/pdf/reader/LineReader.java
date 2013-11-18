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
	
	public Line getNextLine() {
		byte[] data = readLine();
		if (data != null) {
			return new Line(data);
		}
		return null;
	}

	private byte[] readLine() {
		if (reverse) {
			int run = 0;
			int end = buf.position();
			while (true) {
				byte c = buf.get(end);
				buf.position(end);
				if ('\r' == c || '\n' == c) {
					if (run != 0) {
						buf.position(end + 1);
						break;
					}
				} else {
					run++;
				}
				end--;
			}
			if (run > 0) {
				byte[] dst = new byte[run];
				buf.get(dst, 0, run);
				buf.position(buf.position() - run - 1);
				return dst;
			}
		} else {
			int run = 0;
			int start = buf.position();
			while (true) {
				byte c = buf.get();
				if ('\r' == c || '\n' == c) {
					break;
				} else {
					++run;
				}
			}
			if (run > 0) {
				byte[] dst = new byte[run];
				buf.get(dst, start, run);
				return dst;
			}
		}
		return null;
	}
}
