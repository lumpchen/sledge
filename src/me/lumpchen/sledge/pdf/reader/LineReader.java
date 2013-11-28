package me.lumpchen.sledge.pdf.reader;

import java.io.IOException;
import java.nio.ByteBuffer;

public class LineReader {

	public static final byte LF = '\n'; 
	private ByteBuffer buf;
	private SegmentedFileReader segmentedFileReader;
	private int rollbackBytes = 0;
	
	public LineReader(SegmentedFileReader reader) {
		this.segmentedFileReader = reader;
	}
	
	public LineReader(LineData data) {
		byte[] bytes = data.getBytes();
		this.buf = ByteBuffer.wrap(bytes);
		if (bytes[bytes.length - 1] != '\n') {
			this.buf.put(bytes.length - 1, LF);
			this.buf.position(0);
		}
	}
	
	public LineData readLine() {
		if (this.buf == null) {
			if (this.segmentedFileReader != null) {
				this.readSegment();
			}
		}
		byte[] data = readNextLine();
		if (data == null) {
			if (this.segmentedFileReader != null) {
				this.readSegment();
			}
			data = this.readNextLine();
		}
		
		if (data == null) {
			return null;
		}
		return new LineData(data);
	}
	
	private void readSegment() {
		long pos = this.segmentedFileReader.position() - this.rollbackBytes;
		this.segmentedFileReader.setPosition(pos);
		try {
			this.buf = this.segmentedFileReader.readSegment();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private byte[] readNextLine() {
		if (this.buf == null || this.buf.remaining() == 0) {
			return null;
		}
		int pos = this.buf.position();
		int run = 0;
		int remain = this.buf.remaining();
		int eol = 0;
		while (true) {
			if (run + eol == remain) {
				// not a valid line, need back position to \n
				this.rollbackBytes = run;
				run = 0;
				break;
			}
			byte b = buf.get(pos + run + eol);
			if (b == '\r') {
				eol++;
				continue;
			}
			if (b == '\n') {
				eol++;
				break;
			} else {
				if (eol > 0) {
					break;
				}
			}
			run++;
		}

		if (run == 0) {
			return null;
		}
		
		byte[] data = new byte[run];
		buf.get(data);
		
		buf.position(pos + run + eol);
		
		return data;
	}
}
