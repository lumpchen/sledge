package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.reader.InvalidElementException;
import me.lumpchen.sledge.pdf.reader.InvalidTagException;
import me.lumpchen.sledge.pdf.reader.ObjectReader;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PStream;

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

	public PObject getValue(PName key) {
		return this.dict().get(key);
	}
	
	private PDictionary dict() {
		if (null == this.insideObj || !(this.insideObj instanceof PDictionary)) {
			throw new InvalidElementException();
		}
		return (PDictionary) this.insideObj;
	}
	
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.objNum + " " + this.genNum + " " + "obj");
		
		buf.append('\n');
		buf.append(this.insideObj.toString());
		
		buf.append('\n');
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
				this.stream = new PStream();
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
