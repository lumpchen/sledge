package me.lumpchen.sledge.pdf.graphics;

public class CMYKColor extends PDFColor {

	private float cyan;
	private float megenta;
	private float yellow;
	private float black;

	public CMYKColor(float c, float m, float y, float k) {
		this.cyan = c;
		this.megenta = m;
		this.yellow = y;
		this.black = k;
	}

	@Override
	public java.awt.Color toJavaColor() {
		return java.awt.Color.magenta;
	}
}
