package me.lumpchen.sledge.pdf.syntax.basic;

import me.lumpchen.sledge.pdf.reader.InvalidTagException;
import me.lumpchen.sledge.pdf.reader.ObjectReader;

public class PBoolean extends PObject {

	public static final byte[] TAG_TRUE = {'t', 'r', 'u', 'e'};
	public static final byte[] TAG_FALSE = {'f', 'a', 'l', 's', 'e'};
	
	public static final String TRUE = "true";
	public static final String FALSE = "false";

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

	public boolean getValue() {
		return this.value;
	}

	@Override
	protected void readBeginTag(ObjectReader reader) {
	}

	@Override
	protected void readBody(ObjectReader reader) {
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
	protected void readEndTag(ObjectReader reader) {
	}
}
