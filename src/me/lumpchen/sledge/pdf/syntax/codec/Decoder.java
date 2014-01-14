package me.lumpchen.sledge.pdf.syntax.codec;

import me.lumpchen.sledge.pdf.syntax.SyntaxException;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PName;

public abstract class Decoder {

	protected PName name;

	protected Decoder(PName name) {
		this.name = name;
	}

	public PName getName() {
		return name;
	}

	public static Decoder instance(PName filterName, PDictionary decodeParms) {
		Decoder decoder = null;
		if (filterName.equals(PName.ASCII85Decode)) {
			decoder = new ASCII85Decoder();
		} else if (filterName.equals(PName.FlateDecode)) {
			decoder = new FlateDecoder();
		} else if (filterName.equals(PName.ASCIIHexDecode)) {
			decoder = new ASCIIHexDecoder();
		} else if (filterName.equals(PName.LZWDecode)) {
			decoder = new LZWDecoder();
		} else if (filterName.equals(PName.CCITTFaxDecode)) {
			decoder = new CCITTFaxDecoder();
		} else if (filterName.equals(PName.Crypt)) {
			decoder = new CryptDecoder();
		} else if (filterName.equals(PName.DCTDecode)) {
			decoder = new DCTDecoder();
		} else if (filterName.equals(PName.JBIG2Decode)) {
			decoder = new JBIG2Decoder();
		} else if (filterName.equals(PName.JPXDecode)) {
			decoder = new JPXDecoder();
		} else if (filterName.equals(PName.RunLengthDecode)) {
			decoder = new RunLengthDecoder();
		} else { 
			throw new SyntaxException("unknown filter: " + filterName);			
		}

		decoder.setDecodeParms(decodeParms);
		return decoder;
	}

	abstract public void setDecodeParms(PDictionary decodeParms);
	abstract public byte[] decode(byte[] src);

	public static final boolean isWhitespace(int ch) {
		return (ch == 0 || ch == 9 || ch == 10 || ch == 12 || ch == 13 || ch == 32);
	}

	public static int toHex(int c) {
		if (c >= '0' && c <= '9') {
			return c - '0';
		} else if (c >= 'A' && c <= 'F') {
			return c - 'A' + 10;
		} else if (c >= 'a' && c <= 'f') {
			return c - 'a' + 10;
		}
		return -1;
	}
}
