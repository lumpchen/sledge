package me.lumpchen.sledge.pdf.syntax.decoder;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class RunLengthDecode extends Decode {
	/** the end of data in the RunLength encoding. */
	private static final int RUN_LENGTH_EOD = 128;

	protected RunLengthDecode() {
		super(PName.RunLengthDecode);
	}

	@Override
	public ByteBuffer decode(ByteBuffer src) {
		// start at the beginning of the buffer
		src.rewind();

		// allocate the output buffer
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte dupAmount = -1;
		byte[] buffer = new byte[128];
		while ((dupAmount = src.get()) != -1 && dupAmount != RUN_LENGTH_EOD) {
			if (dupAmount <= 127) {
				int amountToCopy = dupAmount + 1;
				while (amountToCopy > 0) {
					src.get(buffer, 0, amountToCopy);
					baos.write(buffer, 0, amountToCopy);
				}
			} else {
				byte dupByte = src.get();
				for (int i = 0; i < 257 - (int) (dupAmount & 0xFF); i++) {
					baos.write(dupByte);
				}
			}
		}
		return ByteBuffer.wrap(baos.toByteArray());
	}

}
