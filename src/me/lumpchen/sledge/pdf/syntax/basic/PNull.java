package me.lumpchen.sledge.pdf.syntax.basic;

import me.lumpchen.sledge.pdf.reader.ObjectReader;

public class PNull extends PObject {

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

}
