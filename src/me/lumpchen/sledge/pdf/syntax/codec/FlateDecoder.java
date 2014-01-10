package me.lumpchen.sledge.pdf.syntax.codec;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class FlateDecoder extends Decoder {

	private int predictor = 1;
	private int colors = 1;
	private int bitsPerComponent = 8;
	private int columns = 1;

	public FlateDecoder() {
		super(PName.FlateDecode);
	}

	public void setPredictor(int predictor) {
		this.predictor = predictor;
	}

	public void setColors(int colors) {
		this.colors = colors;
	}

	public void setBPS(int bps) {
		this.bitsPerComponent = bps;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	@Override
	public byte[] decode(byte[] in) {
		try {
			byte[] out = this.inflate(in);
			if (null == out) {
				return null;
			}
			out = decode1(out);
			return out;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private byte[] inflate(byte[] in) {
		try {
			Inflater decompresser = new Inflater();
			decompresser.setInput(in);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); 
			byte[] buffer = new byte[4096]; 
			while (!decompresser.finished()) { 
				int count = decompresser.inflate(buffer); 
				outputStream.write(buffer, 0, count); 
			}
			outputStream.close();
			return outputStream.toByteArray();	
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DataFormatException e) {
			e.printStackTrace();
		}
		return null;
	}

	private byte[] decode1(byte[] in) throws Exception {
		ByteArrayInputStream byis = new ByteArrayInputStream(in);
		BufferedInputStream bis = new BufferedInputStream(byis);

		// just workout size
		int count = applyPredictorFunction2(predictor, bis, null, colors,
				bitsPerComponent, columns);

		byis.close();
		bis.close();

		byte[] bos = new byte[count];

		byis = new ByteArrayInputStream(in);
		bis = new BufferedInputStream(byis);

		// now actually get the size.
		applyPredictorFunction2(predictor, bis, bos, colors, bitsPerComponent,
				columns);

		byis.close();
		bis.close();

		return bos;
	}

	private static int applyPredictorFunction2(int mainPred,
			BufferedInputStream bis, byte[] bos, int colors,
			int bitsPerComponent, int columns) throws Exception {

		int count = 0;

		int predictor;
		int bytesAvailable = bis.available();

		/**
		 * calculate values
		 */

		int bpp = (colors * bitsPerComponent + 7) / 8; // actual Bytes for a
														// pixel;

		int rowLength = (columns * colors * bitsPerComponent + 7) / 8 + bpp; // length
																				// of
																				// each
																				// row
																				// +
																				// predictor

		// array to hold 2 lines
		byte[] thisLine = new byte[rowLength];
		byte[] lastLine = new byte[rowLength];

		// extra predictor needed for optimization
		int curPred;

		// actual processing loop
		try {
			int byteCount = 0;
			while (true) {

				// exit after all used
				if (bytesAvailable <= byteCount)
					break;

				// set predictor
				predictor = mainPred;

				/**
				 * read line
				 */
				int i = 0;
				int offset = bpp;

				// PNG optimization.
				if (predictor >= 10) {
					curPred = bis.read();
					if (curPred == -1) {
						break;
					}
					curPred += 10;
				} else {
					curPred = predictor;
				}

				while (offset < rowLength) {

					i = bis.read(thisLine, offset, rowLength - offset);

					if (i == -1)
						break;

					offset += i;
					byteCount += i;
				}

				if (i == -1)
					break;

				// apply

				switch (curPred) {

				case 2: // tiff (same as sub)
					for (int i1 = bpp; i1 < rowLength; i1++) {

						int sub = thisLine[i1] & 0xff;
						int raw = thisLine[i1 - bpp] & 0xff;
						thisLine[i1] = (byte) ((sub + raw) & 0xff);
						if (bos != null)
							bos[count] = thisLine[i1];

						count++;

					}
					break;

				case 10: // just pass through
					for (int i1 = bpp; i1 < rowLength; i1++) {

						if (bos != null)
							bos[count] = thisLine[i1];

						count++;

					}

					break;

				case 11: // sub
					for (int i1 = bpp; i1 < rowLength; i1++) {

						int sub = thisLine[i1] & 0xff;
						int raw = thisLine[i1 - bpp] & 0xff;
						thisLine[i1] = (byte) ((sub + raw));

						if (bos != null)
							bos[count] = thisLine[i1];

						count++;
					}
					break;

				case 12: // up
					for (int i1 = bpp; i1 < rowLength; i1++) {

						int sub = (lastLine[i1] & 0xff) + (thisLine[i1] & 0xff);
						thisLine[i1] = (byte) (sub);

						if (bos != null)
							bos[count] = thisLine[i1];

						count++;
					}

					break;

				case 13: // avg
					for (int i1 = bpp; i1 < rowLength; i1++) {

						int av = thisLine[i1] & 0xff;
						int floor = ((thisLine[i1 - bpp] & 0xff)
								+ (lastLine[i1] & 0xff) >> 1);
						thisLine[i1] = (byte) (av + floor);

						if (bos != null)
							bos[count] = thisLine[i1];

						count++;
					}
					break;

				case 14: // paeth (see http://www.w3.org/TR/PNG-Filters.html)
					for (int i1 = bpp; i1 < rowLength; i1++) {

						int a = thisLine[i1 - bpp] & 0xff, b = lastLine[i1] & 0xff, c = lastLine[i1
								- bpp] & 0xff;

						int p = a + b - c;

						int pa = p - a, pb = p - b, pc = p - c;

						// make sure positive
						if (pa < 0)
							pa = -pa;
						if (pb < 0)
							pb = -pb;
						if (pc < 0)
							pc = -pc;

						if (pa <= pb && pa <= pc)
							thisLine[i1] = (byte) (thisLine[i1] + a);
						else if (pb <= pc)
							thisLine[i1] = (byte) (thisLine[i1] + b);
						else
							thisLine[i1] = (byte) (thisLine[i1] + c);

						if (bos != null)
							bos[count] = thisLine[i1];

						count++;

					}
					break;

				case 15:
					// implemented inside main code body
					break;
				default:

					break;
				}

				// add to output and update line
				System.arraycopy(thisLine, 0, lastLine, 0, lastLine.length);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return count;
	}
}
