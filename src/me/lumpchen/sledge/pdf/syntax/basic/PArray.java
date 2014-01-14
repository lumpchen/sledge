package me.lumpchen.sledge.pdf.syntax.basic;

import java.util.ArrayList;
import java.util.List;

public class PArray extends PObject {

	public static final byte BEGIN = '[';
	public static final byte END = ']';
	
	private List<PObject> objList;
	
	public PArray() {
		this.type = Type.Array;
		this.objList = new ArrayList<PObject>();
	}
		
	public void add(PObject child) {
		if (null == child) {
			return;
		}
		this.objList.add(child);
	}
	
	public int size() {
		return this.objList.size();
	}
	
	public PObject get(int index) {
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
}
