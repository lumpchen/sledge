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

