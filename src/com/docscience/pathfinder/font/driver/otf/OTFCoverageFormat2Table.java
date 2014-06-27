package com.docscience.pathfinder.font.driver.otf;

import java.io.IOException;

import com.docscience.pathfinder.font.driver.ttf.TTFRandomReader;

public class OTFCoverageFormat2Table extends OTFCoverageTable {
    
	private int rangeCount;

    // Range records.
    private int[] start;

    private int[] end;

    private int[] startCoverageIndex;

    @Override
	public void read(TTFRandomReader reader) throws IOException {
        this.rangeCount = reader.readTTFUShort();

        this.start = new int[this.rangeCount];
        this.end = new int[this.rangeCount];
        this.startCoverageIndex = new int[this.rangeCount];
        for (int i = 0; i < this.rangeCount; i++) {
            this.start[i] = reader.readTTFGlyphID();
            this.end[i] = reader.readTTFGlyphID();
            this.startCoverageIndex[i] = reader.readTTFUShort();
        }
    }

	@Override
	public boolean isInCoverage(int glyphID) {
		for (int i = 0; i < this.rangeCount; i++) {
			if (glyphID >= this.start[i] && glyphID <= this.end[i]) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getCoverageIndex(int glyphID) {
		for (int i = 0; i < this.rangeCount; i++) {
			if (glyphID >= this.start[i] && glyphID <= this.end[i]) {
				return glyphID - this.start[i] + this.startCoverageIndex[i];
			}
		}
		return -1;
	}
}