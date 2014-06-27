package com.docscience.pathfinder.font.driver.otf;

import java.io.IOException;

import com.docscience.pathfinder.font.driver.ttf.TTFFormatException;
import com.docscience.pathfinder.font.driver.ttf.TTFRandomReader;

public abstract class OTFClassDefTable {
	
    public static final int INVALID_CLASS = -1;

	public abstract int getClassValue(int glyph);
	
	public abstract void read(TTFRandomReader rd) throws IOException, TTFFormatException;
	
	public static OTFClassDefTable readClassDefTable(TTFRandomReader rd) throws IOException, TTFFormatException {
		int format = rd.readTTFUShort();
		if (format == 1) {
			OTFClassDefFormat1Table cd = new OTFClassDefFormat1Table();
			cd.read(rd);
			return cd;
		} else if (format == 2) {
			OTFClassDefFormat2Table cd = new OTFClassDefFormat2Table();
			cd.read(rd);
			return cd;
		} else {
			throw new TTFFormatException("unsupported class definition format [" + format + "]");
		}
	}
	
}
