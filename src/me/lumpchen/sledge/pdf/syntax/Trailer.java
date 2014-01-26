package me.lumpchen.sledge.pdf.syntax;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.lumpchen.sledge.pdf.reader.InvalidElementException;
import me.lumpchen.sledge.pdf.reader.LineData;
import me.lumpchen.sledge.pdf.reader.ObjectReader;
import me.lumpchen.sledge.pdf.reader.RandomByteReader;
import me.lumpchen.sledge.pdf.reader.Tokenizer;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PNumber;
import me.lumpchen.sledge.pdf.syntax.basic.PObject;

public class Trailer {

	public static byte[] TRAILER = { 't', 'r', 'a', 'i', 'l', 'e', 'r' };
	public static byte[] STARTXREF = {'s', 't', 'a', 'r', 't', 'x', 'r', 'e', 'f'};
	public static final byte[] EOF = {'%', '%', 'E', 'O', 'F'};
	
	private PDictionary dict;
	private PNumber startxref;
	
	private Trailer prevTrailer;

	public Trailer() {
	}
	
	public void setPreTrailer(Trailer prevTrailer) {
		this.prevTrailer = prevTrailer;
	}
	
	public Trailer getPrevTrailer() {
		return this.prevTrailer;
	}
	
	public int getSize() {
		PObject size = this.dict.get(PName.size);
		if (size == null || !(size instanceof PNumber)) {
			throw new InvalidElementException(PName.SIZE);
		}
		return ((PNumber) size).intValue();
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
	
	public long getXRefStm() {
		PObject refStm = this.dict.get(PName.xrefstm);
		if (null == refStm) {
			return -1;
		}
		if (!(refStm instanceof PNumber)) {
			throw new InvalidElementException(PName.XREFSTM);
		}
		return ((PNumber) refStm).longValue();
	}
	
	public long getPrev() {
		PObject prev = this.dict.get(PName.prev);
		if (null == prev) {
			return -1;
		}
		if (!(prev instanceof PNumber)) {
			throw new InvalidElementException(PName.PREV);
		}
		return ((PNumber) prev).longValue();
	}
	
	public PDictionary getDict() {
		return this.dict;
	}
	
	public void setStartxref(long pos) {
		this.startxref = new PNumber(pos);
	}
	
	public long getStartxref() {
		return this.startxref.longValue();
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

	public boolean read(RandomByteReader reader) throws IOException {
		Tokenizer tokenizer = new Tokenizer(reader); 
		boolean found = false;
		List<LineData> lineArr = new ArrayList<LineData>();
		while (true) {
			LineData line = new LineData(tokenizer.readLine());
			if (line.length() == 0) {
				break;
			}
			if (line.startsWith(Trailer.EOF)) {
				break;
			}
			if (line.startsWith(TRAILER)) {
				found = true;
			}
			lineArr.add(line);
		}
		
		if (found) {
			for (int i = 0, n = lineArr.size(); i < n;  i++) {
				LineData line = lineArr.get(i);
				if (line.startsWith(STARTXREF)) {
					line = lineArr.get(++i);
					this.startxref = new PNumber(line.readAsLong());
				} else if (line.startsWith(PDictionary.BEGIN)) {
					ObjectReader objReader = new ObjectReader(line.getBytes());
					this.dict = objReader.readDict(); 
				}
			}			
		}
		
		return found;
	}
	
	public void setXRefObj(IndirectObject obj) {
		this.dict = obj.getDict();
	}
}
