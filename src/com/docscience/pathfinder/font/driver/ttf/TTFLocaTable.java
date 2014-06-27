package com.docscience.pathfinder.font.driver.ttf;

import java.io.IOException;

/**
 * 
 * loca - Index to Location
 * 
 * The indexToLoc table stores the offsets to the locations of the glyphs in the
 * font, relative to the beginning of the glyphData table. In order to compute
 * the length of the last glyph element, there is an extra entry after the last
 * valid index.
 * 
 * By definition, index zero points to the "missing character," which is the
 * character that appears if a character is not found in the font. The missing
 * character is commonly represented by a blank box (such as ) or a space. If
 * the font does not contain an outline for the missing character, then the
 * first and second offsets should have the same value. This also applies to any
 * other character without an outline, such as the space character.
 * 
 * Most routines will look at the 'maxp' table to determine the number of glyphs
 * in the font, but the value in the 'loca' table should agree.
 * 
 * There are two versions of this table, the short and the long. The version is
 * specified in the indexToLocFormat entry in the 'head' table.
 * 
 * 
 * Short version
 * 
 * <pre>
 * Type   Name       Description
 * ----------------------------------------------------------------------------- 
 * USHORT offsets[n] The actual local offset divided by 2 is stored. The value 
 *                   of n is numGlyphs + 1. The value for numGlyphs is found in 
 *                   the 'maxp' table.
 * </pre>
 *  
 * Long version
 * 
 * <pre>
 * Type   Name       Description 
 * -----------------------------------------------------------------------------
 * ULONG  offsets[n] The actual local offset is stored. The value of n is 
 *                   numGlyphs + 1. The value for numGlyphs is found in the 
 *                   'maxp' table.
 * </pre>
 * 
 * Note that the local offsets should be long-aligned, i.e., multiples of 4.
 * Offsets which are not long-aligned may seriously degrade performance of some
 * processors.
 * 
 * 
 * @author wxin
 * 
 */
public final class TTFLocaTable extends TTFTable {

    public static final int SHORT_FORMAT = 0;
    public static final int LONG_FORMAT  = 1;
    
    private int format;
    private long[] offsets;
    
    /**
     * @param indexToLocFormat same as TTFHeadTable.indexToLocFormat
     * @param numGlyphs same as TTFMaxpTable.numGlyphs
     */
    public TTFLocaTable(int indexToLocFormat, int numGlyphs) {
        assert(indexToLocFormat == 0 || indexToLocFormat == 1);
        format = indexToLocFormat;
        offsets = new long[numGlyphs+1];
    }
    
    public int getFormat() {
        return format;
    }
    
    public int getNumGlyphs() {
        return offsets.length;
    }
    
    public long getGlyphOffset(int glyphIndex) {
        return offsets[glyphIndex];
    }
    
    @Override
	public int getTag() {
        return TAG_LOCA_TABLE;
    }

    @Override
	public void read(long offset, long length, TTFRandomReader rd)
            throws TTFFormatException, IOException {
        rd.setPosition(offset);
        if (format == SHORT_FORMAT) {
            for (int i=0; i<offsets.length; ++i) {
                offsets[i] = rd.readTTFUShort() * 2;
            }
        }
        else {
            for (int i=0; i<offsets.length; ++i) {
                offsets[i] = rd.readTTFULong();
            }
        }
    }

}
