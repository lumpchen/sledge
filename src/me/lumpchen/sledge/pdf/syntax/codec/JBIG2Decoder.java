package me.lumpchen.sledge.pdf.syntax.codec;

import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class JBIG2Decoder extends Decoder {

	protected JBIG2Decoder() {
		super(PName.JBIG2Decode);
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
