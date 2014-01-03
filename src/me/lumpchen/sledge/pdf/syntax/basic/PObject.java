package me.lumpchen.sledge.pdf.syntax.basic;

import me.lumpchen.sledge.pdf.reader.PObjectReader;
import me.lumpchen.sledge.pdf.writer.ObjectWriter;

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

	public void read(PObjectReader reader) {
		this.readBeginTag(reader);
		this.readBody(reader);
		this.readEndTag(reader);
	}

	abstract protected void readBeginTag(PObjectReader reader);
	abstract protected void readBody(PObjectReader reader);
	abstract protected void readEndTag(PObjectReader reader);
	
	public void writer(ObjectWriter writer) {
		this.writeBeginTag(writer);
		this.writeBody(writer);
		this.writeEndTag(writer);
	}
	
	abstract protected void writeBeginTag(ObjectWriter writer);
	abstract protected void writeBody(ObjectWriter writer);
	abstract protected void writeEndTag(ObjectWriter writer);
	
}
