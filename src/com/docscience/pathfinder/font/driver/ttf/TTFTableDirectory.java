package com.docscience.pathfinder.font.driver.ttf;

import java.io.IOException;

/**
 * Table Directory
 * 
 * <pre>
 * Type   Name      Description
 * -------------------------------------------------------------
 * ULONG  tag       4 -byte identifier.
 * ULONG  checkSum  CheckSum for this table.
 * ULONG  offset    Offset from beginning of TrueType font file.
 * ULONG  length    Length of this table.
 * </pre>
 * 
 * @author wxin
 *
 */
public final class TTFTableDirectory {
    
    private int tag;
    private long checkSum;
    private long offset;
    private long length;
    
    public int getTag() {
        return tag;
    }
    
    public long getCheckSum() {
        return checkSum;
    }

    public long getOffset() {
        return offset;
    }

    public long getLength() {
        return length;
    }

    public void read(TTFRandomReader rd) throws TTFFormatException, IOException {
        tag = rd.readTTFTag();
        checkSum = rd.readTTFULong();
        offset = rd.readTTFULong();
        length = rd.readTTFULong();
    }

    @Override
    public String toString() {
        return "TTFTableDirectory [checkSum=" + checkSum + ", length=" + length
                + ", offset=" + offset + ", tag=" + TTFHelper.decomposeTag(tag) + "]";
    }

        
}
