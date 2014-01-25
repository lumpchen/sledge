package me.lumpchen.sledge.pdf.reader;

import java.io.IOException;

public interface RandomByteReader {
	
	public void position(long offset, boolean fromTrailer);
	public void position(long pos);
	
	public long position();
	
	public long remain();
		
	public byte read()  throws IOException;
	
	public byte[] read(int size)  throws IOException;
	
}
