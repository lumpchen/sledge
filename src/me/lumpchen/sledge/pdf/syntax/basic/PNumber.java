package me.lumpchen.sledge.pdf.syntax.basic;

import me.lumpchen.sledge.pdf.reader.ObjectReader;
import me.lumpchen.sledge.pdf.syntax.SyntaxException;

public class PNumber extends PObject {
	
	private Number number;
	
	public PNumber() {
	}
	
	public PNumber(int i) {
		this.number = i;
	}
	
	public PNumber(long l) {
		this.number = l;
	}
	
	public PNumber(double d) {
		this.number = d;
	}
	
	public PNumber(float f) {
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
	
	@Override
	protected void readBeginTag(ObjectReader reader) {
	}

	@Override
	protected void readBody(ObjectReader reader) {
		byte[] bytes = reader.readToNextToken();
		if (null == bytes || bytes.length <= 0) {
			throw new SyntaxException();
		}
		
		String s = new String(bytes);
		if (s.indexOf('.') >= 0) {
			this.number = Double.parseDouble(s);
		} else {
			this.number = Integer.parseInt(s, 10);			
		}
	}

	@Override
	protected void readEndTag(ObjectReader reader) {
	}

}
