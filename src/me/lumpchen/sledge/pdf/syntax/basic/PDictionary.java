package me.lumpchen.sledge.pdf.syntax.basic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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
		return this.dict.get(key);
	}
	
	public PNumber getValueAsNumber(PName key) {
		PObject value = this.get(key);
		if (null == value || !(value instanceof PNumber)) {
			return null;
		}
		return (PNumber) value;
	}

	public PObject remove(PName key) {
		return this.dict.remove(key);
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("<<");
		Iterator<Entry<PName, PObject>>  it = this.dict.entrySet().iterator();
		while (it.hasNext()) {
			Entry<PName, PObject> entry = it.next();
			PObject key = entry.getKey();
			buf.append(key.toString());
			
			buf.append(" ");
			PObject value = entry.getValue();
			buf.append(value.toString());
		}
		buf.append(">>");
		
		return buf.toString();
	}

	@Override
	protected void readBeginTag(ObjectReader reader) {
		byte[] tag = reader.readBytes(BEGIN.length);
		if (tag[0] != BEGIN[0] || tag[1] != BEGIN[1]) {
			throw new InvalidTagException();
		}
	}

	@Override
	protected void readBody(ObjectReader reader) {
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

	@Override
	protected void readEndTag(ObjectReader reader) {
		byte[] tag = reader.readBytes(END.length);
		if (tag[0] != END[0] || tag[1] != END[1]) {
			throw new InvalidTagException();
		}
	}
}
