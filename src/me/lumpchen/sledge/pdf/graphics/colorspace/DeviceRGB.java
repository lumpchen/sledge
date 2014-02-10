package me.lumpchen.sledge.pdf.graphics.colorspace;

import me.lumpchen.sledge.pdf.graphics.PDFColor;
import me.lumpchen.sledge.pdf.graphics.RGBColor;

public class DeviceRGB extends PDFColorSpace {

	public DeviceRGB() {
	}

	@Override
	public PDFColor getColor(float... componenets) {
		return new RGBColor(componenets);
	}

}
