package me.lumpchen.sledge.pdf.syntax.basic;

import me.lumpchen.sledge.pdf.syntax.SyntaxException;

public class Rectangle {

	private PNumber llx;
	private PNumber lly;
	private PNumber urx;
	private PNumber ury;
	
	public Rectangle() {
	}
	
	public Rectangle(PNumber llx, PNumber lly, PNumber urx, PNumber ury) {
		this.llx = llx;
		this.lly = lly;
		this.urx = urx;
		this.ury = ury;
	}
	
	public Rectangle(PArray rect) {
		if (rect.size() != 4) {
			throw new SyntaxException("mediabox array is invalid as: " + rect);
		}
		this.llx = (PNumber) rect.getChild(0);
		this.lly = (PNumber) rect.getChild(1);
		this.urx = (PNumber) rect.getChild(2);
		this.ury = (PNumber) rect.getChild(3);
	}

	public PNumber llx() {
		return this.llx;
	}
	
	public PNumber lly() {
		return this.lly;
	}
	
	public PNumber urx() {
		return this.urx;
	}
	
	public PNumber ury() {
		return this.ury;
	}
	
	public double getWidth() {
		return this.urx.doubleValue() - this.llx.doubleValue();
	}
	
	public double getHeight() {
		return this.ury.doubleValue() - this.lly.doubleValue();
	}
}
