package me.lumpchen.sledge.pdf.syntax;

import me.lumpchen.sledge.pdf.reader.ObjectReader;

public abstract class PObject {

	private PObject parent;

	protected PObject() {
	}

	public PObject getParent() {
		return this.parent;
	}

	public void setParent(PObject parent) {
		this.parent = parent;
	}

	public void read(ObjectReader reader) {
		this.readBeginTag(reader);
		this.readBody(reader);
		this.readEndTag(reader);
	}

	abstract protected void readBeginTag(ObjectReader reader);

	abstract protected void readBody(ObjectReader reader);

	abstract protected void readEndTag(ObjectReader reader);

}
