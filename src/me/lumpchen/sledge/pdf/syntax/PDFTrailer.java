package me.lumpchen.sledge.pdf.syntax;

import java.io.IOException;
import java.nio.ByteBuffer;

import me.lumpchen.sledge.pdf.reader.Line;
import me.lumpchen.sledge.pdf.reader.LineReader;
import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PInteger;
import me.lumpchen.sledge.pdf.syntax.basic.PLong;

public class PDFTrailer {

	public static byte[] TRAILER = { 't', 'r', 'a', 'i', 'l', 'e', 'r' };
	public static byte[] STARTXREF = {'s', 't', 'a', 'r', 't', 'x', 'r', 'e', 'f'};
	public static final byte[] EOF = {'%', '%', 'E', 'O', 'F'};
	
	private PInteger size;
	private PInteger prev;
	private PDictionary root;
	private PDictionary encrypt;
	private PDictionary info;
	private PArray id;

	private PDictionary dict;
	private PLong startxref;

	public PDFTrailer() {

	}

	public void setStartxref(PLong pos) {
		this.startxref = pos;
	}

	public PLong getStartxref() {
		return this.startxref;
	}
	
	public void read(ByteBuffer buf) throws IOException {
		LineReader reader = new LineReader(buf, true);
		Line line = reader.getNextLine();
		while (line != null) {
			if (line.startsWith(PDictionary.BEGIN)) {
				this.dict = line.redDict();
				continue;
			}
			
			if (line.startsWith(STARTXREF)) {
				line = reader.getNextLine();
				this.startxref = line.readLong();
				continue;
			}
			line = reader.getNextLine();
		}
	}
}
