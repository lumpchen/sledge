package com.docscience.pathfinder.font.driver.ttf;

import java.io.IOException;

public class TTFRandomArrayReader implements TTFRandomReader {

    private byte[] data;
    private int position;
    private int start;
    private int end;
    
    public TTFRandomArrayReader(byte[] data) {
        this(data, 0, data.length);
    }
    
    public TTFRandomArrayReader(byte[] data, int offset, int length) {
        assert(data != null);
        this.data = data;
        start = offset;
        end = offset + length;
    }
    
    @Override
	public void close() throws IOException {
        // do nothing.
    }

    @Override
	public long getPosition() throws IOException {
        return position;
    }
    
    @Override
	public void setPosition(long pos) throws IOException {
        if (pos < start || pos >= end) {
            throw new IOException("out of data bounds");
        }
        position = (int) pos;
    }

    @Override
	public void readBytes(byte[] bytes) throws IOException {
        assert(bytes != null);
        if (bytes.length + position > end) {
            throw new IOException("out of data bounds");
        }
        for (int i=0; i<bytes.length; ++i) {
            bytes[i] = data[position++];
        }
    }

    @Override
	public void readBytes(byte[] bytes, int offset, int length) throws IOException {
        assert(bytes != null);
        assert(offset >= 0);
        assert(length > 0);
        if (length + position > end) {
            throw new IOException("out of data bounds");
        }
        for (int i=offset; i<offset+length; ++i) {
            bytes[i] = data[position++];
        }
    }

    @Override
	public short readTTFByte() throws IOException {
        if (position + 1 > end) {
            throw new IOException("out of data bounds");
        }
        return (short) (data[position++] & 0xff);
    }

    @Override
	public byte readTTFChar() throws IOException {
        if (position + 1 > end) {
            throw new IOException("out of data bounds");
        }        
        return data[position++];
    }

    @Override
	public short readTTFF2Dot14() throws IOException {
        return readTTFShort();
    }

    @Override
	public short readTTFFUnit() throws IOException {
        return readTTFShort();
    }

    @Override
	public short readTTFFWord() throws IOException {
        return readTTFShort();
    }

    @Override
	public int readTTFFixed() throws IOException {
        return readTTFLong();
    }

    @Override
	public int readTTFGlyphID() throws IOException {
        return readTTFUShort();
    }

    @Override
	public int readTTFLong() throws IOException {
        if (position + 4 > end) {
            throw new IOException("out of data bounds");
        }
        int r = ((data[position] & 0xff) << 24) 
                + ((data[position + 1] & 0xff) << 16)
                + ((data[position + 2] & 0xff) << 8)
                + ((data[position + 3] & 0xff));
        position += 4;
        return r;
    }

    @Override
	public long readTTFLongDateTime() throws IOException {
        if (position + 8 > end) {
            throw new IOException("out of data bounds");
        }
        int r1 = ((data[position] & 0xff) << 24) 
                + ((data[position + 1] & 0xff) << 16)
                + ((data[position + 2] & 0xff) << 8)
                + ((data[position + 3] & 0xff));
        position += 4;
        int r2 = ((data[position] & 0xff) << 24) 
        + ((data[position + 1] & 0xff) << 16)
        + ((data[position + 2] & 0xff) << 8)
        + ((data[position + 3] & 0xff));
        position += 4;
        return (((long) r1) << 32) | (r2 & 0xFFFFFFFFL);
    }

    @Override
	public int readTTFOffset() throws IOException {
        return readTTFUShort();
    }

    @Override
	public short readTTFShort() throws IOException {
        if (position + 2 > end) {
            throw new IOException("out of data bounds");
        }
        short r = (short) (((data[position] & 0xff) << 8) + (data[position + 1] & 0xff));
        position += 2;
        return r;
    }

    @Override
	public int readTTFTag() throws IOException, IllegalArgumentException {
        return readTTFLong();
    }

    @Override
	public int readTTFUFWord() throws IOException {
        return readTTFUShort();
    }

    @Override
	public long readTTFULong() throws IOException {
        if (position + 4 > end) {
            throw new IOException("out of data bounds");
        }
        long r = ((data[position] & 0xff) << 24) 
                + ((data[position + 1] & 0xff) << 16)
                + ((data[position + 2] & 0xff) << 8)
                + ((data[position + 3] & 0xff));
        position += 4;
        return r & 0xffffffff;
    }

    @Override
	public int readTTFUShort() throws IOException {
        if (position + 2 > end) {
            throw new IOException("out of data bounds");
        }
        int r = ((data[position] & 0xff) << 8) + (data[position + 1] & 0xff);
        position += 2;
        return r;
    }

}
