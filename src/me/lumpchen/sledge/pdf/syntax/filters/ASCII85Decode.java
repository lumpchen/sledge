package me.lumpchen.sledge.pdf.syntax.filters;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import me.lumpchen.sledge.pdf.syntax.lang.PName;

public class ASCII85Decode extends Decode {

	public ASCII85Decode() {
		super(PName.ASCII85Decode);
	}

	@Override
	public ByteBuffer decode(ByteBuffer src) {
		// start from the beginning of the data
		src.rewind();

		// allocate the output buffer
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		// decode the bytes
		while (decode5(src, baos)) {
		}

		return ByteBuffer.wrap(baos.toByteArray());
	}

	/**
	 * get the next character from the input.
	 * 
	 * @return the next character, or -1 if at end of stream
	 */
	private int nextChar(ByteBuffer buf) {
		// skip whitespace
		// returns next character, or -1 if end of stream
		while (buf.remaining() > 0) {
			char c = (char) buf.get();

			if (!super.isWhitespace(c)) {
				return c;
			}
		}

		// EOF reached
		return -1;
	}

	/**
	 * decode the next five ASCII85 characters into up to four decoded bytes.
	 * Return false when finished, or true otherwise.
	 * 
	 * @param baos
	 *            the ByteArrayOutputStream to write output to, set to the
	 *            correct position
	 * @return false when finished, or true otherwise.
	 */
	private boolean decode5(ByteBuffer buf, ByteArrayOutputStream baos)
			throws DecodeException {
		// stream ends in ~>
		int[] five = new int[5];
		int i;
		for (i = 0; i < 5; i++) {
			five[i] = nextChar(buf);
			if (five[i] == '~') {
				if (nextChar(buf) == '>') {
					break;
				} else {
					throw new DecodeException(
							"Bad character in ASCII85Decode: not ~>");
				}
			} else if (five[i] >= '!' && five[i] <= 'u') {
				five[i] -= '!';
			} else if (five[i] == 'z') {
				if (i == 0) {
					five[i] = 0;
					i = 4;
				} else {
					throw new DecodeException(
							"Inappropriate 'z' in ASCII85Decode");
				}
			} else {
				throw new DecodeException("Bad character in ASCII85Decode: "
						+ five[i] + " (" + (char) five[i] + ")");
			}
		}

		if (i > 0) {
			i -= 1;
		}

		int value = five[0] * 85 * 85 * 85 * 85 + five[1] * 85 * 85 * 85
				+ five[2] * 85 * 85 + five[3] * 85 + five[4];

		for (int j = 0; j < i; j++) {
			int shift = 8 * (3 - j);
			baos.write((byte) ((value >> shift) & 0xff));
		}

		return (i == 4);
	}
}
