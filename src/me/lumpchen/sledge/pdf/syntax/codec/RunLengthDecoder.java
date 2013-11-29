package me.lumpchen.sledge.pdf.syntax.codec;

import java.io.ByteArrayOutputStream;

import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class RunLengthDecoder extends Decoder {

	protected RunLengthDecoder() {
		super(PName.RunLengthDecode);
	}

	@Override
	public byte[] decode(byte[] in) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte dupCount = -1;
		for (int i = 0; i < in.length; i++) {
			dupCount = in[i];
			if (dupCount == -128) {
				break; // this is implicit end of data
			}

			if (dupCount >= 0 && dupCount <= 127) {
				int bytesToCopy = dupCount + 1;
				baos.write(in, i, bytesToCopy);
				i += bytesToCopy;
			} else {
				i++;
				for (int j = 0; j < 1 - (int) (dupCount); j++) {
					baos.write(in[i]);
				}
			}
		}

		return baos.toByteArray();
	}

}
