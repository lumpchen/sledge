package com.docscience.pathfinder.font.driver.ttf;

import java.io.IOException;

/**
 * @author wxin
 *
 */
public interface TTFRandomReader {
    
    /**
     * Read bytes.
     * @param bytes
     * @throws IOException
     */
    public void readBytes(byte[] bytes) throws IOException;
    
    /**
     * Read bytes.
     * @param bytes
     * @param offset
     * @param length
     * @throws IOException
     */
    public void readBytes(byte[] bytes, int offset, int length) throws IOException;
    
    /**
     * Read a TTF 'BYTE' data type. 'BYTE' is 8-bit unsigned integer. 
     * @return 8-bit unsigned integer.
     * @throws IOException
     */
    public short readTTFByte() throws IOException;
    
    /**
     * Read a TTF 'CHAR' data type. 'CHAR' is 8-bit signed integer.
     * @return 8-bit signed integer.
     * @throws IOException
     */
    public byte readTTFChar() throws IOException;

    /**
     * Read a TTF 'USHORT' data type. 'USHORT' is 16-bit unsigned integer.
     * @return 16-bit unsigned integer.
     * @throws IOException
     */
    public int readTTFUShort() throws IOException;
    
    /**
     * Read a TTF 'SHORT' data type. 'SHORT' is 16-bit signed integer.
     * @return 16-bit signed integer.
     * @throws IOException
     */
    public short readTTFShort() throws IOException;
    
    /**
     * Read a TTF 'ULONG' data type. 'ULONG' is 32-bit unsigned integer.
     * @return 32-bit unsigned integer.
     * @throws IOException
     */
    public long readTTFULong() throws IOException;
    
    /**
     * Read a TTF 'LONG' data type. 'LONG' is 32-bit signed integer.
     * @return 32-bit signed integer.
     * @throws IOException
     */
    public int readTTFLong() throws IOException;
    
    /**
     * Read a TTF 'Fixed' data type. 'Fixed' is 32-bit signed fixed-point number (16.16).
     * @return 32-bit signed fixed-point number (16.16).
     * @throws IOException
     */
    public int readTTFFixed() throws IOException;
    
    /**
     * Read a TTF 'FUNIT' data type. 'FUNIT' is smallest measurable distance in the em space.
     * @return short
     * @throws IOException
     */
    public short readTTFFUnit() throws IOException;
    
    /**
     * Read a TTF 'FWORD' data type. 'FWORD' is 16-bit signed integer (SHORT) that describes a quantity in FUnits.
     * @return 16-bit signed integer.
     * @throws IOException
     */
    public short readTTFFWord() throws IOException;
    
    /**
     * Read a TTF 'UFWORD' data type. 'UFWORD' is 16-bit unsigned integer (USHORT) that describes a quantity in FUnits.
     * @return 16-bit unsigned integer
     * @throws IOException
     */
    public int readTTFUFWord() throws IOException;
   
    /**
     * Read a TTF 'F2DOT14' data type. 'F2DOT14' is 16-bit signed fixed number with the low 14 bits of fraction (2.14).
     * @return 16-bit signed fixed with (2.14).
     * @throws IOException
     */
    public short readTTFF2Dot14() throws IOException;
    
    /**
     * Read a TTF 'LONGDATETIME' data type. 'LONGDATETIME' represented in number of seconds since 12:00 midnight, January 1, 1904. The value is represented as a signed 64-bit integer.
     * @return signed 64-bit integer.
     * @throws IOException
     */
    public long readTTFLongDateTime() throws IOException;
    
    /**
     * Read a TTF 'Tag' data type. 'Tag' is array of four uint8s (length = 32 bits) used to identify a script, language system, feature, or baseline.
     * @param tag a array of four integer or null.
     * @return array of four unsigned 8-bits integer.
     * @throws IOException
     * @throws IllegalArgumentException if tag is neither null nor a array with at least 4 integer.
     */
    public int readTTFTag() throws IOException, IllegalArgumentException;
    
    /**
     * Read a TTF 'GlyphID' data type. 'GlyphID' is glyph index number, same as uint16(length = 16 bits).
     * @return unsigned 16-bits integer.
     * @throws IOException
     */
    public int readTTFGlyphID() throws IOException;
    
    /**
     * Read a TTF 'Offset' data type. 'Offset' is offset to a table, same as uint16 (length = 16 bits), NULL offset = 0x0000.
     * @return unsigned 16-bits integer.
     * @throws IOException
     */
    public int readTTFOffset() throws IOException;
    
    /**
     * Get the current position.
     * @return position value.
     * @throws IOException 
     */
    public long getPosition() throws IOException;
    
    /**
     * Set the current position.
     * @param pos taget position value.
     * @throws IOException
     */
    public void setPosition(long pos) throws IOException;
    
    /**
     * Close file.
     * @throws IOException 
     */
    public void close() throws IOException;
}
