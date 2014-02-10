package me.lumpchen.sledge.pdf.graphics.colorspace;

import me.lumpchen.sledge.pdf.graphics.PDFColor;
import me.lumpchen.sledge.pdf.graphics.RGBColor;

public class DeviceGray extends PDFColorSpace {

	public DeviceGray() {
	}

	@Override
	public PDFColor getColor(float... componenets) {
		float[] rgb = grapCS.toRGB(componenets);
		return new RGBColor(rgb);
	}

}
