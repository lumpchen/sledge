package me.lumpchen.sledge.pdf.syntax.basic;

import me.lumpchen.sledge.pdf.reader.InvalidTagException;
import me.lumpchen.sledge.pdf.reader.PObjectReader;
import me.lumpchen.sledge.pdf.writer.ObjectWriter;

public class PNull extends PObject {

	public static final byte[] NULL = { 'n', 'u', 'l', 'l' };

	public PNull() {
		super.type = Type.Null;
	}

	public String toString() {
		return "null";
	}

	@Override
	protected void readBeginTag(PObjectReader reader) {
	}

	@Override
	protected void readBody(PObjectReader reader) {
		byte[] bytes = reader.readBytes(NULL.length);
		if (bytes[0] != NULL[0] || bytes[1] != NULL[1] || bytes[2] != NULL[2]
				|| bytes[3] != NULL[3]) {
			throw new InvalidTagException(new String(bytes));
		}
	}

	@Override
	protected void readEndTag(PObjectReader reader) {
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
