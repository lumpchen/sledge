package com.docscience.pathfinder.font.driver.ttf;

import java.io.IOException;

/**
 * hmtx - Horizontal Metrics 
 * 
 * @author wxin
 *
 */
public final class TTFHmtxTable extends TTFTable {
   
    private short[] advanceWidths;
    private short[] leftSideBearings;
    
    /**
     * @param numberOfHMetrics same as TTFHheaTable.numberOfHMetrics
     * @param numGlyphs same as TTFMaxpTable.numGlyphs
     */
    public TTFHmtxTable(int numberOfHMetrics, int numGlyphs) {
        assert(numGlyphs >= numberOfHMetrics);
        advanceWidths = new short[numberOfHMetrics];
        leftSideBearings = new short[numGlyphs];
    }
    
    public int getNumberOfHMetrics() {
        return advanceWidths.length;
    }
    
    public int getNumberOfGlyphs() {
        return leftSideBearings.length;
    }
    
    public int getAdvanceWidth(int index) {
        if (index >= advanceWidths.length) {
            return advanceWidths[advanceWidths.length - 1] & 0xffff;
        }
        return advanceWidths[index] & 0xffff;
    }
    
    public short[] getAdvanceWidths() {
        return (short[]) advanceWidths.clone();
    }
    
    public int getLeftSideBearing(int index) {
        return leftSideBearings[index];
    }
    
	public short[] getLeftSideBearings() {
		return leftSideBearings.clone();
	}
    
    @Override
	public int getTag() {
        return TAG_HMTX_TABLE;
    }

    @Override
	public void read(long offset, long length, TTFRandomReader rd)
            throws TTFFormatException, IOException {
        rd.setPosition(offset);
        int i=0;
        for ( ; i<advanceWidths.length; ++i) {
            advanceWidths[i] = (short) rd.readTTFUShort();
            leftSideBearings[i] = rd.readTTFShort();
        }
        for ( ; i<leftSideBearings.length; ++i) {
            leftSideBearings[i] = rd.readTTFShort();
        }
    }

}
