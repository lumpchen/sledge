package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.syntax.basic.PArray;
import me.lumpchen.sledge.pdf.syntax.basic.PName;
import me.lumpchen.sledge.pdf.syntax.basic.PNumber;
import me.lumpchen.sledge.pdf.syntax.basic.PString;

public abstract class DocObject {

	protected DocObject parent;
	protected IndirectObject insideObj;
	
	protected DocObject(IndirectObject obj) {
		if (null == obj) {
			throw new InvalidTypeException("inside object is null.");
		}
		PName type = getType();
		if (null == type) {
			this.insideObj = obj;
			return;
		}
		if (!type.equals(obj.getValue(PName.type))) {
			throw new InvalidTypeException(obj.toString());
		}
		this.insideObj = obj;
	}
	
	public String toString() {
		if (this.insideObj != null) {
			return this.insideObj.toString();
		}
		return "";
	}
	
	protected PNumber getValueAsNumber(PName name) {
		return this.insideObj.getValueAsNumber(name);
	}
	
	protected PArray getValueAsArray(PName name) {
		return this.insideObj.getValueAsArray(name);
	}
	
	protected IndirectRef getValueAsRef(PName name) {
		return this.insideObj.getValueAsRef(name);
	}
	
	protected PString getValueAsString(PName name) {
		return this.insideObj.getValueAsString(name);
	}
	
	public DocObject getParent() {
		return this.parent;
	}
	
	abstract public PName getType();
}
