package me.lumpchen.sledge.pdf.syntax.basic;

public class PDFInteger extends PDFNumber {

	private int value;
	
	public PDFInteger(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
}
