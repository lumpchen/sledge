package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PInteger;
import me.lumpchen.sledge.pdf.syntax.basic.PLong;

public class PDFTrailer {

	public static byte[] TRAILER = { 't', 'r', 'a', 'i', 'l', 'e', 'r' };
	public static byte[] STARTXREF = {'s', 't', 'a', 'r', 't', 'x', 'r', 'e', 'f'};
	
	private PInteger size;
	private PInteger prev;
	private PDictionary root;
	private PDictionary encrypt;
	private PDictionary info;
	private PArray id;
	
	private PLong startxref;

	public PDFTrailer() {

	}

	public void setStartxref(PLong pos) {
		this.startxref = pos;
	}
	
	public PLong getStartxref() {
		return this.startxref;
	}
}
