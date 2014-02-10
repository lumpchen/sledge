package me.lumpchen.sledge.pdf.graphics;

public class RGBColor extends PDFColor {

	private float red = 0;
	private float green = 0;
	private float blue = 0;
	private float alpha = 1.0f;
	
	public RGBColor(float[] rgb) {
		if (rgb.length == 3) {
			this.red = rgb[0];
			this.green = rgb[1];
			this.blue = rgb[2];
			this.alpha = 1.0f;
		} else if (rgb.length == 4) {
			this.red = rgb[0];
			this.green = rgb[1];
			this.blue = rgb[2];
			this.alpha = rgb[3];
		}
	}

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