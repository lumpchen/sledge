package me.lumpchen.sledge.pdf.syntax.basic;


public class PNumber extends PObject {
	
	private Number number;
	
	public PNumber() {
		super.type = TYPE.Number;
	}
	
	public PNumber(int i) {
		super.type = TYPE.Number;
		this.number = i;
	}
	
	public PNumber(long l) {
		super.type = TYPE.Number;
		this.number = l;
	}
	
	public PNumber(double d) {
		super.type = TYPE.Number;
		this.number = d;
	}
	
	public PNumber(float f) {
		super.type = TYPE.Number;
		this.number = f;
	}
	
	public String toString() {
		return this.number.toString();
	}
	
	public int intValue() {
		return this.number.intValue();
	}
	
	public long longValue() {
		return this.number.longValue();
	}
	
	public double doubleValue() {
		return this.number.doubleValue();
	}
	
	public float floatValue() {
		return this.number.floatValue();
	}
	
	public void setNumber(Number number) {
		this.number = number;
	}
	
	public Number getNumber() {
		return this.number;
	}

}
