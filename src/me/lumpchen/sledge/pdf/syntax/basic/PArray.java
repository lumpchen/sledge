package me.lumpchen.sledge.pdf.syntax.basic;

import java.util.ArrayList;
import java.util.List;

import me.lumpchen.sledge.pdf.reader.InvalidTagException;
import me.lumpchen.sledge.pdf.reader.PObjectReader;
import me.lumpchen.sledge.pdf.writer.ObjectWriter;

public class PArray extends PObject {

	public static final byte BEGIN = '[';
	public static final byte END = ']';
	
	private List<PObject> objList;
	
	public PArray() {
		this.type = Type.Array;
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
	public void readBeginTag(PObjectReader reader) {
		byte tag = reader.readByte();
		if (tag != PArray.BEGIN) {
			throw new InvalidTagException();
		}
	}

	@Override
	public void readBody(PObjectReader reader) {
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
	public void readEndTag(PObjectReader reader) {
		byte tag = reader.readByte();
		if (tag != PArray.END) {
			throw new InvalidTagException();
		}
	}

	@Override
	protected void writeBeginTag(ObjectWriter writer) {
		writer.writeBytes(PArray.BEGIN);
	}

	@Override
	protected void writeBody(ObjectWriter writer) {
		if (null == this.objList || this.objList.size() <= 0) {
			return;
		}
		for (int i = 0, n = this.objList.size(); i < n; i++) {
			PObject obj = this.objList.get(i);
			obj.writer(writer);
			if (i != n - 1) {
				writer.writeSpace();				
			}
		}
	}

	@Override
	protected void writeEndTag(ObjectWriter writer) {
		writer.writeBytes(PArray.END);
	}
}
