package me.lumpchen.sledge.pdf.syntax.filters;

import java.nio.ByteBuffer;

import me.lumpchen.sledge.pdf.syntax.lang.PName;

public class CryptDecoder extends Decode {

	protected CryptDecoder() {
		super(PName.Crypt);
	}

	@Override
	public ByteBuffer decode(ByteBuffer src) {
		return null;
	}

}
