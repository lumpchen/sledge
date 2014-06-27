package com.docscience.pathfinder.font.driver.ttf;

import java.io.IOException;

/**
 * "OS/2" table (OS/2 and windows metrics).
 * 
 * This class is extract from orginal TTFFile class.
 * 
 * @author wxin
 */
public final class TTFOS_2Table extends TTFTable {

    public static final int FS_ITALIC = 1 << 0; // Font contains Italic characters, otherwise they are upright. 
    public static final int FS_UNDERSCORE = 1 << 1; // Characters are underscored. 
    public static final int FS_NEGATIVE = 1 << 2; // Characters have their foreground and background reversed. 
    public static final int FS_OUTLINED = 1 << 3; // Outline (hollow) characters, otherwise they are solid. 
    public static final int FS_STRIKEOUT = 1 << 4; // Characters are overstruck. 
    public static final int FS_BOLD = 1 << 5; // Characters are emboldened. 
    public static final int FS_REGULAR = 1 << 6;

    private int version;

    private short xAvgCharWidth;

    private int usWeightClass;

    private int usWidthClass;

    private int fsType;

    private short ySubscriptXSize;

    private short ySubscriptYSize;

    private short ySubscriptXOffset;

    private short ySubscriptYOffset;

    private short ySuperscriptXSize;

    private short ySuperscriptYSize;

    private short ySuperscriptXOffset;

    private short ySuperscriptYOffset;

    private short yStrikeoutSize;

    private short yStrikeoutPosition;

    private short sFamilyClass;

    private byte[] panose = new byte[10];

    private int uIUnicodeRange1;

    private int uIUnicodeRange2;

    private int uIUnicodeRange3;

    private int uIUnicodeRange4;

    private String achVendorID; // 4-byte character array.

    private short fsSelection;

    private int usFirstCharIndex;

    private int usLastCharIndex;

    private short sTypoAscender;

    private short sTypoDescender;

    private short sTypoLineGap;

    private int usWinAscent;

    private int usWinDescent;

    private int uICodePageRange1;

    private int uICodePageRange2;
    
    private short sxHeight;
    
    private short sCapHeight;
    
    private int usDefaultChar;
    
    private int usBreakChar;
    
    private int usMaxContext;

    
    public final String getAchVendorID() {
        return achVendorID;
    }

    public final short getFsSelection() {
        return fsSelection;
    }

    public final int getFsType() {
        return fsType;
    }

    public final byte[] getPanose() {
        return panose;
    }

    public final short getSCapHeight() {
        return sCapHeight;
    }

    public final short getSFamilyClass() {
        return sFamilyClass;
    }

    public final short getSTypoAscender() {
        return sTypoAscender;
    }

    public final short getSTypoDescender() {
        return sTypoDescender;
    }

    public final short getSTypoLineGap() {
        return sTypoLineGap;
    }

    public final short getSxHeight() {
        return sxHeight;
    }

    public final int getUICodePageRange1() {
        return uICodePageRange1;
    }

    public final int getUICodePageRange2() {
        return uICodePageRange2;
    }

    public final int getUIUnicodeRange1() {
        return uIUnicodeRange1;
    }

    public final int getUIUnicodeRange2() {
        return uIUnicodeRange2;
    }

    public final int getUIUnicodeRange3() {
        return uIUnicodeRange3;
    }

    public final int getUIUnicodeRange4() {
        return uIUnicodeRange4;
    }

    public final int getUsBreakChar() {
        return usBreakChar;
    }

    public final int getUsDefaultChar() {
        return usDefaultChar;
    }

    public final int getUsFirstCharIndex() {
        return usFirstCharIndex;
    }

    public final int getUsLastCharIndex() {
        return usLastCharIndex;
    }

    public final int getUsMaxContext() {
        return usMaxContext;
    }

    public final int getUsWeightClass() {
        return usWeightClass;
    }

    public final int getUsWidthClass() {
        return usWidthClass;
    }

    public final int getUsWinAscent() {
        return usWinAscent;
    }

    public final int getUsWinDescent() {
        return usWinDescent;
    }

    public final int getVersion() {
        return version;
    }

    public final short getXAvgCharWidth() {
        return xAvgCharWidth;
    }

    public final short getYStrikeoutPosition() {
        return yStrikeoutPosition;
    }

    public final short getYStrikeoutSize() {
        return yStrikeoutSize;
    }

    public final short getYSubscriptXOffset() {
        return ySubscriptXOffset;
    }

    public final short getYSubscriptXSize() {
        return ySubscriptXSize;
    }

    public final short getYSubscriptYOffset() {
        return ySubscriptYOffset;
    }

    public final short getYSubscriptYSize() {
        return ySubscriptYSize;
    }

    public final short getYSuperscriptXOffset() {
        return ySuperscriptXOffset;
    }

    public final short getYSuperscriptXSize() {
        return ySuperscriptXSize;
    }

    public final short getYSuperscriptYOffset() {
        return ySuperscriptYOffset;
    }

    public final short getYSuperscriptYSize() {
        return ySuperscriptYSize;
    }
    
