package me.lumpchen.sledge.pdf.syntax.codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.InflaterInputStream;

import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class FlateDecoder extends Decoder {

	protected FlateDecoder() {
		super(PName.FlateDecode);
	}

	@Override
	public byte[] decode(byte[] in) {
		byte b[] = decode(in, true);
		if (b == null)
			return decode(in, false);
		return b;
	}

	private byte[] decode(byte[] in, boolean strict) {
		ByteArrayInputStream stream = new ByteArrayInputStream(in);
		InflaterInputStream zip = new InflaterInputStream(stream);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte b[] = new byte[strict ? 4092 : 1];
		try {
			int n;
			while ((n = zip.read(b)) >= 0) {
				out.write(b, 0, n);
			}
			zip.close();
			out.close();
			return out.toByteArray();
		} catch (Exception e) {
			if (strict) {
				return null;
			}
			return out.toByteArray();
		}
	}
}
