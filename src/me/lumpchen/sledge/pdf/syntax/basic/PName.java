package me.lumpchen.sledge.pdf.syntax.basic;

import me.lumpchen.sledge.pdf.syntax.PObject;

public abstract class PName extends PObject {
	
	byte[] name;
	PObject value;
	
	void parse() {
		
	}
}

class PDFSize extends PName {
	
	public static final byte[] NAME = {'S', 'i', 'z', 'e'};
	
	private PInteger size;
	
	public void read(byte[] data) {
		
	}
	
	public String toString() {
		return "";
	}
}
