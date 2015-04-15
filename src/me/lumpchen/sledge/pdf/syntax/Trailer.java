package me.lumpchen.sledge.pdf.syntax;

import java.io.IOException;

import me.lumpchen.sledge.pdf.reader.InvalidElementException;
import me.lumpchen.sledge.pdf.reader.LineData;
import me.lumpchen.sledge.pdf.reader.ObjectReader;
import me.lumpchen.sledge.pdf.reader.RandomByteReader;
import me.lumpchen.sledge.pdf.reader.Tokenizer;
import me.lumpchen.sledge.pdf.syntax.lang.PArray;
import me.lumpchen.sledge.pdf.syntax.lang.PDictionary;
import me.lumpchen.sledge.pdf.syntax.lang.PName;
import me.lumpchen.sledge.pdf.syntax.lang.PNumber;
import me.lumpchen.sledge.pdf.syntax.lang.PObject;

public class Trailer {

	public static byte[] TRAILER = { 't', 'r', 'a', 'i', 'l', 'e', 'r' };
	public static byte[] STARTXREF = {'s', 't', 'a', 'r', 't', 'x', 'r', 'e', 'f'};
	public static final byte[] EOF = {'%', '%', 'E', 'O', 'F'};
	
	private IndirectObject trailerObj;
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
		PObject size = this.dict.get(PName.Size);
		if (size == null || !(size instanceof PNumber)) {
			throw new InvalidElementException(PName.Size.getName());
		}
		return ((PNumber) size).intValue();
	}
	
	public IndirectRef getInfo() {
		PObject info = this.dict.get(PName.Info);
		if (info == null || !(info instanceof IndirectRef)) {
//			throw new InvalidElementException(PName.Info.getName());
			return null;
		}
		return (IndirectRef) info;
	}
	
	public IndirectRef getRoot() {
		PObject root = this.dict.get(PName.Root);
		if (root == null || !(root instanceof IndirectRef)) {
			throw new InvalidElementException(PName.Root.getName());
		}
		return (IndirectRef) root;
	}
	
	public long getXRefStm() {
		PObject refStm = this.dict.get(PName.XRefStm);
		if (null == refStm) {
			return -1;
		}
		if (!(refStm instanceof PNumber)) {
			throw new InvalidElementException(PName.XRefStm.getName());
		}
		return ((PNumber) refStm).longValue();
	}
	
	public long getPrev() {
		PObject prev = this.dict.get(PName.Prev);
		if (null == prev) {
			return -1;
		}
		if (!(prev instanceof PNumber)) {
			throw new InvalidElementException(PName.Prev.getName());
		}
		return ((PNumber) prev).longValue();
	}
	
	public PObject getEncrypt() {
		return this.dict.get(PName.Encrypt);
	}
	
	public PArray getID() {
		PObject id = this.dict.get(PName.ID);
		if (null == id) {
			return null;
		}
		if (!(id instanceof PArray)) {
			throw new InvalidElementException(PName.ID.getName());
		}
		return (PArray) id;
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
		while (true) {
			LineData line = new LineData(tokenizer.readLine());
			if (line.length() == 0) {
				continue;
			} 
			
			if (line.startsWith(Trailer.EOF)) {
				break;
			}
			
			if (line.startsWith(TRAILER)) {
				ObjectReader objReader = new ObjectReader(tokenizer);
				this.dict = objReader.readDict(); 
				found = true;
			} else if (line.startsWith(STARTXREF)) {
				line = new LineData(tokenizer.readLine());
				this.startxref = new PNumber(line.readAsLong());
			}
		}
		
		return found;
	}
	
	public void setXRefObj(IndirectObject obj) {
		this.trailerObj = obj;
		this.dict = obj.getDict();
	}
	
	public IndirectObject getXRefObj() {
		return this.trailerObj;
	}
}
