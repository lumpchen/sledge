package me.lumpchen.sledge.pdf.graphics;

public abstract class Color {
	
	protected Color() {
	}
	
	abstract public java.awt.Color toJavaColor();
}

class RGBColor extends Color {
	
	private float red;
	private float green;
	private float blue;
	private float alpha;
	
	public RGBColor(float r, float g, float b) {
		this(r, g, b, 0);
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

class CMYKColor extends Color {
	
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
