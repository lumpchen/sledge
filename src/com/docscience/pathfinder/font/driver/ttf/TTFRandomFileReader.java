package com.docscience.pathfinder.font.driver.ttf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author wxin
 *
 */
public class TTFRandomFileReader implements TTFRandomReader {

    private RandomAccessFile file;
    
    public TTFRandomFileReader(String filename) throws FileNotFoundException {
        file = new RandomAccessFile(filename, "r");
    }
    
    public TTFRandomFileReader(File f) throws FileNotFoundException {
        file = new RandomAccessFile(f, "r");
    }
    
    @Override
	public long getPosition() throws IOException {
        return file.getFilePointer();
    }

    @Override
	public short readTTFByte() throws IOException {
        return (short) file.readUnsignedByte();
    }

    @Override
	public byte readTTFChar() throws IOException {
        return file.readByte();
    }

    @Override
	public short readTTFF2Dot14() throws IOException {
        return file.readShort();
    }

    @Override
	public short readTTFFUnit() throws IOException {
        return file.readShort();
    }

    @Override
	public short readTTFFWord() throws IOException {
        return file.readShort();
    }

    @Override
	public int readTTFFixed() throws IOException {
        return file.readInt();
    }

    @Override
	public int readTTFGlyphID() throws IOException {
        return file.readUnsignedShort();
    }

    @Override
	public int readTTFLong() throws IOException {
        return file.readInt();
    }

    @Override
	public long readTTFLongDateTime() throws IOException {
        return file.readLong();
    }

    @Override
	public int readTTFOffset() throws IOException {
        return file.readUnsignedShort();
    }

    @Override
	public short readTTFShort() throws IOException {
        return file.readShort();
    }

    @Override
	public int readTTFTag() throws IOException, IllegalArgumentException {
        return file.readInt();
    }

    @Override
	public int readTTFUFWord() throws IOException {
        return file.readUnsignedShort();
    }

    @Override
	public long readTTFULong() throws IOException {
        return ((long) file.readInt()) & 0x00ffffffff;
    }

    @Override
	public int readTTFUShort() throws IOException {
        return file.readUnsignedShort();
    }

    @Override
	public void setPosition(long pos) throws IOException {
        file.seek(pos);
    }

    @Override
	public void readBytes(byte[] bytes) throws IOException {
        file.read(bytes);
    }

    @Override
	public void readBytes(byte[] bytes, int offset, int length) throws IOException {
        file.read(bytes, offset, length);
    }

    @Override
	public void close() throws IOException {
        file.close();
    }

}
