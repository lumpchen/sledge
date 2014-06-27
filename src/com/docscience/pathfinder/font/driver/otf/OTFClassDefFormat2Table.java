package com.docscience.pathfinder.font.driver.otf;

import java.io.IOException;

import com.docscience.pathfinder.font.driver.ttf.TTFRandomReader;

public class OTFClassDefFormat2Table extends OTFClassDefTable {
	
    private int[] start;

    private int[] end;

    private int[] classValue;

    @Override
    public void read(TTFRandomReader reader) throws IOException {
        int classRangeCount = reader.readTTFUShort();

        this.start = new int[classRangeCount];
        this.end = new int[classRangeCount];
        this.classValue = new int[classRangeCount];
        for (int i = 0; i < classRangeCount; i++) {
            this.start[i] = reader.readTTFGlyphID();
            this.end[i] = reader.readTTFGlyphID();
            this.classValue[i] = reader.readTTFUShort();
        }
    }

	@Override
	public int getClassValue(int glyph) {
		for (int i=0; i<this.start.length; ++i) {
			if (glyph >= this.start[i] && glyph <= this.end[i]) {
				return this.classValue[i];
			}
		}
		return INVALID_CLASS;
	}
}