package com.docscience.pathfinder.font.driver.ttf;

import java.io.IOException;

/**
 * maxp - Maximum Profile 
 * This table establishes the memory requirements for this font.
 *
 * Version 0.5 
 * 
 * <pre>
 * Type   Name       Description 
 * -----------------------------------------------------------------------------
 * Fixed  Table      version number 0x00005000 for version 0.5 
 *                   (Note the difference in the representation of a non-zero 
 *                   fractional part, in Fixed numbers.)  
 * USHORT numGlyphs  The number of glyphs in the font. 
 * </pre>
 * 
 * Version 1.0
 * 
 * <pre>
 * Type   Name                  Description
 * -----------------------------------------------------------------------------
 * Fixed  Table                 version number 0x00010000 for version 1.0. 
 * USHORT numGlyphs             The number of glyphs in the font. 
 * USHORT maxPoints             Maximum points in a non-composite glyph. 
 * USHORT maxContours           Maximum contours in a non-composite glyph. 
 * USHORT maxCompositePoints    Maximum points in a composite glyph. 
 * USHORT maxCompositeContours  Maximum contours in a composite glyph. 
 * USHORT maxZones              1 if instructions do not use the twilight zone 
 *                              (Z0), or 2 if instructions do use Z0; should be 
 *                              set to 2 in most cases. 
 * USHORT maxTwilightPoints     Maximum points used in Z0. 
 * USHORT maxStorage            Number of Storage Area locations.  
 * USHORT maxFunctionDefs       Number of FDEFs. 
 * USHORT maxInstructionDefs    Number of IDEFs. 
 * USHORT maxStackElements      Maximum stack depth2. 
 * USHORT maxSizeOfInstructions Maximum byte count for glyph instructions. 
 * USHORT maxComponentElements  Maximum number of components referenced at 
 *                              "top level" for any composite glyph. 
 * USHORT maxComponentDepth     Maximum levels of recursion; 1 for simple 
 *                              components. 
 * </pre>
 * 
 * @author wxin
 *
 */
public final class TTFMaxpTable extends TTFTable {

    public static final int VERSION_0_5 = 0x00005000;
    public static final int VERSION_1_0 = 0x00010000;
    
    private int version;
    private int numGlyphs;
    private int maxPoints;
    private int maxContours;
    private int maxCompositePoints;
    private int maxCompositeContours;
    private int maxZones;
    private int maxTwilightPoints;
    private int maxStorage;
    private int maxFunctionDefs;
    private int maxInstructionDefs;
    private int maxStackElements;
    private int maxSizeOfInstructions;
    private int maxComponentElements;
    private int maxComponentDepth;
    
    public final int getMaxComponentDepth() {
        return maxComponentDepth;
    }

    public final int getMaxComponentElements() {
        return maxComponentElements;
    }

    public final int getMaxCompositeContours() {
        return maxCompositeContours;
    }

    public final int getMaxCompositePoints() {
        return maxCompositePoints;
    }

    public final int getMaxContours() {
        return maxContours;
    }

    public final int getMaxFunctionDefs() {
        return maxFunctionDefs;
    }

    public final int getMaxInstructionDefs() {
        return maxInstructionDefs;
    }

    public final int getMaxPoints() {
        return maxPoints;
    }

    public final int getMaxSizeOfInstructions() {
        return maxSizeOfInstructions;
    }

    public final int getMaxStackElements() {
        return maxStackElements;
    }

    public final int getMaxStorage() {
        return maxStorage;
    }

    public final int getMaxTwilightPoints() {
        return maxTwilightPoints;
    }

    public final int getMaxZones() {
        return maxZones;
    }

    public final int getNumGlyphs() {
        return numGlyphs;
    }

    public final int getVersion() {
        return version;
    }

    @Override
	public int getTag() {
        return TAG_MAXP_TABLE;
    }

    @Override
	public void read(long offset, long length, TTFRandomReader rd)
            throws TTFFormatException, IOException {
        rd.setPosition(offset);
        version = rd.readTTFFixed();
        if (version == VERSION_0_5) {
            numGlyphs = rd.readTTFUShort();
        }
        else if(version == VERSION_1_0) {
            numGlyphs = rd.readTTFUShort();
            maxPoints = rd.readTTFUShort();
            maxContours = rd.readTTFUShort();
            maxCompositePoints = rd.readTTFUShort();
            maxCompositeContours = rd.readTTFUShort();
            maxZones = rd.readTTFUShort();
            maxTwilightPoints = rd.readTTFUShort();
            maxStorage = rd.readTTFUShort();
            maxFunctionDefs = rd.readTTFUShort();
            maxInstructionDefs = rd.readTTFUShort();
            maxStackElements = rd.readTTFUShort();
            maxSizeOfInstructions = rd.readTTFUShort();
            maxComponentElements = rd.readTTFUShort();
            maxComponentDepth = rd.readTTFUShort();
        }
        else {
            throw new TTFFormatException("bad maxp table version number", rd.getPosition() - 4);
        }
    }

}
