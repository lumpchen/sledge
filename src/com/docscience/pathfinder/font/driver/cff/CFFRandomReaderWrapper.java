package com.docscience.pathfinder.font.driver.cff;

import java.io.EOFException;
import java.io.IOException;

import com.docscience.pathfinder.font.driver.ttf.TTFRandomReader;

/**
 * @author wxin
 *
 */
public class CFFRandomReaderWrapper implements CFFRandomReader {

    private TTFRandomReader rd;
    private final long offset;
    private final long length;
    
    public CFFRandomReaderWrapper(TTFRandomReader rd, long offset, long length) throws IOException {
        this.rd = rd;
        this.offset = offset;
        this.length = length;
        rd.setPosition(offset);
    }
    
    /* (non-Javadoc)
     * @see com.docscience.emitter.ps.font.cff.CFFRandomReader#getPosition()
     */
    @Override
	public long getPosition() throws IOException {
        return rd.getPosition() - offset;
    }
    
    /* (non-Javadoc)
     * @see com.docscience.emitter.ps.font.cff.CFFRandomReader#setPosition(long)
     */
    @Override
	public void setPosition(long pos) throws IOException {
        rd.setPosition(pos + offset);
        checkEOF(0);
    }

    private void checkEOF(long delta) throws IOException {
        if (rd.getPosition() + delta > offset + length) {
            throw new EOFException();
        }
    }

    /* (non-Javadoc)
     * @see com.docscience.emitter.ps.font.cff.CFFRandomReader#readBytes(byte[])
     */
    @Override
	public void readBytes(byte[] b) throws IOException {
        readBytes(b, 0, b.length);
    }

    /* (non-Javadoc)
     * @see com.docscience.emitter.ps.font.cff.CFFRandomReader#readBytes(byte[], int, int)
     */
    @Override
	public void readBytes(byte[] b, int offset, int length) throws IOException {
        checkEOF(length);
        rd.readBytes(b, offset, length);
    }

    /* (non-Javadoc)
     * @see com.docscience.emitter.ps.font.cff.CFFRandomReader#readCFFCard16()
     */
    @Override
	public int readCFFCard16() throws IOException {
        checkEOF(2);
        return rd.readTTFUShort();
    }

    /* (non-Javadoc)
     * @see com.docscience.emitter.ps.font.cff.CFFRandomReader#readCFFCard8()
     */
    @Override
	public int readCFFCard8() throws IOException {
        checkEOF(1);
        return rd.readTTFByte();
    }

    /* (non-Javadoc)
     * @see com.docscience.emitter.ps.font.cff.CFFRandomReader#readCFFOffSize()
     */
    @Override
	public int readCFFOffSize() throws IOException {
        checkEOF(1);
        return rd.readTTFByte();
    }

    /* (non-Javadoc)
     * @see com.docscience.emitter.ps.font.cff.CFFRandomReader#readCFFOffset1()
     */
    @Override
	public int readCFFOffset1() throws IOException {
        checkEOF(1);
        return rd.readTTFByte();
    }

    /* (non-Javadoc)
     * @see com.docscience.emitter.ps.font.cff.CFFRandomReader#readCFFOffset2()
     */
    @Override
	public int readCFFOffset2() throws IOException {
        checkEOF(2);
        return rd.readTTFUShort();
    }

    /* (non-Javadoc)
     * @see com.docscience.emitter.ps.font.cff.CFFRandomReader#readCFFOffset3()
     */
    @Override
	public int readCFFOffset3() throws IOException {
        checkEOF(3);
        return (rd.readTTFByte() << 16)
                + (rd.readTTFByte() << 8)
                + (rd.readTTFByte());
    }

    /* (non-Javadoc)
     * @see com.docscience.emitter.ps.font.cff.CFFRandomReader#readCFFOffset4()
     */
    @Override
	public int readCFFOffset4() throws IOException {
        checkEOF(4);
        return (int) rd.readTTFULong();
    }

    /* (non-Javadoc)
     * @see com.docscience.emitter.ps.font.cff.CFFRandomReader#readCFFSID()
     */
    @Override
	public int readCFFSID() throws IOException {
        checkEOF(2);
        return rd.readTTFUShort();
    }
    
}
