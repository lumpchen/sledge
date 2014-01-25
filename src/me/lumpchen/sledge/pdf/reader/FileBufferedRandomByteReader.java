package me.lumpchen.sledge.pdf.reader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileBufferedRandomByteReader implements RandomByteReader {
	
	public static final int BUFFER_SIZE = 4096;
	private FileChannel channel;
	
	private long fileSize;
	private long position;
	private ByteBuffer buffer;
	
	public FileBufferedRandomByteReader(FileChannel channel) throws IOException {
		this.channel = channel;
		this.fileSize = this.channel.size();
	}
	
	@Override
	public void position(long pos) {
		if (pos > this.fileSize) {
			throw new ReadException("the position is greater than file size(" + this.fileSize + "): " + pos);
		}
		this.position(pos, false);
	}

	@Override
	public void position(long offset, boolean fromTrailer) {
		if (fromTrailer) {
			this.position = this.fileSize - offset;
		} else {
			this.position = offset;
		}
	}
	
	@Override
	public long position() {
		return this.position;
	}

	@Override
	public byte read() throws IOException {
		if (this.buffer == null || this.buffer.remaining() == 0) {
			this.fillBuffer();
		}
		return this.buffer.get();
	}

	@Override
	public byte[] read(int size) throws IOException {
		if (this.buffer == null || size > this.buffer.remaining()) {
			this.position -= this.buffer == null ? 0 : this.buffer.remaining();
			this.fillBuffer(size);
		}
		
		if (size > this.buffer.remaining()) {
			size = this.buffer.remaining();
		}
		byte[] dst = new byte[size];
		this.buffer.get(dst);
		return dst;
	}

	@Override
	public long remain() {
		return this.fileSize - this.position;
	}

	private void fillBuffer() throws IOException {
		this.fillBuffer(BUFFER_SIZE);
	}
	
	private void fillBuffer(int size) throws IOException {
		if (this.position >= this.fileSize) {
			return;
		}
		if (this.position + size > this.fileSize) {
			size = (int) (this.fileSize - this.position);
		}
		this.buffer = this.channel.map(FileChannel.MapMode.READ_ONLY, position, size);
		this.position += size;
	}
}
