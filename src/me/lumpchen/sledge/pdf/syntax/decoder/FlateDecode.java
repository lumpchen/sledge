package me.lumpchen.sledge.pdf.syntax.decoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PNumber;

public class FlateDecode extends Decode {

	public FlateDecode() {
		super(PName.FlateDecode);
	}

	@Override
	public ByteBuffer decode(ByteBuffer src) {
		Inflater inf = new Inflater(false);

		int bufSize = src.remaining();

		byte[] data = new byte[bufSize];
		src.get(data);

		// set the input to the inflater
		inf.setInput(data);

		// output to a byte-array output stream, since we don't
		// know how big the output will be
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] decomp = new byte[bufSize];
		int read = 0;

		try {
			while (!inf.finished()) {
				read = inf.inflate(decomp);
				if (read <= 0) {
					if (inf.needsDictionary()) {
						throw new DecodeException("Don't know how to ask for a dictionary in FlateDecode");
					} else {
						return ByteBuffer.allocate(0);
					}
				}
				baos.write(decomp, 0, read);
			}
		} catch (DataFormatException dfe) {
			throw new DecodeException("Data format exception:" + dfe.getMessage());
		}

		// return the output as a byte buffer
		ByteBuffer outBytes = ByteBuffer.wrap(baos.toByteArray());

		if (this.decodeParms != null) {
			// undo a predictor algorithm, if any was used
			PNumber pd = this.decodeParms.getValueAsNumber(PName.Predictor);
			if (pd != null) {
				try {
					Predictor predictor = Predictor.getPredictor(this.decodeParms);
					if (predictor != null) {
						outBytes = predictor.unpredict(outBytes);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return outBytes;
	}
}
