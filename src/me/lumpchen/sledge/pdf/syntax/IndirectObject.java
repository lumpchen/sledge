package me.lumpchen.sledge.pdf.syntax;

import java.util.ArrayList;
import java.util.List;

import me.lumpchen.sledge.pdf.reader.ObjectReader;

public class IndirectObject extends PObject {
	
	public static final String OBJ_BEGIN = "obj";
	public static final String OBJ_END = "endobj";

	public static final byte[] BEGIN = { 'o', 'b', 'j' };
	public static final byte[] END = { 'e', 'n', 'd', 'o', 'b', 'j' };

	private int objNum;
	private int genNum;

	private List<PObject> objList;

	public IndirectObject() {
	}
	
	public IndirectObject(int objectNumber, int generationNumber) {
		this.objNum = objectNumber;
		this.genNum = generationNumber;

		this.objList = new ArrayList<PObject>();
	}
	
	public int getObjNum() {
		return this.objNum;
	}
	
	public int getGenNumb() {
		return this.genNum;
	}

	public void addObj(PObject obj) {
		this.objList.add(obj);
	}

	public void removeObj(PObject obj) {
		this.objList.remove(obj);
	}

	@Override
	public void read(ObjectReader reader) {
		
	}
}
