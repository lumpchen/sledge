package me.lumpchen.sledge.pdf.reader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class SegmentedFileReader {

	public static final int SEGMETN_SIZE = 4096;
	private FileChannel channel;
	private long fileSize;
	private long position;
	private int segmentSize;
	
	public SegmentedFileReader(FileChannel channel) throws IOException {
		this.channel = channel;
		this.fileSize = this.channel.size();
	}
	
	public long position() {
		return this.position;
	}
	
	public void setPosition(long offset) {
		if (offset > this.fileSize) {
			throw new ReadException("");
		}
		this.setPosition(offset, false);
	}

	public void setPosition(long offset, boolean fromTrailer) {
		if (fromTrailer) {
			this.position = this.fileSize - offset;
		} else {
			this.position = offset;
		}
	}

	public long remain() {
		return this.fileSize - this.position;
	}
	
	public void setSegmentSize(int size) {
		this.segmentSize = size;
	}
	
	public ByteBuffer readSegment() throws IOException {
		return this.readSegment(this.segmentSize);
	}
	
	public ByteBuffer readSegment(int size) throws IOException {
		if (this.position >= this.fileSize) {
			return null;
		}
		if (this.position + size > this.fileSize) {
			size = (int) (this.fileSize - this.position);
		}
		ByteBuffer buf = this.channel.map(FileChannel.MapMode.READ_ONLY, position, size);
		this.position += size;
		return buf;
	}
}
