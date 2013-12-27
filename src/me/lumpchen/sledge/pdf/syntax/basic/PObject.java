package me.lumpchen.sledge.pdf.syntax.basic;

import me.lumpchen.sledge.pdf.reader.ObjectReader;

public abstract class PObject {

	public static enum Type {
		Array("Array"), 
		Boolean("Boolean"), 
		Dict("Dict"), 
		String("String"), 
		Name("Name"),
		Number("Number"), 
		Null("Null"), 
		Stream("Stream"),
		IndirectObject("Indirect Object"), 
		IndirectRef("Indirect Ref");

		private String typeString;
		
		Type(String typeString) {
			this.typeString = typeString;
		}
		
		public String toString() {
			return this.typeString;
		}
	};

	protected PObject parent;
	protected Type type; 

	protected PObject() {
	}

	public PObject getParent() {
		return this.parent;
	}

	public void setParent(PObject parent) {
		this.parent = parent;
	}

	public Type getType() {
		return this.type; 
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
