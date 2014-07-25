package me.lumpchen.sledge.pdf.syntax.filters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import me.lumpchen.sledge.pdf.syntax.lang.PName;
import me.lumpchen.sledge.pdf.syntax.lang.PNumber;

public class LZWDecode extends Decode {
	int bytepos;
	int bitpos;
	byte[] dict[] = new byte[4096][];
	int dictlen = 0;
	int bitspercode = 9;
	static int STOP = 257;
	static int CLEARDICT = 256;

	protected LZWDecode() {
		super(PName.LZWDecode);
	}

	@Override
	public ByteBuffer decode(ByteBuffer src) {
		try {
			ByteBuffer outBytes = this.decodeLzw(src);

			// undo a predictor algorithm, if any was used
			PNumber pd = this.decodeParms.getValueAsNumber(PName.Predictor);
			if (pd != null) {
				Predictor predictor = Predictor.getPredictor(this.decodeParms);
				if (predictor != null) {
					outBytes = predictor.unpredict(outBytes);
				}
			}
			return outBytes;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private ByteBuffer decodeLzw(ByteBuffer buf) throws DecodeException {
		// algorithm derived from:
		// http://www.rasip.fer.hr/research/compress/algorithms/fund/lz/lzw.html
		// and the PDFReference
		int cW = CLEARDICT;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while (true) {
			int pW = cW;
			cW = nextCode(buf);
			if (cW == -1) {
				throw new DecodeException("Missed the stop code in LZWDecode!");
			}
			if (cW == STOP) {
				break;
			} else if (cW == CLEARDICT) {
				resetDict();
				// pW= -1;
			} else if (pW == CLEARDICT) {
				baos.write(dict[cW], 0, dict[cW].length);
			} else {
				if (cW < dictlen) { // it's a code in the dictionary
					baos.write(dict[cW], 0, dict[cW].length);
					byte[] p = new byte[dict[pW].length + 1];
					System.arraycopy(dict[pW], 0, p, 0, dict[pW].length);
					p[dict[pW].length] = dict[cW][0];
					dict[dictlen++] = p;
				} else { // not in the dictionary (should==dictlen)
					// if (cW!=dictlen) {
					// System.out.println("Got a bouncy code: "+cW+" (dictlen="+dictlen+")");
					// }
					byte[] p = new byte[dict[pW].length + 1];
					System.arraycopy(dict[pW], 0, p, 0, dict[pW].length);
					p[dict[pW].length] = p[0];
					baos.write(p, 0, p.length);
					dict[dictlen++] = p;
				}
				if (dictlen >= (1 << bitspercode) - 1 && bitspercode < 12) {
					bitspercode++;
				}
			}
		}
		return ByteBuffer.wrap(baos.toByteArray());
	}

	/**
	 * get the next code from the input stream
	 */
	private int nextCode(ByteBuffer buf) {
		int fillbits = bitspercode;
		int value = 0;
		if (bytepos >= buf.limit() - 1) {
			return -1;
		}
		while (fillbits > 0) {
			int nextbits = buf.get(bytepos); // bitsource
			int bitsfromhere = 8 - bitpos; // how many bits can we take?
			if (bitsfromhere > fillbits) { // don't take more than we need
				bitsfromhere = fillbits;
			}
			value |= ((nextbits >> (8 - bitpos - bitsfromhere)) & (0xff >> (8 - bitsfromhere))) << (fillbits - bitsfromhere);
			fillbits -= bitsfromhere;
			bitpos += bitsfromhere;
			if (bitpos >= 8) {
				bitpos = 0;
				bytepos++;
			}
		}
		return value;
	}

	/**
	 * reset the dictionary to the initial 258 entries
	 */
	private void resetDict() {
		dictlen = 258;
		bitspercode = 9;
	}
}
