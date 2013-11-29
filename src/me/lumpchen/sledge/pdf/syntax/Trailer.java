package me.lumpchen.sledge.pdf.syntax;

import java.util.ArrayList;
import java.util.List;

import me.lumpchen.sledge.pdf.reader.InvalidElementException;
import me.lumpchen.sledge.pdf.reader.LineData;
import me.lumpchen.sledge.pdf.reader.LineReader;
import me.lumpchen.sledge.pdf.reader.ObjectReader;
import me.lumpchen.sledge.pdf.reader.ReadException;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PInteger;
import me.lumpchen.sledge.pdf.syntax.basic.PLong;
import me.lumpchen.sledge.pdf.syntax.basic.PName;

public class Trailer {

	public static byte[] TRAILER = { 't', 'r', 'a', 'i', 'l', 'e', 'r' };
	public static byte[] STARTXREF = {'s', 't', 'a', 'r', 't', 'x', 'r', 'e', 'f'};
	public static final byte[] EOF = {'%', '%', 'E', 'O', 'F'};
	
	private PDictionary dict;
	private PLong startxref;

	public Trailer() {
	}
	
	public int getSize() {
		PObject size = this.dict.get(PName.size);
		if (size == null || !(size instanceof PInteger)) {
			throw new InvalidElementException(PName.SIZE);
		}
		return ((PInteger) size).getValue();
	}
	
	public IndirectRef getInfo() {
		PObject info = this.dict.get(PName.info);
		if (info == null || !(info instanceof IndirectRef)) {
			throw new InvalidElementException(PName.INFO);
		}
		return (IndirectRef) info;
	}
	
	public IndirectRef getRoot() {
		PObject root = this.dict.get(PName.root);
		if (root == null || !(root instanceof IndirectRef)) {
			throw new InvalidElementException(PName.ROOT);
		}
		return (IndirectRef) root;
	}
	
	public void setStartxref(long pos) {
		this.startxref = new PLong(pos);
	}
	
	public long getStartxref() {
		return this.startxref.getValue();
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

	public void read(LineReader lineReader) {
		boolean found = false;
		List<LineData> lineArr = new ArrayList<LineData>();
		while (true) {
			LineData line = lineReader.readLine();
			if (line == null) {
				break;
			}
			if (line.startsWith(TRAILER)) {
				found = true;
			}
			if (found) {
				lineArr.add(line);
			}
		}
		
		if (!found) {
			throw new ReadException("not found trailer.");
		}
		
		for (int i = 0, n = lineArr.size(); i < n;  i++) {
			LineData line = lineArr.get(i);
			if (line.startsWith(STARTXREF)) {
				line = lineArr.get(++i);
				this.startxref = new PLong(line.readAsLong());
			} else if (line.startsWith(PDictionary.BEGIN)) {
				ObjectReader objReader = new ObjectReader(line);
				PObject obj = objReader.readNextObj();
				if (obj == null || !(obj instanceof PDictionary)) {
					throw new InvalidElementException();
				}
				this.dict = (PDictionary) obj;
			}
		}
	}
}