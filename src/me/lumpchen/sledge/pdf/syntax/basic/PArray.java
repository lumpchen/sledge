package me.lumpchen.sledge.pdf.syntax.basic;

import java.util.ArrayList;
import java.util.List;

import me.lumpchen.sledge.pdf.reader.InvalidTagException;
import me.lumpchen.sledge.pdf.reader.ObjectReader;
import me.lumpchen.sledge.pdf.syntax.PObject;

public class PArray extends PObject {

	public static final byte BEGIN = '[';
	public static final byte END = ']';
	
	private List<PObject> objList;
	
	public PArray() {
		this.objList = new ArrayList<PObject>();
	}
		
	public void appendChild(PObject child) {
		this.objList.add(child);
	}
	
	public int size() {
		return this.objList.size();
	}
	
	public PObject getChild(int index) {
		return this.objList.get(index);
	}
	
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append((char) BEGIN);
		
		for (int i = 0, n = this.objList.size(); i < n; i++) {
			PObject obj = this.objList.get(i);
			buf.append(obj.toString());
			if (i != n - 1) {
				buf.append(" ");				
			}
		}
		buf.append((char) END);
		return buf.toString();
	}
	
	@Override
	public void readBeginTag(ObjectReader reader) {
		byte tag = reader.readByte();
		if (tag != BEGIN) {
			throw new InvalidTagException();
		}
	}

	@Override
	public void readBody(ObjectReader reader) {
		while (true) {
			PObject child = reader.readNextObj();
			if (null == child) {
				break;
			}
			child.setParent(this);
			this.appendChild(child);
		}
	}

	@Override
	public void readEndTag(ObjectReader reader) {
		byte tag = reader.readByte();
		if (tag != END) {
			throw new InvalidTagException();
		}
	}
}
