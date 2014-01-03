package me.lumpchen.sledge.pdf.syntax.basic;

import me.lumpchen.sledge.pdf.reader.InvalidTagException;
import me.lumpchen.sledge.pdf.reader.PObjectReader;
import me.lumpchen.sledge.pdf.writer.ObjectWriter;

public class PBoolean extends PObject {

	public static final byte[] TAG_TRUE = {'t', 'r', 'u', 'e'};
	public static final byte[] TAG_FALSE = {'f', 'a', 'l', 's', 'e'};
	
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	public static final PBoolean True = new PBoolean(true);
	public static final PBoolean False = new PBoolean(false);

	private boolean value;

	public PBoolean() {
		this.type = Type.Boolean;
	}

	public PBoolean(boolean b) {
		this.type = Type.Boolean;
		this.value = b;
	}

	public PBoolean(String s) {
		this.type = Type.Boolean;
		if (TRUE.equalsIgnoreCase(s)) {
			this.value = true;
		} else {
			this.value = false;
		}
	}

	public String toString() {
		if (value) {
			return "true";
		} else {
			return "false";
		}
	}
	
	public boolean getValue() {
		return this.value;
	}

	@Override
	protected void readBeginTag(PObjectReader reader) {
	}

	@Override
	protected void readBody(PObjectReader reader) {
		byte b0 = reader.readByte();
		byte b1 = reader.readByte();
		byte b2 = reader.readByte();
		byte b3 = reader.readByte();
		if (b0 == 't' && b1 == 'r' && b2 == 'u' && b3 == 'e') {
			this.value = true;
			return;
		} else {
			byte b4 = reader.readByte();
			if (b0 == 'f' && b1 == 'a' && b2 == 'l' && b3 == 's' && b4 == 'e') {
				this.value = false;
				return;
			}
		}
		throw new InvalidTagException();		
	}

	@Override
	protected void readEndTag(PObjectReader reader) {
	}

	@Override
	protected void writeBeginTag(ObjectWriter writer) {
	}

	@Override
	protected void writeBody(ObjectWriter writer) {
		if (this.value) {
			writer.writeBytes(PBoolean.TAG_TRUE);
		} else {
			writer.writeBytes(PBoolean.TAG_FALSE);
		}
	}

	@Override
	protected void writeEndTag(ObjectWriter writer) {
	}
}
