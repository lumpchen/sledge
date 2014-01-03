package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.reader.InvalidTagException;
import me.lumpchen.sledge.pdf.reader.PObjectReader;
import me.lumpchen.sledge.pdf.syntax.basic.PObject;
import me.lumpchen.sledge.pdf.writer.ObjectWriter;

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
	protected void readBeginTag(PObjectReader reader) {
		this.objNum = reader.readInt();
		this.genNum = reader.readInt();
	}

	@Override
	protected void readBody(PObjectReader reader) {
	}

	@Override
	protected void readEndTag(PObjectReader reader) {
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

	@Override
	protected void writeBeginTag(ObjectWriter writer) {
	}

	@Override
	protected void writeBody(ObjectWriter writer) {
		writer.writeInt(this.objNum);
		writer.writeSpace();
		writer.writeInt(this.genNum);
		writer.writeSpace();
		writer.writeBytes(IndirectRef.BEGIN);
	}

	@Override
	protected void writeEndTag(ObjectWriter writer) {
	}
}
