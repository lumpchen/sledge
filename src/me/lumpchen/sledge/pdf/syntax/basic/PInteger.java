package me.lumpchen.sledge.pdf.syntax.basic;

import me.lumpchen.sledge.pdf.reader.ObjectReader;

public class PInteger extends PNumber {

	private int value;

	public PInteger() {
	}
	
	public PInteger(int value) {
		this.value = value;
	}

	public PInteger(String s) {
		this(Integer.parseInt(s));
	}

	public int getValue() {
		return this.value;
	}

	public String toString() {
		return this.value + "";
	}
	
	@Override
	protected void readBody(ObjectReader reader) {
		this.value = reader.readInt();
	}
	
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof PInteger)) {
			return false;
		}
		if (((PInteger) obj).value != this.value) {
			return false;
		}
		
		return true;
	}
}
