package com.docscience.pathfinder.font.driver.ttf;

import java.io.IOException;

/**
 * hhea - Horizontal Header 
 * This table contains information for horizontal layout.
 * 
 * <pre>
 * Type    Name                Description
 * ----------------------------------------------------------------------------- 
 * Fixed   Table               version number 0x00010000 for version 1.0. 
 * FWORD   Ascender            Typographic ascent. (Distance from baseline of highest ascender)  
 * FWORD   Descender           Typographic descent. (Distance from baseline of lowest descender)  
 * FWORD   LineGap             Typographic line gap. Negative LineGap values are treated as zero 
 *                             in Windows 3.1, System 6, and System 7. 
 * UFWORD  advanceWidthMax     Maximum advance width value in 'hmtx' table. 
 * FWORD   minLeftSideBearing  Minimum left sidebearing value in 'hmtx' table. 
 * FWORD   minRightSideBearing Minimum right sidebearing value; calculated as Min(aw - lsb - (xMax - xMin)). 
 * FWORD   xMaxExtent          Max(lsb + (xMax - xMin)). 
 * SHORT   caretSlopeRise      Used to calculate the slope of the cursor (rise/run); 1 for vertical. 
 * SHORT   caretSlopeRun       0 for vertical. 
 * SHORT   caretOffset         The amount by which a slanted highlight on a glyph needs to be shifted to produce the best appearance. Set to 0 for non-slanted fonts 
 * SHORT   (reserved)          set to 0 
 * SHORT   (reserved)          set to 0 
 * SHORT   (reserved)          set to 0 
 * SHORT   (reserved)          set to 0 
 * SHORT   metricDataFormat    0 for current format. 
 * USHORT  numberOfHMetrics    Number of hMetric entries in 'hmtx' table
 * </pre>
 * 
 * 
 * @author wxin
 *
 */
public final class TTFHheaTable extends TTFTable {

    private short ascender;
    private short descender;
    private short lineGap;
    private int advanceWidthMax;
    private short minLeftSideBearing;
    private short minRightSideBearing;
    private short xMaxExtent;
    private short caretSlopeRise;
    private short caretSlopeRun;
    private short caretOffset;
    private short metricDataFormat;
    private int numberOfHMetrics;
    
    public final int getAdvanceWidthMax() {
        return advanceWidthMax;
    }

    public final short getAscender() {
        return ascender;
    }

    public final short getCaretOffset() {
        return caretOffset;
    }

    public final short getCaretSlopeRise() {
        return caretSlopeRise;
    }

    public final short getCaretSlopeRun() {
        return caretSlopeRun;
    }

    public final short getDescender() {
        return descender;
    }

    public final short getLineGap() {
        return lineGap;
    }

    public final short getMetricDataFormat() {
        return metricDataFormat;
    }

    public final short getMinLeftSideBearing() {
        return minLeftSideBearing;
    }

    public final short getMinRightSideBearing() {
        return minRightSideBearing;
    }

    public final int getNumberOfHMetrics() {
        return numberOfHMetrics;
    }

    public final short getXMaxExtent() {
        return xMaxExtent;
    }

    @Override
	public int getTag() {
        return TAG_HHEA_TABLE;
    }

    @Override
	public void read(long offset, long length, TTFRandomReader rd)
            throws TTFFormatException, IOException {
        rd.setPosition(offset);
        if (rd.readTTFFixed() != 0x00010000) {
            throw new TTFFormatException("bad hhea table version number", rd.getPosition() - 4);
        }
        ascender = rd.readTTFFWord();
        descender = rd.readTTFFWord();
        lineGap = rd.readTTFFWord();
        advanceWidthMax = rd.readTTFUFWord();
        minLeftSideBearing = rd.readTTFFWord();
        minRightSideBearing = rd.readTTFFWord();
        xMaxExtent = rd.readTTFFWord();
        caretSlopeRise = rd.readTTFShort();
        caretSlopeRun = rd.readTTFShort();
        caretOffset = rd.readTTFShort();
        rd.readTTFShort();
        rd.readTTFShort();
        rd.readTTFShort();
        rd.readTTFShort();
        metricDataFormat = rd.readTTFShort();
        numberOfHMetrics = rd.readTTFUShort();
    }

}
