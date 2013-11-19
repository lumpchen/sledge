package me.lumpchen.sledge.pdf.syntax.basic;

import me.lumpchen.sledge.pdf.reader.InvalidTagException;
import me.lumpchen.sledge.pdf.reader.ObjectReader;
import me.lumpchen.sledge.pdf.syntax.PObject;

public class PName extends PObject {

	public static final byte BEGIN = '/';
	
	private byte[] name;

	public PName() {
	}

	@Override
	public void read(ObjectReader reader) {
		byte tag = reader.readByte();
		if (tag != BEGIN) {
			throw new InvalidTagException();
		}
		byte[] bytes = reader.readToSpace();
		this.name = bytes;
	}
	
	public String toString() {
		return "/" + new String(this.name);
	}
}
