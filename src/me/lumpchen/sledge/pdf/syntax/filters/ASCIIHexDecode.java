package me.lumpchen.sledge.pdf.syntax.filters;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import me.lumpchen.sledge.pdf.syntax.lang.PName;

public class ASCIIHexDecode extends Decode {

	protected ASCIIHexDecode() {
		super(PName.ASCIIHexDecode);
	}

	/**
	 * get the next character from the input
	 * 
	 * @return a number from 0-15, or -1 for the end character
	 */
	private int readHexDigit(ByteBuffer buf) throws DecodeException {
		// read until we hit a non-whitespace character or the
		// end of the stream
		while (buf.remaining() > 0) {
			int c = (int) buf.get();

			// see if we found a useful character
			if (!isWhitespace((char) c)) {
				if (c >= '0' && c <= '9') {
					c -= '0';
				} else if (c >= 'a' && c <= 'f') {
					c -= 'a' - 10;
				} else if (c >= 'A' && c <= 'F') {
					c -= 'A' - 10;
				} else if (c == '>') {
					c = -1;
				} else {
					// unknown character
					throw new DecodeException("Bad character " + c
							+ "in ASCIIHex decode");
				}

				// return the useful character
				return c;
			}
		}

		// end of stream reached
		throw new DecodeException("Short stream in ASCIIHex decode");
	}

	@Override
	public ByteBuffer decode(ByteBuffer src) {
		// start at the beginning of the buffer
		src.rewind();

		// allocate the output buffer
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		while (true) {
			int first = readHexDigit(src);
			int second = readHexDigit(src);

			if (first == -1) {
				break;
			} else if (second == -1) {
				baos.write((byte) (first << 4));
				break;
			} else {
				baos.write((byte) ((first << 4) + second));
			}
		}

		return ByteBuffer.wrap(baos.toByteArray());
	}

}
