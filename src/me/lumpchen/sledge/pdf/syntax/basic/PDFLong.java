package me.lumpchen.sledge.pdf.syntax.basic;

public class PDFLong extends PDFNumber {

	private long value;

	public PDFLong(long value) {
		this.value = value;
	}
	
	public long getValue() {
		return this.value;
	}
	
	public String toString() {
		return Long.toString(this.value);
	}
}
