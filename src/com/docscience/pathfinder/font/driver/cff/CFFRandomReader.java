package com.docscience.pathfinder.font.driver.cff;

import java.io.IOException;

/**
 * @author wxin
 *
 */
public interface CFFRandomReader {
    
    /**
     * Get the current position.
     * @return
     * @throws IOException
     */
    public long getPosition() throws IOException;
    
    /**
     * Set the current position.
     * @param pos
     * @throws IOException
     */
    public void setPosition(long pos) throws IOException;
    
    /**
     * Read bytes.
     * @param b
     * @throws IOException
     */
    public void readBytes(byte[] b) throws IOException;
    
    /**
     * Read bytes.
     * @param b
     * @param offset
     * @param length
     * @throws IOException
     */
    public void readBytes(byte[] b, int offset, int length) throws IOException;
       
    /**
     * Read a CFF 'Card16' data type, 'Card16' is 16-bit unsigned integer.
     * @return 16-bit unsigned integer.
     * @throws IOException
     */
    public int readCFFCard16() throws IOException;
    
    /**
     * Read a CFF 'Card8' data type, 'Card8' is 8-bit unsigned integer.
     * @return 8-bit unsigned integer.
     * @throws IOException
     */
    public int readCFFCard8() throws IOException;
    
    /**
     * Read a CFF 1-byte 'Offset' data type, it's 8-bit unsigned integer.
     * @return 8-bit unsigned integer.
     * @throws IOException
     */
    public int readCFFOffset1() throws IOException;
    
    /**
     * Read a CFF 2-byte 'Offset' data type, it's 16-bit unsigned integer.
     * @return 16-bit unsigned integer.
     * @throws IOException
     */
    public int readCFFOffset2() throws IOException;
    
    /**
     * Read a CFF 3-byte 'Offset' data type, it's 24-bit unsigned integer.
     * @return 24-bit unsigned integer.
     * @throws IOException
     */
    public int readCFFOffset3() throws IOException;
    
    /**
     * Read a CFF 4-byte 'Offset' data type, it's 31-bit unsigned integer. 
     * @return 31-bit unsigned integer.
     * @throws IOException
     */
    public int readCFFOffset4() throws IOException;
    
    /**
     * Read a CFF 'OffSize' data type, 'OffSize' is 1-byte unsigned integer but 
     * only value 1 ~ 4 is valide. Note: implementer don't need check the range 
     * of value, invoker must validate it.
     * @return 8-bit unsigned integer.
     * @throws IOException
     */
    public int readCFFOffSize() throws IOException;
    
    /**
     * Read a CFF 'SID' data type, 'SID' is 2-byte unsigned integer, valide 
     * range is 0 ~ 64999. Note: implementer don't need check the range of value,
     * invoker must validate it.
     * @return 16-bit unsigned integer.
     * @throws IOException
     */
    public int readCFFSID() throws IOException;
    
}
