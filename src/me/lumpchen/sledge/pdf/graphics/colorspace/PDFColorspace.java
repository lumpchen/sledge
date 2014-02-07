package me.lumpchen.sledge.pdf.graphics.colorspace;

import java.awt.color.ColorSpace;

import me.lumpchen.sledge.pdf.graphics.PDFColor;

public abstract class PDFColorSpace {

	static ColorSpace rgbCS = ColorSpace.getInstance(ColorSpace.CS_sRGB);

	static ColorSpace grapCS = ColorSpace.getInstance(ColorSpace.CS_GRAY);

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
