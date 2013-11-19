package me.lumpchen.sledge.pdf.syntax.basic;

import me.lumpchen.sledge.pdf.reader.ObjectReader;
import me.lumpchen.sledge.pdf.syntax.PObject;

public class PStream extends PObject {

	public static final byte[] BEGIN = {'s', 't', 'r', 'e', 'a', 'm'};
	public static final byte[] END = {'e', 'n', 'd', 's', 't', 'r', 'e', 'a', 'm'};
	
	private PDictionary dict;
	private byte[] stream;
	
	public PStream() {
	}
	
	@Override
	public void read(ObjectReader reader) {
		
	}
}
