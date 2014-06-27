package com.docscience.pathfinder.font.driver.ttf;

import java.io.IOException;

/**
 * Encoding Record
 * 
 * <pre>
 * Type    Name        Description
 * ----------------------------------------------------------
 * USHORT  platformID  Platform ID.
 * USHORT  encodingID  Platform-specific encoding ID.
 * ULONG   offset      Byte offset from beginning of table to the
 *                     subtable for this encoding.
 * </pre>
 * 
 * @author wxin
 * 
 */
public final class TTFCMapEncodingRecord {
    
    private int  platformID;
    private int  encodingID;
    private long offset;

    public int getPlatfromID() {
        return platformID;
    }
    
    public int getEncodingID() {
        return encodingID;
    }
    
    public long getOffset() {
        return offset;
    }
    
    public void read(TTFRandomReader rd) throws IllegalArgumentException, IOException {
        platformID = rd.readTTFUShort();
        encodingID = rd.readTTFUShort();
        offset = rd.readTTFULong();
    }

}
