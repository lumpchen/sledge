package me.lumpchen.sledge.pdf.graphics;

public abstract class Color {
	
	public static Color white = new RGBColor(1.0f, 1.0f, 1.0f);
	public static Color black = new RGBColor(0f, 0f, 0f);
	public static Color red = new RGBColor(1.0f, 0, 0);
	public static Color green = new RGBColor(0, 1.0f, 0);
	public static Color blue = new RGBColor(0, 0, 1.0f);
	
	protected Color() {
	}
	
	abstract public java.awt.Color toJavaColor();
}

class RGBColor extends Color {
	
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
