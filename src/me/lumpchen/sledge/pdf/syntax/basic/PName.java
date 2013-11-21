package me.lumpchen.sledge.pdf.syntax.basic;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import me.lumpchen.sledge.pdf.reader.InvalidNameException;
import me.lumpchen.sledge.pdf.reader.ObjectReader;
import me.lumpchen.sledge.pdf.syntax.PObject;

public class PName extends PObject {

	public static final byte BEGIN = '/';

	protected String name;

	public static final String SIZE = "Size";
	public static final PName size = new PName(SIZE);
	
	public static final String ROOT = "Root";
	public static final PName root = new PName(ROOT);
	
	public static final String ID = "ID";
	public static final PName id = new PName(ID);
	
	public static final String INFO = "Info";
	public static final PName info = new PName(INFO);
	
	private static Map<String, PName> nameMap = new HashMap<String, PName>();
	static {
		nameMap.put(ID, id);
		nameMap.put(INFO, info);
		nameMap.put(ROOT, root);
		nameMap.put(SIZE, size);
	}

	private PName(byte[] name) {
		this.name = new String(name, Charset.defaultCharset());
	}

	private PName(String name) {
		this.name = name;
	}

	public static PName instance(byte[] name) {
		String key = new String(name, Charset.defaultCharset());
		if (nameMap.containsKey(key)) {
			return nameMap.get(key);
		}
		throw new InvalidNameException(key);
	}

	public int hashCode() {
		return super.hashCode() + this.name.hashCode();
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PName)) {
			return false;
		}
		if (!(this.name.equals(((PName) obj).name))) {
			return false;
		}
		return true;
	}

	@Override
	public void read(ObjectReader reader) {
		// read directly, do nothing here
	}

	public String toString() {
		return "/" + this.name;
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
