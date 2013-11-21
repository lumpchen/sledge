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
	
	public String toString() {
		StringBuilder buf = new StringBuilder();
		if (this.dict != null) {
			buf.append(this.dict.toString());
		}
		buf.append("stream");
		buf.append('\n');
		buf.append(new String(stream));
		buf.append('\n');
		buf.append("endstream");
		buf.append('\n');
		return buf.toString();
	}
	
	@Override
	protected void readBeginTag(ObjectReader reader) {
	}

	@Override
	protected void readBody(ObjectReader reader) {
		if (null == this.dict) {
			throw new NotMatchObjectException();
		}
	}

	@Override
	protected void readEndTag(ObjectReader reader) {
	}
}
