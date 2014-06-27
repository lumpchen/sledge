package com.docscience.pathfinder.font.driver.ttf;

import java.io.IOException;

/**
 * "PCLT" table (PCL 5).
 * 
 * Type     Name of Entry
 * ---------------------------------- 
 * FIXED    Version 
 * ULONG    FontNumber 
 * USHORT   Pitch 
 * USHORT   xHeight 
 * USHORT   Style 
 * USHORT   TypeFamily 
 * USHORT   CapHeight 
 * USHORT   SymbolSet 
 * CHAR     Typeface[16] 
 * CHAR     CharacterComplement[8] 
 * CHAR     FileName[6] 
 * CHAR     StrokeWeight 
 * CHAR     WidthType 
 * BYTE     SerifStyle 
 * BYTE     Reserved (pad) 
 * 
 * @author wxin
 */
public final class TTFPCLTTable extends TTFTable {

    private int version;
    private long fontNumber;
    private int pitch;
    private int xHeight;
    private int style;
    private int typeFamily;
    private int capHeight;
    private int symbolSet;
    private byte[] typeface = new byte[16];
    private byte[] characterComplement = new byte[8];
    private byte[] fileName = new byte[6];
    private byte strokeWeight;
    private byte widthType;
    private short serifStyle;
    
    @Override
	public void read(long offset, long length, TTFRandomReader rd) throws TTFFormatException, IOException {
        rd.setPosition(offset);
        version = rd.readTTFFixed();
        fontNumber = rd.readTTFULong();
        pitch = rd.readTTFUShort();
        xHeight = rd.readTTFUShort();
        style = rd.readTTFUShort();
        typeFamily = rd.readTTFUShort();
        capHeight = rd.readTTFUShort();
        symbolSet = rd.readTTFUShort();
        for (int i=0; i<typeface.length; ++i) {
            typeface[i] = rd.readTTFChar();
        }
        for (int i=0; i<characterComplement.length; ++i) {
            characterComplement[i] = rd.readTTFChar();
        }
        for (int i=0; i<fileName.length; ++i) {
            fileName[i] = rd.readTTFChar();
        }
        strokeWeight = rd.readTTFChar();
        widthType = rd.readTTFChar();
        serifStyle = rd.readTTFByte();
    }
    
    public final byte[] getCharacterComplement() {
        return characterComplement;
    }

    public final byte[] getFileName() {
        return fileName;
    }

    public final long getFontNumber() {
        return fontNumber;
    }

    public final int getPitch() {
        return pitch;
    }

    public final short getSerifStyle() {
        return serifStyle;
    }

    public final byte getStrokeWeight() {
        return strokeWeight;
    }

    public final int getStyle() {
        return style;
    }

    public final int getSymbolSet() {
        return symbolSet;
    }

    public final byte[] getTypeface() {
        return typeface;
    }

    public final int getTypeFamily() {
        return typeFamily;
    }

    public final int getVersion() {
        return version;
    }

    public final byte getWidthType() {
        return widthType;
    }

    public final int getXHeight() {
        return xHeight;
    }
    
    public final int getCapHeight() {
        return capHeight;
    }

    @Override
	public int getTag() {
        return TAG_PCLT_TABLE;
    }

}
