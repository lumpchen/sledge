package me.lumpchen.sledge.pdf.syntax.decoder;

import java.nio.ByteBuffer;

import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class CryptDecoder extends Decode {

	protected CryptDecoder() {
		super(PName.Crypt);
	}

	@Override
	public ByteBuffer decode(ByteBuffer src) {
		return null;
	}

}
