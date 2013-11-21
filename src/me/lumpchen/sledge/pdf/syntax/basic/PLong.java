package me.lumpchen.sledge.pdf.syntax.basic;

public class PLong extends PNumber {

	private long value;

	public PLong(long value) {
		this.value = value;
	}
	
	public long getValue() {
		return this.value;
	}

	public String toString() {
		return Long.toString(this.value);
	}
}
