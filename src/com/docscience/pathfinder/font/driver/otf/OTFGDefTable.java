package com.docscience.pathfinder.font.driver.otf;

import java.io.IOException;

import com.docscience.pathfinder.font.driver.ttf.TTFFormatException;
import com.docscience.pathfinder.font.driver.ttf.TTFRandomReader;

public class OTFGDefTable extends OTFTable {

	public static final int GLYPH_CLASS_BASE = 1; // Base glyph (single character, spacing glyph) 
	public static final int GLYPH_CLASS_LIGATURE = 2; // Ligature glyph (multiple character, spacing glyph) 
	public static final int GLYPH_CLASS_MARK = 3; // Mark glyph (non-spacing combining glyph) 
	public static final int GLYPH_CLASS_COMPONENT = 4; // Component glyph (part of single character, spacing glyph) 
	
	private int version;
	
	private OTFClassDefTable glyphClassDefTable;

	private OTFClassDefTable markAttachClassDefTable;

	public int getVersion() {
		return this.version;
	}
	
	public OTFClassDefTable getGlyphClassDefTable() {
		return this.glyphClassDefTable;
	}
	
	public OTFClassDefTable getMarkAttachClassDefTable() {
		return this.markAttachClassDefTable;
	}
	
	@Override
	public int getTag() {
		return TAG_GDEF_TABLE;
	}

	@Override
	public void read(long offset, long length, TTFRandomReader rd)
			throws TTFFormatException, IOException {
		rd.setPosition(offset);
		
		this.version = rd.readTTFLong();
		int glyphClassDefOffset = rd.readTTFOffset();
		int attachListOffset = rd.readTTFOffset();
		int ligCaretListOffset = rd.readTTFOffset();
		int markAttachClassDefOffset = rd.readTTFOffset();
		int markGlyphSetsDefOffset = rd.readTTFOffset();
		
		if (glyphClassDefOffset != 0) {
			rd.setPosition(offset + glyphClassDefOffset);
			this.glyphClassDefTable = OTFClassDefTable.readClassDefTable(rd);
		}
		
		if (attachListOffset != 0) {
			// TODO ADD CODE HERE
		}
		
		if (ligCaretListOffset != 0) {
			// TODO ADD CODE HERE	
		}
		
		if (markAttachClassDefOffset != 0) {
			rd.setPosition(offset + markAttachClassDefOffset);
			this.markAttachClassDefTable = OTFClassDefTable.readClassDefTable(rd);
		}
		
		if (markGlyphSetsDefOffset != 0) {
			// TODO ADD CODE HERE	
		}
	}

}
