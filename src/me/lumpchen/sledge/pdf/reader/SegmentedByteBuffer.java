package me.lumpchen.sledge.pdf.reader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class SegmentedByteBuffer {

	private static int CAPACITY = 10240;
	private FileChannel channel;
	private int capacity;
	private long fileSize;
	private long filePoint;
	
	public SegmentedByteBuffer(FileChannel channel) throws IOException {
		this(channel, CAPACITY);
	}
	
	public SegmentedByteBuffer(FileChannel channel, int capacity) throws IOException {
		this.channel = channel;
		this.capacity = capacity;
		
		this.fileSize = this.channel.size();
	}

	public ByteBuffer readTrailerBytes() throws IOException {
		long trailerSize = 1024;
		long position = this.fileSize - trailerSize;
		ByteBuffer buf = this.channel.map(FileChannel.MapMode.READ_ONLY, position, trailerSize);
		return buf;
	}
	
	private ByteBuffer getTailBuffer() throws IOException {
		long position = this.fileSize - this.capacity;
		long size = this.capacity;
		if (position < 0) {
			position = 0;
			size = this.fileSize;
		}
		ByteBuffer buffer = this.channel.map(FileChannel.MapMode.READ_ONLY, position, size);
		return buffer;
	}
	
	
}
