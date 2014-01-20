package me.lumpchen.sledge.pdf.syntax.decoder;

import java.nio.ByteBuffer;

import me.lumpchen.sledge.pdf.syntax.SyntaxException;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PName;

public abstract class Decode {

	protected PDictionary decodeParms;
	protected PName name;

	protected Decode(PName name) {
		this.name = name;
	}

	public PName getName() {
		return name;
	}

	public static Decode instance(PName filterName, PDictionary decodeParms) {
		Decode decoder = null;
		if (filterName.equals(PName.ASCII85Decode)) {
			decoder = new ASCII85Decode();
		} else if (filterName.equals(PName.FlateDecode)) {
			decoder = new FlateDecode();
		} else if (filterName.equals(PName.ASCIIHexDecode)) {
			decoder = new ASCIIHexDecode();
		} else if (filterName.equals(PName.LZWDecode)) {
			decoder = new LZWDecode();
		} else if (filterName.equals(PName.CCITTFaxDecode)) {
			decoder = new CCITTFaxDecode();
		} else if (filterName.equals(PName.Crypt)) {
			decoder = new CryptDecoder();
		} else if (filterName.equals(PName.DCTDecode)) {
			decoder = new DCTDecode();
		} else if (filterName.equals(PName.JBIG2Decode)) {
			decoder = new JBIG2Decoder();
		} else if (filterName.equals(PName.JPXDecode)) {
			decoder = new JPXDecoder();
		} else if (filterName.equals(PName.RunLengthDecode)) {
			decoder = new RunLengthDecode();
		} else { 
			throw new SyntaxException("unknown filter: " + filterName);			
		}

		decoder.setDecodeParms(decodeParms);
		return decoder;
	}

	protected void setDecodeParms(PDictionary decodeParms) {
		this.decodeParms = decodeParms;
	}
	
	abstract public ByteBuffer decode(ByteBuffer src);
	
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
