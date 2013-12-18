package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.reader.InvalidTagException;
import me.lumpchen.sledge.pdf.reader.ObjectReader;
import me.lumpchen.sledge.pdf.syntax.basic.PObject;

public class IndirectRef extends PObject {

	public static final byte BEGIN = 'R';

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
	protected void readBeginTag(ObjectReader reader) {
		this.objNum = reader.readInt();
		this.genNum = reader.readInt();
	}

	@Override
	protected void readBody(ObjectReader reader) {
	}

	@Override
	protected void readEndTag(ObjectReader reader) {
		byte tag = reader.readByte();
		if (tag != BEGIN) {
			throw new InvalidTagException();
		}		
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
