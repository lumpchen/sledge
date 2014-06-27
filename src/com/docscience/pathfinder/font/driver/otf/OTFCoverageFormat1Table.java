package com.docscience.pathfinder.font.driver.otf;

import java.io.IOException;
import java.util.Arrays;

import com.docscience.pathfinder.font.driver.ttf.TTFRandomReader;

public class OTFCoverageFormat1Table extends OTFCoverageTable {
	
	private int[] glyphArray;

	public int getGlyphCount() {
		return glyphArray.length;
	}
	
	public int getGlyphID(int index) {
		return glyphArray[index];
	}
	
	@Override
    public void read(TTFRandomReader reader) throws IOException {
        int glyphCount = reader.readTTFUShort();
        this.glyphArray = new int[glyphCount];
        for (int i = 0; i < glyphCount; i++) {
            this.glyphArray[i] = reader.readTTFGlyphID();
        }
    }

	@Override
	public boolean isInCoverage(int glyphID) {
		return Arrays.binarySearch(this.glyphArray, glyphID) >= 0;
	}

	@Override
	public int getCoverageIndex(int glyphID) {
		return Arrays.binarySearch(this.glyphArray, glyphID);
	}
	
}