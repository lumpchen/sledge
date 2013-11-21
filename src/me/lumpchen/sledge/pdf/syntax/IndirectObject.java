package me.lumpchen.sledge.pdf.syntax;

import java.util.ArrayList;
import java.util.List;

import me.lumpchen.sledge.pdf.reader.InvalidElementException;
import me.lumpchen.sledge.pdf.reader.InvalidTagException;
import me.lumpchen.sledge.pdf.reader.ObjectReader;
import me.lumpchen.sledge.pdf.syntax.basic.PDictionary;
import me.lumpchen.sledge.pdf.syntax.basic.PStream;

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

	public int getChildrenCount() {
		return this.objList.size();
	}

	public PObject getLastChild() {
		if (this.objList.size() == 0) {
			return null;
		}
		return this.objList.get(this.objList.size() - 1);
	}
	
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(this.objNum + " " + this.genNum + " " + "obj");
		
		buf.append('\n');
		for (PObject obj : this.objList) {
			buf.append(obj.toString());
		}
		
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
				PObject streamDict = this.getLastChild();
				if (null == streamDict || !(streamDict instanceof PDictionary)) {
					throw new InvalidElementException();
				}
				((PStream) next).setDict((PDictionary) streamDict);
				this.removeObj(streamDict);
				next.read(reader);
			} else {
				this.addObj(next);
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
