package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.reader.InvalidElementException;
import me.lumpchen.sledge.pdf.reader.InvalidTagException;
import me.lumpchen.sledge.pdf.reader.PObjectReader;
import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PNumber;
import me.lumpchen.sledge.pdf.syntax.basic.PObject;
import me.lumpchen.sledge.pdf.syntax.basic.PStream;
import me.lumpchen.sledge.pdf.syntax.basic.PString;
import me.lumpchen.sledge.pdf.writer.ObjectWriter;

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
	
	public PStream getStream() {
		return this.stream;
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
	protected void readBeginTag(PObjectReader reader) {
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
	protected void readBody(PObjectReader reader) {
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
	protected void readEndTag(PObjectReader reader) {
		byte[] obj = reader.readBytes(END.length);
		for (int i = 0; i < END.length; i++) {
			if (obj[i] != END[i]) {
				throw new InvalidTagException();
			}
		}
	}

	@Override
	protected void writeBeginTag(ObjectWriter writer) {
		writer.writeInt(this.objNum);
		writer.writeSpace();
		writer.writeInt(this.genNum);
		writer.writeSpace();
		writer.writeBytes(IndirectObject.BEGIN);
		writer.writeLN();
	}

	@Override
	protected void writeBody(ObjectWriter writer) {
		if (this.stream != null) {
			this.stream.writer(writer);
		} else if (this.insideObj != null) {
			this.insideObj.writer(writer);
		}
		writer.writeLN();
	}

	@Override
	protected void writeEndTag(ObjectWriter writer) {
		writer.writeBytes(IndirectObject.END);
		writer.writeLN();
	}
}
