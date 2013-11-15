package me.lumpchen.sledge.pdf.syntax.basic;

public class PInteger extends PNumber {

	private int value;

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
