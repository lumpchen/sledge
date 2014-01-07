package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PNumber;
import me.lumpchen.sledge.pdf.syntax.basic.PObject;
import me.lumpchen.sledge.pdf.syntax.basic.PStream;
import me.lumpchen.sledge.pdf.syntax.basic.PString;

public class IndirectObject extends PObject {

	public static final String OBJ_BEGIN = "obj";
	public static final String OBJ_END = "endobj";

	public static final byte[] BEGIN = { 'o', 'b', 'j' };
	public static final byte[] END = { 'e', 'n', 'd', 'o', 'b', 'j' };

	private int objNum;
	private int genNum;

	private PObject insideObj;
//	private PStream stream;

	public IndirectObject() {
		super.type = Type.IndirectObject; 
	}

	public IndirectObject(int objectNumber, int generationNumber) {
		super.type = Type.IndirectObject;
		this.objNum = objectNumber;
		this.genNum = generationNumber;
	}
	
	public int getObjNum() {
		return this.objNum;
	}

	public int getGenNumb() {
		return this.genNum;
	}

	public PObject insideObj() {
		return this.insideObj;
	}
	
	public void setInsideObj(PObject insideObj) {
		this.insideObj = insideObj;
	}
	
	public PStream getStream() {
		if (this.insideObj instanceof PStream) {
			return (PStream) this.insideObj;
		}
		return null;
	}
	
	public PDictionary getDict() {
		if (null == this.insideObj || !(this.insideObj instanceof PDictionary)) {
			return null;
		}
		return (PDictionary) this.insideObj;
	}
	
	public PObject getValue(PName key) {
		PDictionary dict = this.getDict();
		if (null == dict) {
			return null;
		}
		return dict.get(key);
	}
	
	public PName getValueAsName(PName key) {
		PDictionary dict = this.getDict();
		if (null == dict) {
			return null;
		}
		return dict.getValueAsName(key);
	}
	
	public PNumber getValueAsNumber(PName key) {
		PDictionary dict = this.getDict();
		if (null == dict) {
			return null;
		}
		return dict.getValueAsNumber(key);
	}
	
	public PArray getValueAsArray(PName key) {
		PDictionary dict = this.getDict();
		if (null == dict) {
			return null;
		}
		return dict.getValueAsArray(key);
	}
	
	public IndirectRef getValueAsRef(PName key) {
		PDictionary dict = this.getDict();
		if (null == dict) {
			return null;
		}
		return dict.getValueAsRef(key);
	}
	
	public PString getValueAsString(PName key) {
		PDictionary dict = this.getDict();
		if (null == dict) {
			return null;
		}
		return dict.getValueAsString(key);
	}
	
	public PDictionary getValueAsDict(PName key) {
		PDictionary dict = this.getDict();
		if (null == dict) {
			return null;
		}
		return dict.getValueAsDict(key);
	}
	
	
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.objNum + " " + this.genNum + " " + "obj");
		buf.append('\n');
		
		if (null != this.insideObj) {
			buf.append(this.insideObj.toString());
		}
		
		buf.append("endobj");
		buf.append('\n');
		return buf.toString();
	}

}
