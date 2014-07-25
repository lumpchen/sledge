package me.lumpchen.sledge.pdf.syntax.document;

import me.lumpchen.sledge.pdf.syntax.IndirectObject;
import me.lumpchen.sledge.pdf.syntax.IndirectRef;
import me.lumpchen.sledge.pdf.syntax.InvalidTypeException;
import me.lumpchen.sledge.pdf.syntax.lang.PArray;
import me.lumpchen.sledge.pdf.syntax.lang.PDictionary;
import me.lumpchen.sledge.pdf.syntax.lang.PName;
import me.lumpchen.sledge.pdf.syntax.lang.PNumber;
import me.lumpchen.sledge.pdf.syntax.lang.PObject;
import me.lumpchen.sledge.pdf.syntax.lang.PString;

public abstract class DocObject {

	protected DocObject parent;
	protected IndirectObject insideObj;
	protected PDFDocument owner;
	
	protected DocObject(IndirectObject obj, PDFDocument owner) {
		if (null == obj) {
			throw new InvalidTypeException("inside object is null.");
		}
		PName type = getType();
		if (null == type) {
			this.insideObj = obj;
			return;
		}
		if (!type.equals(obj.getValue(PName.Type))) {
			throw new InvalidTypeException(obj.toString());
		}
		this.insideObj = obj;
		this.owner = owner;
	}
	
	public String toString() {
		if (this.insideObj != null) {
			return this.insideObj.toString();
		}
		return "";
	}
	
	public IndirectObject insideObject() {
		return this.insideObj;
	}
	
	public PDFDocument getDocument() {
		return this.owner;
	}
	
	protected PObject getValue(PName name) {
		return this.insideObj.getValue(name);
	}
	
	protected PObject getValue(String name) {
		return this.insideObj.getValue(PName.instance(name));
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
	
	protected PName getValueAsName(PName name) {
		return this.insideObj.getValueAsName(name);
	}
	
	protected PString getValueAsString(PName name) {
		return this.insideObj.getValueAsString(name);
	}
	
	protected PDictionary getValueAsDict(PName name) {
		return this.insideObj.getValueAsDict(name);
	}
	
	protected PNumber getValueAsNumber(String name) {
		return this.insideObj.getValueAsNumber(PName.instance(name));
	}
	
	protected PArray getValueAsArray(String name) {
		return this.insideObj.getValueAsArray(PName.instance(name));
	}
	
	protected IndirectRef getValueAsRef(String name) {
		return this.insideObj.getValueAsRef(PName.instance(name));
	}
	
	protected PName getValueAsName(String name) {
		return this.insideObj.getValueAsName(PName.instance(name));
	}
	
	protected PString getValueAsString(String name) {
		return this.insideObj.getValueAsString(PName.instance(name));
	}
	
	protected PDictionary getValueAsDict(String name) {
		return this.insideObj.getValueAsDict(PName.instance(name));
	}
	
	public DocObject getParent() {
		return this.parent;
	}
	
	abstract public PName getType();
}
