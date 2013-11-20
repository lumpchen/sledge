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
		long trailerSize = 2048;
		long position = this.fileSize - trailerSize;
		ByteBuffer buf = this.channel.map(FileChannel.MapMode.READ_ONLY, position, trailerSize);
		return buf;
	}
}
