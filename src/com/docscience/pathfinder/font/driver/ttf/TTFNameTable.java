package com.docscience.pathfinder.font.driver.ttf;

import java.io.IOException;

/**
 * The Naming Table is organized as follows: 
 * 
 * <pre>
 * Type        Name               Description
 * ----------------------------------------------------------------------------- 
 * USHORT      format             Format selector (=0).
 * USHORT      count              Number of name records. 
 * USHORT      stringOffset       Offset to start of string storage (from start 
 *                                of table). 
 * NameRecord  nameRecord[count]  The name records where count is the number of 
 *                                records. 
 * (Variable)                     Storage for the actual string data. 
 * </pre>
 * 
 * @author wxin
 *
 */
public final class TTFNameTable extends TTFTable {

    private TTFNameRecord[] nameRecords;
    private int stringOffset;
    
    @Override
	public int getTag() {
        return TAG_NAME_TABLE;
    }
    
    public int getStringOffset() {
        return stringOffset;
    }
    
    public int getNumNameRecords() {
        return nameRecords.length;
    }
    
    public TTFNameRecord getNameRecord(int i) {
        return nameRecords[i];
    }
    
    @Override
	public void read(long offset, long length, TTFRandomReader rd)
            throws TTFFormatException, IOException {
        rd.setPosition(offset);
        if (rd.readTTFUShort() != 0)
            throw new TTFFormatException("bad name table format, should be zero", rd.getPosition() - 2);
        int n = rd.readTTFUShort();
        stringOffset = rd.readTTFUShort();
        nameRecords = new TTFNameRecord[n];
        for (int i=0; i<n; ++i) {
            nameRecords[i] = new TTFNameRecord();
            nameRecords[i].read(rd);
        }
    }

}
