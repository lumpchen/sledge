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

	@Override
	public void read(ObjectReader reader) {
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
}
