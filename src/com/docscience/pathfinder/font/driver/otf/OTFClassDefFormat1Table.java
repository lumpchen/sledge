package com.docscience.pathfinder.font.driver.otf;

import java.io.IOException;

import com.docscience.pathfinder.font.driver.ttf.TTFRandomReader;

public class OTFClassDefFormat1Table extends OTFClassDefTable {
	
	private int startGlyph;

    private int[] classValues;

    public int getStartGlyph() {
    	return startGlyph;
    }
    
    public int getGlyphCount() {
    	return classValues.length;
    }
    
    public int[] getClassValues() {
    	return classValues;
    }
    
    @Override
    public void read(TTFRandomReader reader) throws IOException {
        this.startGlyph = reader.readTTFGlyphID();
        int glyphCount = reader.readTTFUShort();
        this.classValues = new int[glyphCount];
        for (int i = 0; i < glyphCount; i++) {
            this.classValues[i] = reader.readTTFUShort();
        }
    }

	@Override
	public int getClassValue(int glyph) {
		if (glyph < startGlyph || glyph >= startGlyph + classValues.length) {
			return INVALID_CLASS;
		}
		return classValues[glyph - startGlyph];
	}
    
}