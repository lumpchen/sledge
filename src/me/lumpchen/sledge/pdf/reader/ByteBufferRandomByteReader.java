package me.lumpchen.sledge.pdf.reader;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ByteBufferRandomByteReader implements RandomByteReader {

	private ByteBuffer buf;

	public ByteBufferRandomByteReader(byte[] bytes) {
		this.buf = ByteBuffer.wrap(bytes);
	}

	@Override
	public void position(long offset, boolean fromTrailer) {

	}

	@Override
	public void position(long pos) {
		this.buf.position((int) pos);
	}

	@Override
	public long position() {
		return this.buf.position();
	}

	@Override
	public long remain() {
		return this.buf.remaining();
	}

	@Override
	public byte read() throws IOException {
		return this.buf.get();
	}

	@Override
	public byte[] read(int size) throws IOException {
		if (size > this.buf.remaining()) {
			size = this.buf.remaining();
		}
		byte[] dst = new byte[size];
		this.buf.get(dst);
		return dst;
	}
}
