package com.docscience.pathfinder.font.driver.otf;

import java.io.IOException;

import com.docscience.pathfinder.font.driver.ttf.TTFFormatException;
import com.docscience.pathfinder.font.driver.ttf.TTFRandomReader;

public abstract class OTFCoverageTable {
	
	public abstract boolean isInCoverage(int glyphID);
	
	public abstract int getCoverageIndex(int glyphID);

	public abstract void read(TTFRandomReader rd) throws IOException, TTFFormatException;
	
	public static OTFCoverageTable readCoverageTable(TTFRandomReader rd) throws IOException, TTFFormatException {
		int format = rd.readTTFUShort();
		if (format == 1) {
			OTFCoverageFormat1Table ct = new OTFCoverageFormat1Table();
			ct.read(rd);
			return ct;
		} else if (format == 2) {
			OTFCoverageFormat2Table ct = new OTFCoverageFormat2Table();
			ct.read(rd);
			return ct;
		} else {
			throw new TTFFormatException("unsupported coverage table format [" + format + "]");
		}
	}

}
