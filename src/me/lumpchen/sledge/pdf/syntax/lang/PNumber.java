package me.lumpchen.sledge.pdf.syntax.lang;


public class PNumber extends PObject {
	
	private Number number;
	
	public PNumber() {
		super.classType = ClassType.Number;
	}
	
	public PNumber(int i) {
		super.classType = ClassType.Number;
		this.number = i;
	}
	
	public PNumber(long l) {
		super.classType = ClassType.Number;
		this.number = l;
	}
	
	public PNumber(double d) {
		super.classType = ClassType.Number;
		this.number = d;
	}
	
	public PNumber(float f) {
		super.classType = ClassType.Number;
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
