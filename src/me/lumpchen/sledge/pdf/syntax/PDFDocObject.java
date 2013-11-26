package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PInteger;
import me.lumpchen.sledge.pdf.syntax.basic.PName;

public abstract class PDFDocObject {

	protected IndirectObject insideObj;
	
	protected PDFDocObject(IndirectObject obj) {
		if (obj == null || !getType().equals(obj.getValue(PName.type))) {
			throw new InvalidTypeException();
		}
		this.insideObj = obj;
	}
	
	public String toString() {
		if (this.insideObj != null) {
			return this.insideObj.toString();
		}
		return "";
	}
	
	protected PInteger getValueAsInteger(PName name) {
		return this.insideObj.getValueAsInteger(name);
	}
	
	protected PArray getValueAsArray(PName name) {
		return this.insideObj.getValueAsArray(name);
	}
	
	protected IndirectRef getValueAsRef(PName name) {
		return this.insideObj.getValueAsRef(name);
	}
	
	abstract public PName getType();
}
