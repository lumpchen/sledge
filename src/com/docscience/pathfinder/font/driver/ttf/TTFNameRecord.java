package com.docscience.pathfinder.font.driver.ttf;

import java.io.IOException;

/**
 * 
 * Each NameRecord looks like this: 
 *
 * <pre>
 * Type    Name        Description
 * ----------------------------------------------------------------------------- 
 * USHORT  platformID  Platform ID. 
 * USHORT  encodingID  Platform-specific encoding ID. 
 * USHORT  languageID  Language ID. 
 * USHORT  nameID      Name ID. 
 * USHORT  length      String length (in bytes). 
 * USHORT  offset      String offset from start of storage area (in bytes).
 * </pre> 
 * 
 * @author wxin
 * @see TTFEncoding
 */
public final class TTFNameRecord {

    private int platformID;
    private int encodingID;
    private int languageID;
    private int nameID;
    private int length;
    private int offset;
    
    public final int getEncodingID() {
        return encodingID;
    }

    public final int getLanguageID() {
        return languageID;
    }

    public final int getLength() {
        return length;
    }

    public final int getNameID() {
        return nameID;
    }

    public final int getOffset() {
        return offset;
    }

    public final int getPlatformID() {
        return platformID;
    }

    public void read(TTFRandomReader rd) throws IOException {
        platformID = rd.readTTFUShort();
        encodingID = rd.readTTFUShort();
        languageID = rd.readTTFUShort();
        nameID = rd.readTTFUShort();
        length = rd.readTTFUShort();
        offset = rd.readTTFUShort();
    }

}
