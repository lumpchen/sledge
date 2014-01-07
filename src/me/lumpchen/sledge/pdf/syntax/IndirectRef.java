package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.syntax.basic.PObject;

public class IndirectRef extends PObject {

	public static final byte BEGIN = 'R';

	private int objNum;
	private int genNum;
	
	public IndirectRef() {
		super.type = Type.IndirectRef;
	}

	public IndirectRef(int objectNumber, int generationNumber) {
		super.type = Type.IndirectRef;
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
    public int hashCode() {
    	int hash = 1;
    	hash = hash * 17 + this.objNum;
    	hash = hash * 31 + this.genNum;
    	return hash;
    }
    
	@Override
    public boolean equals(Object obj) {
        if (this == obj) {
        	return true;
        }
        if (null == obj || !(obj instanceof IndirectRef)) {
        	return false;
        }
        
        if ( this.objNum != ((IndirectRef) obj).objNum 
        		|| this.genNum != ((IndirectRef) obj).genNum) {
        	return false;
        }
        
        return true;
    }
	
	public String toString() {
		return this.objNum + " " + this.genNum + " " + "R";
	}
}
