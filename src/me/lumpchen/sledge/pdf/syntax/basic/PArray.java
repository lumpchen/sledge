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
	
	public void read(ObjectReader reader) {
		byte tag = reader.readByte();
		if (tag != BEGIN) {
			throw new InvalidTagException();
		}
		
		PObject child = reader.readNextObj();
		while (null != child) {
			child.setParent(this);
			this.appendChild(child);
		}
	}
}
