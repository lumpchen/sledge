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

	abstract public void read(ObjectReader reader);
}
