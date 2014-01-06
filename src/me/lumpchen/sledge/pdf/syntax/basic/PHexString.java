package me.lumpchen.sledge.pdf.syntax.basic;

import me.lumpchen.sledge.pdf.reader.InvalidTagException;
import me.lumpchen.sledge.pdf.reader.ObjectReader;
import me.lumpchen.sledge.pdf.writer.ObjectWriter;

public class PHexString extends PString {

	public static final byte BEGIN = '<';
	public static final byte END = '>';

	public PHexString() {
		super();
	}

	public PHexString(byte[] data) {
		encode(data);
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append("<");
		if (charSequence != null) {
			buf.append(new String(charSequence));
		}
		buf.append(">");
		return buf.toString();
	}
	
	protected void encode(byte[] data) {
	}

	@Override
	protected void readBeginTag(ObjectReader reader) {
		byte tag = reader.readByte();
		if (tag != BEGIN) {
			throw new InvalidTagException();
		}
	}

	@Override
	protected void readBody(ObjectReader reader) {
		byte[] data = reader.readToFlag(END);
		encode(data);
	}

	@Override
	protected void readEndTag(ObjectReader reader) {
		byte tag = reader.readByte();
		if (tag != END) {
			throw new InvalidTagException();
		}		
	}

	@Override
	protected void writeBeginTag(ObjectWriter writer) {
		writer.writeBytes(PHexString.BEGIN);
	}

	@Override
	protected void writeBody(ObjectWriter writer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void writeEndTag(ObjectWriter writer) {
		writer.writeBytes(PHexString.END);
		
	}
}
