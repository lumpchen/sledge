package me.lumpchen.sledge.pdf.syntax.basic;

import me.lumpchen.sledge.pdf.reader.NotMatchObjectException;
import me.lumpchen.sledge.pdf.reader.ObjectReader;
import me.lumpchen.sledge.pdf.syntax.PObject;

public class PStream extends PObject {

	public static final byte[] BEGIN = {'s', 't', 'r', 'e', 'a', 'm'};
	public static final byte[] END = {'e', 'n', 'd', 's', 't', 'r', 'e', 'a', 'm'};
	
	private PDictionary dict;
	private byte[] stream;
	
	public PStream() {
	}
	
	public void setDict(PDictionary dict) {
		this.dict = dict;
	}
	
	@Override
	public void read(ObjectReader reader) {
		if (null == this.dict) {
			throw new NotMatchObjectException();
		}
		
		
	}
}
