package me.lumpchen.sledge.pdf.syntax;

import java.util.ArrayList;
import java.util.List;

import me.lumpchen.sledge.pdf.reader.InvalidElementException;
import me.lumpchen.sledge.pdf.reader.LineData;
import me.lumpchen.sledge.pdf.reader.LineReader;
import me.lumpchen.sledge.pdf.reader.ObjectReader;
import me.lumpchen.sledge.pdf.reader.ReadException;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PLong;

public class PDFTrailer {

	public static byte[] TRAILER = { 't', 'r', 'a', 'i', 'l', 'e', 'r' };
	public static byte[] STARTXREF = {'s', 't', 'a', 'r', 't', 'x', 'r', 'e', 'f'};
	public static final byte[] EOF = {'%', '%', 'E', 'O', 'F'};
	
	private PDictionary dict;
	private PLong startxref;

	public PDFTrailer() {
	}
	
	public String toString() {
		StringBuilder buf = new StringBuilder();
		
		buf.append("trailer");
		buf.append('\n');
		buf.append(dict.toString());
		buf.append('\n');
		buf.append("startxref");
		buf.append('\n');
		buf.append(startxref.toString());
		buf.append('\n');
		buf.append("%%EOF");
		buf.append('\n');
		return buf.toString();
	}

	public void setStartxref(PLong pos) {
		this.startxref = pos;
	}

	public PLong getStartxref() {
		return this.startxref;
	}
	
	public void read(LineReader lineReader) {
		boolean found = false;
		List<LineData> lineArr = new ArrayList<LineData>();
		while (true) {
			LineData line = lineReader.getLine();
			if (line.startsWith(TRAILER)) {
				found = true;
				break;
			}
			lineArr.add(0, line);
		}
		
		if (!found) {
			throw new ReadException();
		}
		
		for (int i = 0, n = lineArr.size(); i < n;  i++) {
			LineData line = lineArr.get(i);
			if (line.startsWith(STARTXREF)) {
				line = lineArr.get(++i);
				this.startxref = new PLong(line.readAsLong());
			} else if (line.startsWith(PDictionary.BEGIN)) {
				ObjectReader objReader = new ObjectReader(line.getData());
				PObject obj = objReader.readNextObj();
				if (obj == null || !(obj instanceof PDictionary)) {
					throw new InvalidElementException();
				}
				this.dict = (PDictionary) obj;
			}
		}
	}
}
