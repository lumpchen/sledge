package me.lumpchen.sledge.pdf.syntax.codec;

import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class CryptDecoder extends Decoder {

	protected CryptDecoder() {
		super(PName.Crypt);
	}

	@Override
	public byte[] decode(byte[] src) {
		return null;
	}

	@Override
	public void setDecodeParms(PDictionary decodeParms) {
		// TODO Auto-generated method stub
		
	}

}
