package com.docscience.pathfinder.font.driver.ttf;

import java.io.IOException;

/**
 * cmap Header
 * <pre>
 * Type    Name        Description
 * ----------------------------------------------------------
 * USHORT  version     Table version number (0).
 * USHORT  numTables   Number of encoding tables that follow.
 * </pre>
 * 
 * @author wxin
 *
 */
public final class TTFCMapTable extends TTFTable {
    
    private int version;
    private int numEncodings;
    private TTFCMapEncodingRecord[] encodings;
           
    @Override
	public int getTag() {
        return TAG_CMAP_TABLE;
    }
    
    public int getVersion() {
        return version;
    }
    
    public int getNumEncodings() {
        return numEncodings;
    }
    
    public TTFCMapEncodingRecord getEncoding(int i) {
        return encodings[i];
    }
    
    /**
     * Get a proper encoding record, by given platformID and encodingID. If 
     * encodingID is -1, it's value is ignored, just care platformID. Return 
     * null for no proper encoding record.
     * 
     * @param platformID
     * @param encodingID
     * @return
     */
    public TTFCMapEncodingRecord getEncoding(int platformID, int encodingID) {
        if (encodingID == -1) {
            for (int i=0; i < encodings.length; ++i) {
                if (encodings[i].getPlatfromID() == platformID) {
                    return encodings[i];
                }
            }
            return null;
        }
        else {
            for (int i=0; i < encodings.length; ++i) {
                if (encodings[i].getPlatfromID() == platformID 
                        && encodings[i].getEncodingID() == encodingID) {
                    return encodings[i];
                }
            }
            return null;
        }
    }

    @Override
	public void read(long offset, long length, TTFRandomReader rd) 
            throws TTFFormatException, IOException {
        rd.setPosition(offset);
        version = rd.readTTFUShort();
        numEncodings = rd.readTTFUShort();
        encodings = new TTFCMapEncodingRecord[numEncodings];
        for (int i=0; i<numEncodings; ++i) {
            encodings[i] = new TTFCMapEncodingRecord();
            encodings[i].read(rd);
        }
    }
    
}