    public final boolean isFsSelectionSetted(int flag) {
        return (fsSelection & flag) != 0;
    }

    @Override
	public int getTag() {
        return TAG_OS_2_TABLE;
    }

    @Override
	public void read(long offset, long length, TTFRandomReader rd) throws TTFFormatException, IOException {
        rd.setPosition(offset);
        this.version = rd.readTTFUShort();

        this.xAvgCharWidth = rd.readTTFShort();
        this.usWeightClass = rd.readTTFUShort();
        this.usWidthClass = rd.readTTFUShort();
        this.fsType = rd.readTTFUShort();

        this.ySubscriptXSize = rd.readTTFShort();
        this.ySubscriptYSize = rd.readTTFShort();
        this.ySubscriptXOffset = rd.readTTFShort();
        this.ySubscriptYOffset = rd.readTTFShort();

        this.ySuperscriptXSize = rd.readTTFShort();
        this.ySuperscriptYSize = rd.readTTFShort();
        this.ySuperscriptXOffset = rd.readTTFShort();
        this.ySuperscriptYOffset = rd.readTTFShort();

        this.yStrikeoutSize = rd.readTTFShort();
        this.yStrikeoutPosition = rd.readTTFShort();

        this.sFamilyClass = rd.readTTFShort();

        // Read panose.
        for (int i = 0; i < 10; i++) {
            this.panose[i] = rd.readTTFChar();
        }

        this.uIUnicodeRange1 = rd.readTTFLong();
        this.uIUnicodeRange2 = rd.readTTFLong();
        this.uIUnicodeRange3 = rd.readTTFLong();
        this.uIUnicodeRange4 = rd.readTTFLong();

        this.achVendorID = TTFHelper.decomposeTag(rd.readTTFTag());
        this.fsSelection = rd.readTTFShort();

        this.usFirstCharIndex = rd.readTTFUShort();
        this.usLastCharIndex = rd.readTTFUShort();
        this.sTypoAscender = rd.readTTFShort();
        this.sTypoDescender = rd.readTTFShort();
        this.sTypoLineGap = rd.readTTFShort();
        this.usWinAscent = rd.readTTFUShort();
        this.usWinDescent = rd.readTTFUShort();
        
        if (this.version >= 1) {
            this.uICodePageRange1 = rd.readTTFLong();
            this.uICodePageRange2 = rd.readTTFLong();
        }
        
        if (this.version >= 2) {
        	this.sxHeight = rd.readTTFShort();
        	this.sCapHeight = rd.readTTFShort();
        	this.usDefaultChar = rd.readTTFUShort();
        	this.usBreakChar = rd.readTTFUShort();
        	this.usMaxContext = rd.readTTFUShort();
        }
    }

    @Override
	public String toString() {
        String ret = "OS/2 table:  [\n";

        ret += "version = " + this.version + ";\n";

        ret += "vendor = " + this.achVendorID + "\n";

        ret += "weight class = " + this.usWeightClass + ";  ";
        ret += "width class = " + this.usWidthClass + ";\n";

        ret += "Subscript (XOffset, Yoffset, xSize, ySize): ";
        ret += this.ySubscriptXOffset + ", ";
        ret += this.ySubscriptYOffset + ", ";
        ret += this.ySubscriptXSize + ", ";
        ret += this.ySubscriptYSize + ";\n";

        ret += "Superscript (XOffset, Yoffset, xSize, ySize): ";
        ret += this.ySuperscriptXOffset + ", ";
        ret += this.ySuperscriptYOffset + ", ";
        ret += this.ySuperscriptXSize + ", ";
        ret += this.ySuperscriptYSize + ";\n";

        ret += "Strikeout (position, size): ";
        ret += this.yStrikeoutPosition + ", " + this.yStrikeoutSize + ";\n";

        ret += "panose = [ ";
        for (int i = 0; i < 10; i++) {
            ret += this.panose[i] + " ";
        }
        ret += "]\n";

        ret += "Unicode range: ";
        ret += Integer.toBinaryString(this.uIUnicodeRange1);
        ret += Integer.toBinaryString(this.uIUnicodeRange2);
        ret += Integer.toBinaryString(this.uIUnicodeRange3);
        ret += Integer.toBinaryString(this.uIUnicodeRange4);
        ret += "\n";

        ret += "selection flag: "
                + Integer.toBinaryString(this.fsSelection) + "\n";

        ret += "fistCharIndex = " + this.usFirstCharIndex + ", ";
        ret += "lastCharIndex = " + this.usLastCharIndex + "\n";

        if (this.version == 1) {
            ret += "Typographic: ascender = " + this.sTypoAscender + ", ";
            ret += "descender = " + this.sTypoDescender + ", ";
            ret += "line gap = " + this.sTypoLineGap + "\n";

            ret += "win ascender = " + this.usWinAscent + ", ";
            ret += "win descender = " + this.usWinDescent + "\n";

            ret += "code page range: ";
            ret += Integer.toBinaryString(this.uICodePageRange1);
            ret += Integer.toBinaryString(this.uICodePageRange2);
            ret += "\n";
        }

        ret += "]\n";

        return ret;
    }

}