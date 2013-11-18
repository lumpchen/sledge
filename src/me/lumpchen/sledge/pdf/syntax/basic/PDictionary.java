package me.lumpchen.sledge.pdf.syntax.basic;

import me.lumpchen.sledge.pdf.syntax.PObject;

public class PDictionary extends PObject {
	public static final byte[] BEGIN = { '<', '<' };
	public static final byte[] END = { '>', '>' };

	static {
		classMap.put("<<", PDictionary.class);
	}

}
