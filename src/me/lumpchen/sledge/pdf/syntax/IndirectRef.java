package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.reader.ObjectReader;

public class IndirectRef extends PObject {

	public static final byte[] BEGIN = { 'R' };

	private int objNum;
	private int genNum;
	
	public IndirectRef() {
		
	}

	public IndirectRef(int objectNumber, int generationNumber) {
		this.objNum = objectNumber;
		this.genNum = generationNumber;
	}
	
	public int getObjNum() {
		return this.objNum;
	}
	
	public int getGenNum() {
		return this.genNum;
	}

	@Override
	public void read(ObjectReader reader) {
		
	}
	
	
}
