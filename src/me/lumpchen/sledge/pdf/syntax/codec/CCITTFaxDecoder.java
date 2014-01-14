package me.lumpchen.sledge.pdf.syntax.codec;

import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class CCITTFaxDecoder extends Decoder {

	private int identify; // K
	private boolean endOfLine;
	private boolean encodedByteAlign;
	private int coloums;
	private int rows;
	private boolean endOfBlock;
	private boolean blackIs1;
	private int damagedRowsBeforeError;
	
	protected CCITTFaxDecoder() {
		super(PName.CCITTFaxDecode);
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
