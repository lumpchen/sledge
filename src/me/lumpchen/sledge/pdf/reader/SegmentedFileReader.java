package me.lumpchen.sledge.pdf.reader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class SegmentedFileReader {

	private FileChannel channel;
	private long fileSize;
	
	public SegmentedFileReader(FileChannel channel) throws IOException {
		this.channel = channel;
		this.fileSize = this.channel.size();
	}

	public ByteBuffer readBufferFromTrailer(int size) throws IOException {
		long position = this.fileSize - size;
		ByteBuffer buf = this.channel.map(FileChannel.MapMode.READ_ONLY, position, size);
		return buf;
	}
	
	public ByteBuffer readBuffer(long offset, long size) throws IOException {
		long remain = this.fileSize - size;
		size = remain > size ? size : remain;
		ByteBuffer buf = this.channel.map(FileChannel.MapMode.READ_ONLY, offset, size);
		return buf;
	}
}
