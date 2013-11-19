package me.lumpchen.sledge.pdf.syntax.basic;

import java.util.HashMap;
import java.util.Map;

import me.lumpchen.sledge.pdf.reader.InvalidElementException;
import me.lumpchen.sledge.pdf.reader.InvalidTagException;
import me.lumpchen.sledge.pdf.reader.NotMatchObjectException;
import me.lumpchen.sledge.pdf.reader.ObjectReader;
import me.lumpchen.sledge.pdf.syntax.PObject;

public class PDictionary extends PObject {

	public static final byte[] BEGIN = { '<', '<' };
	public static final byte[] END = { '>', '>' };

	private Map<PName, PObject> dict;

	public PDictionary() {
		this.dict = new HashMap<PName, PObject>();
	}

	public void put(PName key, PObject value) {
		if (this.dict.containsKey(key)) {
			throw new InvalidElementException();
		}
		this.dict.put(key, value);
	}

	public PObject get(PName key) {
		return this.get(key);
	}

	public PObject remove(PName key) {
		return this.dict.remove(key);
	}

	public void read(ObjectReader reader) {
		byte[] tag = reader.readBytes(BEGIN.length);
		if (tag[0] != BEGIN[0] || tag[1] != BEGIN[1]) {
			throw new InvalidTagException();
		}
		
		while (true) {
			PObject key = reader.readNextObj();
			if (key == null) {
				break;
			}
			if (key instanceof PName) {
				PObject value = reader.readNextObj();
				this.dict.put((PName) key, value);
			} else {
				throw new NotMatchObjectException();
			}			
		}
	}
}
