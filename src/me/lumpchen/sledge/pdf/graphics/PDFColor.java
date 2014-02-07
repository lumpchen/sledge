package me.lumpchen.sledge.pdf.graphics;

import me.lumpchen.sledge.pdf.graphics.colorspace.PDFColorSpace;

public abstract class PDFColor {
	
	public static PDFColor white = new RGBColor(1.0f, 1.0f, 1.0f);
	public static PDFColor black = new RGBColor(0f, 0f, 0f);
	public static PDFColor red = new RGBColor(1.0f, 0, 0);
	public static PDFColor green = new RGBColor(0, 1.0f, 0);
	public static PDFColor blue = new RGBColor(0, 0, 1.0f);

	protected PDFColor() {
	}
	
	public PDFColor instance(PDFColorSpace cs) {
		return null;
	}
	
	abstract public java.awt.Color toJavaColor();
}

class RGBColor extends PDFColor {
	
	private float red = 0;
	private float green = 0;
	private float blue = 0;
	private float alpha = 1.0f;
	
	public RGBColor(float r, float g, float b) {
		this(r, g, b, 1.0f);
	}
	
	public RGBColor(float r, float g, float b, float a) {
		this.red = r;
		this.green = g;
		this.blue = b;
		this.alpha = a;
	}
	
	@Override
	public java.awt.Color toJavaColor() {
		java.awt.Color color = new java.awt.Color(red, green, blue, alpha);
		return color;
	}
}

class CMYKColor extends PDFColor {
	
	private float cyan;
	private float megenta;
	private float yellow;
	private float black;

	public CMYKColor(float c, float m, float y, float k) {
		this.cyan = c;
		this.megenta = m;
		this.yellow =y;
		this.black = k;
	}
	
	@Override
	public java.awt.Color toJavaColor() {
		return java.awt.Color.magenta;
	}
	
}
