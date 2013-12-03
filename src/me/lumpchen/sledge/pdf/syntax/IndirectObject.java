package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.reader.InvalidElementException;
import me.lumpchen.sledge.pdf.reader.InvalidTagException;
import me.lumpchen.sledge.pdf.reader.ObjectReader;
import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PInteger;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
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
	private PStream stream;

	public IndirectObject() {
	}

	public IndirectObject(int objectNumber, int generationNumber) {
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
	
	public PObject getValue(PName key) {
		if (this.dict() == null) {
			return null;
		}
		return this.dict().get(key);
	}
	
	public PName getValueAsName(PName key) {
		PObject obj = this.getValue(key);
		if (obj != null && obj instanceof PName) {
			return (PName) obj;
		}
		return null;
	}
	
	public PInteger getValueAsInteger(PName key) {
		PObject obj = this.getValue(key);
		if (obj != null && obj instanceof PInteger) {
			return (PInteger) obj;
		}
		return null;
	}
	
	public PArray getValueAsArray(PName key) {
		PObject obj = this.getValue(key);
		if (obj != null && obj instanceof PArray) {
			return (PArray) obj;
		}
		return null;
	}
	
	public IndirectRef getValueAsRef(PName key) {
		PObject obj = this.getValue(key);
		if (obj != null && obj instanceof IndirectRef) {
			return (IndirectRef) obj;
		}
		return null;
	}
	
	public PString getValueAsString(PName key) {
		PObject obj = this.getValue(key);
		if (obj != null && obj instanceof PString) {
			return (PString) obj;
		}
		return null;
	}
	
	private PDictionary dict() {
		if (null == this.insideObj || !(this.insideObj instanceof PDictionary)) {
			return null;
		}
		return (PDictionary) this.insideObj;
	}
	
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.objNum + " " + this.genNum + " " + "obj");
		buf.append('\n');
		
		if (null != this.stream) {
			buf.append(this.stream.toString());
		} else if (null != this.insideObj) {
			buf.append(this.insideObj.toString());
		} 
		
		buf.append("endobj");
		buf.append('\n');
		return buf.toString();
	}

	@Override
	protected void readBeginTag(ObjectReader reader) {
		int iobj = reader.readInt();
		int igen = reader.readInt();

		byte[] obj = reader.readBytes(BEGIN.length);
		for (int i = 0; i < BEGIN.length; i++) {
			if (obj[i] != BEGIN[i]) {
				throw new InvalidTagException();
			}
		}

		this.objNum = iobj;
		this.genNum = igen;
	}

	@Override
	protected void readBody(ObjectReader reader) {
		while (true) {
			PObject next = reader.readNextObj();
			if (null == next) {
				break;
			}
			if (next instanceof PStream) {
				this.stream = (PStream) next;
				if (null == this.insideObj || !(this.insideObj instanceof PDictionary)) {
					throw new InvalidElementException();
				}
				this.stream.setDict((PDictionary) this.insideObj);
				this.stream.read(reader);
			} else {
				this.insideObj = next;
			}
		}
	}

	@Override
	protected void readEndTag(ObjectReader reader) {
		byte[] obj = reader.readBytes(END.length);
		for (int i = 0; i < END.length; i++) {
			if (obj[i] != END[i]) {
				throw new InvalidTagException();
			}
		}
	}
}
