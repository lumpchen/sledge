package com.docscience.pathfinder.font.driver.ttf;

import java.io.IOException;

/**
 * head - Font Header 
 * This table gives global information about the font.
 * 
 * <pre>
 * Type          Name                Description 
 * Fixed         version             version number 0x00010000 for version 1.0. 
 * Fixed         fontRevision        Set by font manufacturer. 
 * ULONG         checkSumAdjustment  To compute: set it to 0, sum the entire 
 *                                   font as ULONG, then store 0xB1B0AFBA - sum. 
 * ULONG         magicNumber         Set to 0x5F0F3CF5. 
 * USHORT        flags               Bit 0: Baseline for font at y=0;
 *                                   Bit 1: Left sidebearing point at x=0;
 *                                   Bit 2: Instructions may depend on point size; 
 *                                   Bit 3: Force ppem to integer values for all
 *                                          internal scaler math; may use 
 *                                          fractional ppem sizes if this bit is
 *                                          clear; 
 *                                   Bit 4: Instructions may alter advance width
 *                                          (the advance widths might not scale 
 *                                          linearly); 
 *                                   Bits 5-10: These should be set according to
 *                                          Apple's specification . However, 
 *                                          they are not implemented in OpenType. 
 *                                   Bit 11: Font data is 'lossless,' as a 
 *                                           result of having been compressed 
 *                                           and decompressed with the Agfa 
 *                                           MicroType Express engine.
 *                                   Bit 12: Font converted (produce compatible 
 *                                           metrics)
 *                                   Bit 13: Font optimised for ClearType
 *                                   Bit 14: Reserved, set to 0
 *                                   Bit 15: Reserved, set to 0 
 * USHORT        unitsPerEm          Valid range is from 16 to 16384. This value
 *                                   should be a power of 2 for fonts that have 
 *                                   TrueType outlines. 
 * LONGDATETIME  created             Number of seconds since 12:00 midnight, 
 *                                   January 1, 1904. 64-bit integer 
 * LONGDATETIME  modified            Number of seconds since 12:00 midnight, 
 *                                   January 1, 1904. 64-bit integer 
 * SHORT         xMin                For all glyph bounding boxes. 
 * SHORT         yMin                For all glyph bounding boxes. 
 * SHORT         xMax                For all glyph bounding boxes. 
 * SHORT         yMax                For all glyph bounding boxes. 
 * USHORT        macStyle            Bit 0: Bold (if set to 1); 
 *                                   Bit 1: Italic (if set to 1) 
 *                                   Bit 2: Underline (if set to 1) 
 *                                   Bit 3: Outline (if set to 1) 
 *                                   Bit 4: Shadow (if set to 1) 
 *                                   Bit 5: Condensed (if set to 1) 
 *                                   Bit 6: Extended (if set to 1) 
 *                                   Bits 7-15: Reserved (set to 0).  
 * USHORT        lowestRecPPEM       Smallest readable size in pixels. 
 * SHORT         fontDirectionHint    0: Fully mixed directional glyphs; 
 *                                    1: Only strongly left to right; 
 *                                    2: Like 1 but also contains neutrals; 
 *                                   -1: Only strongly right to left; 
 *                                   -2: Like -1 but also contains neutrals. 
 * SHORT         indexToLocFormat    0 for short offsets, 1 for long. 
 * SHORT         glyphDataFormat     0 for current format. 
 * </pre>
 * 
 * 
 * @author wxin
 *
 */
public final class TTFHeadTable extends TTFTable {

    public static final int MACSTYLE_BOLD = 1 << 0;
    public static final int MACSTYLE_ITALIC = 1 << 1;
    public static final int MACSTYLE_UNDERLINE = 1 << 2;
    public static final int MACSTYLE_OUTLINE = 1 << 3;
    public static final int MACSTYLE_SHADOW = 1 << 4;
    public static final int MACSTYLE_CONDENSED = 1 << 5;
    public static final int MACSTYLE_EXTENDED = 1 << 6;
    
    public static final long MAGIC_NUMBER = 0x5F0F3CF5;
    
    private int revision;
    private int flags;
    private int unitsPerEm;
    private long created;
    private long modified;
    private short xMin;
    private short yMin;
    private short xMax;
    private short yMax;
    private int macStyle;
    private int lowestRecPPEM;
    private short fontDirectionHint;
    private short indexToLocFormat;
    private short glyphDataFormat;
    
    public final long getCreated() {
        return created;
    }

    public final int getFlags() {
        return flags;
    }

    public final short getFontDirectionHint() {
        return fontDirectionHint;
    }

    public final short getGlyphDataFormat() {
        return glyphDataFormat;
    }

    public final short getIndexToLocFormat() {
        return indexToLocFormat;
    }

    public final int getLowestRecPPEM() {
        return lowestRecPPEM;
    }

    public final int getMacStyle() {
        return macStyle;
    }

    public final long getModified() {
        return modified;
    }

    public final int getRevision() {
        return revision;
    }

    public final int getUnitsPerEm() {
        return unitsPerEm;
    }

    public final short getXMax() {
        return xMax;
    }

    public final short getXMin() {
        return xMin;
    }

    public final short getYMax() {
        return yMax;
    }

    public final short getYMin() {
        return yMin;
    }
    
    public final boolean isMacStyleSetted(int macStyle) {
        return (this.macStyle & macStyle) != 0;
    }

    @Override
	public int getTag() {
        return TAG_HEAD_TABLE;
    }

    @Override
	public void read(long offset, long length, TTFRandomReader rd)
            throws TTFFormatException, IOException {
        rd.setPosition(offset);
        if (rd.readTTFFixed() != 0x00010000) {
            throw new TTFFormatException("bad head table version", rd.getPosition() - 4);
        }
        revision = rd.readTTFFixed();
        rd.readTTFULong(); // read check sum;
        if (rd.readTTFULong() != MAGIC_NUMBER) {
            throw new TTFFormatException("bad magic number in head table", rd.getPosition() - 4);
        }
        flags = rd.readTTFUShort();
        unitsPerEm = rd.readTTFUShort();
        created = rd.readTTFLongDateTime();
        modified = rd.readTTFLongDateTime();
        xMin = rd.readTTFShort();
        yMin = rd.readTTFShort();
        xMax = rd.readTTFShort();
        yMax = rd.readTTFShort();
        macStyle = rd.readTTFUShort();
        lowestRecPPEM = rd.readTTFUShort();
        fontDirectionHint = rd.readTTFShort();
        indexToLocFormat = rd.readTTFShort();
        if (indexToLocFormat != 0 && indexToLocFormat != 1) {
            throw new TTFFormatException("bad indexToLocFormat in head table", rd.getPosition() - 2);
        }
        glyphDataFormat = rd.readTTFShort();
    }

}
