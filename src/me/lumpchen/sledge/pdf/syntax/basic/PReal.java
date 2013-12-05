package me.lumpchen.sledge.pdf.syntax.basic;

import me.lumpchen.sledge.pdf.reader.ObjectReader;

public class PReal extends PNumber {

	private double value;
	
	public PReal(double value) {
		this.value = value;
	}
	
	public double getValue() {
		return this.value;
	}
	
	public String toString() {
		return this.value + "";
	}
	
	@Override
	protected void readBody(ObjectReader reader) {
		this.value = reader.readDouble();
	}
}
