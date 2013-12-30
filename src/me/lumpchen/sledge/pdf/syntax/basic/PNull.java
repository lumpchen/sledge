package me.lumpchen.sledge.pdf.syntax.basic;

import me.lumpchen.sledge.pdf.reader.ObjectReader;
import me.lumpchen.sledge.pdf.writer.ObjectWriter;

public class PNull extends PObject {

	public PNull() {
		super.type = Type.Null;
	}
	
	public String toString() {
		return "NULL";
	}
	
	@Override
	protected void readBeginTag(ObjectReader reader) {
	}

	@Override
	protected void readBody(ObjectReader reader) {
	}

	@Override
	protected void readEndTag(ObjectReader reader) {
	}

	@Override
	protected void writeBeginTag(ObjectWriter writer) {
	}

	@Override
	protected void writeBody(ObjectWriter writer) {
	}

	@Override
	protected void writeEndTag(ObjectWriter writer) {
	}
}
