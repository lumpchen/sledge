package me.lumpchen.sledge.pdf.syntax.filters;

import java.io.IOException;
import java.nio.ByteBuffer;

import me.lumpchen.sledge.pdf.syntax.lang.PBoolean;
import me.lumpchen.sledge.pdf.syntax.lang.PName;
import me.lumpchen.sledge.pdf.syntax.lang.PNumber;

public class CCITTFaxDecode extends Decode {

	private int identify; // K
	private boolean endOfLine;
	private boolean encodedByteAlign;
	private int columns;
	private int rows;
	private boolean endOfBlock;
	private boolean blackIs1;
	private int damagedRowsBeforeError;

	protected CCITTFaxDecode() {
		super(PName.CCITTFaxDecode);
	}

	@Override
	public ByteBuffer decode(ByteBuffer src) {
		byte[] bytes = new byte[src.remaining()];
		src.get(bytes, 0, bytes.length);
		try {
			return ByteBuffer.wrap(decode(bytes));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected byte[] decode(byte[] source) throws IOException {
		int width = 1728;
		int height = -1;

		//
		int columns = getOptionFieldInt("Columns", width);
		int rows = getOptionFieldInt("Rows", height);
		int k = getOptionFieldInt("K", 0);
		int size = rows * ((columns + 7) >> 3);
		byte[] destination = new byte[size];

		boolean align = getOptionFieldBoolean("EncodedByteAlign", false);

		CCITTFaxDecoder decoder = new CCITTFaxDecoder(1, columns, rows);
		decoder.setAlign(align);
		if (k == 0) {
			decoder.decodeT41D(destination, source, 0, rows);
		} else if (k > 0) {
			decoder.decodeT42D(destination, source, 0, rows);
		} else if (k < 0) {
			decoder.decodeT6(destination, source, 0, rows);
		}
		if (!getOptionFieldBoolean("BlackIs1", false)) {
			for (int i = 0; i < destination.length; i++) {
				// bitwise not
				destination[i] = (byte) ~destination[i];
			}
		}

		return destination;
	}

	public int getOptionFieldInt(String name, int defaultValue)
			throws IOException {
		if (this.decodeParms == null) {
			return defaultValue;
		}
		PNumber value = this.decodeParms.getValueAsNumber(PName.instance(name));
		if (value == null) {
			return defaultValue;
		}
		return value.intValue();
	}

	public boolean getOptionFieldBoolean(String name, boolean defaultValue)
			throws IOException {
		if (this.decodeParms == null) {
			return defaultValue;
		}
		PBoolean value = this.decodeParms.getValueAsBool(PName.instance(name));
		if (value == null) {
			return defaultValue;
		}
		return value.getValue();
	}
}
