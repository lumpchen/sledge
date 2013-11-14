package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.syntax.basic.PDFArray;
import me.lumpchen.sledge.pdf.syntax.basic.PDFDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PDFInteger;
import me.lumpchen.sledge.pdf.syntax.basic.PDFLong;

public class PDFTrailer {

	public static byte[] TRAILER = { 't', 'r', 'a', 'i', 'l', 'e', 'r' };
	public static byte[] STARTXREF = {'s', 't', 'a', 'r', 't', 'x', 'r', 'e', 'f'};
	
	private PDFInteger size;
	private PDFInteger prev;
	private PDFDictionary root;
	private PDFDictionary encrypt;
	private PDFDictionary info;
	private PDFArray id;
	
	private PDFLong startxref;

	public PDFTrailer() {

	}

	public void setStartxref(PDFLong pos) {
		this.startxref = pos;
	}
	
	public PDFLong getStartxref() {
		return this.startxref;
	}
}
