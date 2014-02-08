package me.lumpchen.sledge.pdf.graphics.colorspace;

import java.awt.color.ColorSpace;

import me.lumpchen.sledge.pdf.graphics.PDFColor;

public abstract class PDFColorSpace {

	public static final int GRAY = ColorSpace.TYPE_GRAY;
	
	public static final int CMYK = ColorSpace.TYPE_CMYK;
	
	public static final int RGB = ColorSpace.TYPE_RGB;

	public static final int XYZ = ColorSpace.TYPE_XYZ;
	
	protected static ColorSpace rgbCS = ColorSpace.getInstance(ColorSpace.CS_sRGB);

	protected static ColorSpace grapCS = ColorSpace.getInstance(ColorSpace.CS_GRAY);

	PDFColorSpace() {
	}
	
	public static PDFColorSpace instance(int cs) {
		if (cs == ColorSpace.CS_GRAY) {
			return new DeviceGray();
		}
		
		return null;
	}

	public abstract PDFColor getColor(float... componenets);
}
